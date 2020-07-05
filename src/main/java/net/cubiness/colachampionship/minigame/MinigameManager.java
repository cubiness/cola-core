package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.scoreboard.ScoreManager;

public class MinigameManager {

  private final Map<String, Minigame> minigames = new HashMap<>();
  private final Map<Player, MinigamePlayer> players = new HashMap<>();
  private final ScoreManager scoreManager;
  private final MinigameAPI api = new MinigameAPI(this);
  private final ColaCore cola;
  private Minigame runningGame;

  public MinigameManager(ColaCore cola, ScoreManager scoreManager) {
    this.cola = cola;
    this.scoreManager = scoreManager;
    Bukkit.getScheduler().scheduleSyncRepeatingTask(cola, () -> scoreManager.updateTitle(getPlayers()), 5, 5);
  }

  /**
   * Called when the entire ColaCore is being reset. All minigames should be killed if this happens
   */
  public void reset() {
    if (runningGame != null) {
      runningGame.forceStop();
    }
    minigames.clear();
    players.clear();
    runningGame = null;
  }

  /**
   * Called when an operator adds all players to a minigame
   * @param minigameName The minigame to add everyone to
   */
  public void addAll(String minigameName) {
    if (runningGame != null) {
      throw new RuntimeException("Minigame " + runningGame.getName() + " is already running!");
    }
    if (!minigames.containsKey(minigameName)) {
      throw new RuntimeException("Minigame " + minigameName + " has not been registered!");
    }
    Minigame game = minigames.get(minigameName);
    Bukkit.getOnlinePlayers().forEach(p -> {
      MinigamePlayer player = players.get(p);
      if (player == null) {
        player = new MinigamePlayer(p, this);
        players.put(p, player);
      }
      if (player.getCurrentMinigame() != null) {
        player.leaveCurrentMinigame();
      }
      player.setCurrentMinigame(game);
      minigames.get(minigameName).addPlayer(player);
    });
  }

  /**
   * Called when a player leaves a minigame
   * @param p The player
   * @return true if the player was in a minigame, false if otherwise
   */
  public boolean onPlayerLeaveMinigame(Player p) {
    if (players.get(p) == null) {
      players.put(p, new MinigamePlayer(p, this));
    }
    return players.get(p).leaveCurrentMinigame();
  }

  /**
   * Called when a player leaves the server
   * @param p The player that is leaving
   */
  public void onPlayerLeaveServer(Player p) {
    onPlayerLeaveMinigame(p);
    players.remove(p);
  }

  /**
   * Called when an operator starts a minigame
   * @param minigameName The minigame they are starting
   */
  public void start(String minigameName) {
    assert runningGame == null;
    assert minigames.containsKey(minigameName);
    Minigame game = minigames.get(minigameName);
    String error = game.checkStart();
    if (error != null) {
      Bukkit.broadcastMessage(ChatColor.RED + error);
      return;
    }
    Bukkit.broadcastMessage(ChatColor.YELLOW + minigameName + " is starting!");
    runningGame = game;
    runningGame.start();
  }

  /**
   * Called when a minigame is stopped by an operator
   */
  public void stop() {
    if (runningGame == null) {
      throw new RuntimeException("No minigame is running!");
    }
    runningGame.forceStop();
    for (MinigamePlayer p : players.values()) {
      p.leaveCurrentMinigame();
    }
    runningGame = null;
  }

  public MinigameAPI getAPI() {
    return api;
  }

  /**
   * Called when a minigame plugin first loads. Should only be called when the server is loading
   * @param minigame The new minigame to load
   */
  public void registerMinigame(Minigame minigame) {
    if (minigames.containsKey(minigame.getName())) {
      throw new RuntimeException(
          "Minigame " + minigame.getName() + " has already been registered!");
    }
    minigames.put(minigame.getName(), minigame);
    cola.updateTabComplete(true, false);
  }

  /**
   * Called when a minigame is finished, either by the minigame ending itself or by an operator
   * @param game The minigame that is finishing
   */
  public void finish(Minigame game) {
    if (runningGame == game) {
      Bukkit.broadcastMessage(ChatColor.YELLOW + game.getName() + " has ended!");
      runningGame.reset();
      for (MinigamePlayer p : players.values()) {
        if (p.getCurrentMinigame() == runningGame) {
          p.leaveCurrentMinigame();
        }
      }
      runningGame = null;
    } else {
      throw new RuntimeException("Minigame " + game.getName() + " is not running!");
    }
  }

  /**
   * Gets the location to teleport everyone to once a minigame ends
   * @return The location of the lobby
   */
  public Location getSpawn() {
    return new Location(Bukkit.getWorld("world"), 0, 201, 0);
  }

  /**
   * Checks if a minigame exists
   * @param name The minigame name
   * @return true if the minigame has been registered, false if otherwise
   */
  public boolean hasMinigame(String name) {
    return minigames.containsKey(name);
  }

  public Minigame getMinigame(String name) {
    assert minigames.containsKey(name);
    return minigames.get(name);
  }

  /**
   * Checks if a minigame is running
   * @return true if a minigame is running, false if otherwise
   */
  public boolean running() {
    return runningGame != null;
  }

  /**
   * Gets an ArrayList of registered minigames
   * @return The list of minigames
   */
  public List<String> getMinigameList() {
    return new ArrayList<>(minigames.keySet());
  }

  /**
   * Gets the ScoreboardManager
   * @return the ScoreboardManager
   */
  public ScoreManager getScoreboard() {
    return scoreManager;
  }

  public Collection<MinigamePlayer> getPlayers() {
    return players.values();
  }

  public MinigamePlayer getPlayer(Player p) {
    MinigamePlayer player = players.get(p);
    if (player == null) {
      player = new MinigamePlayer(p, this);
      players.put(p, player);
    }
    return player;
  }
}
