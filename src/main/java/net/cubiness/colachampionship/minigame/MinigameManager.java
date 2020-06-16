package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.scoreboard.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MinigameManager {

  private final Map<String, Minigame> minigames = new HashMap<>();
  private final ScoreManager scoreManager;
  private final MinigameAPI api = new MinigameAPI(this);
  private final Map<Player, MinigamePlayer> players = new HashMap<>();
  private final ColaCore cola;
  private Minigame runningGame;

  public MinigameManager(ColaCore cola, ScoreManager scoreManager) {
    this.cola = cola;
    this.scoreManager = scoreManager;
  }

  /***
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

  public void resetMinigame() {
    scoreManager.clearMinigameSections();
  }

  public void addPlayer(Player p, String minigameName) {
    if (runningGame != null) {
      throw new RuntimeException("Minigame " + runningGame.getName() + " is already running!");
    }
    if (!minigames.containsKey(minigameName)) {
      throw new RuntimeException("Minigame " + minigameName + " has not been registered!");
    }
    MinigamePlayer player = players.get(p);
    if (player == null) {
      player = new MinigamePlayer(p);
      players.put(p, player);
    }
    if (player.getCurrentMinigame() != null) {
      p.sendMessage(ChatColor.RED + "You are already in " + player.getCurrentMinigame().getName());
      p.sendMessage(ChatColor.RED + "Please leave this game first with /lobby or /leave");
      return;
    }
    Minigame game = minigames.get(minigameName);
    player.setCurrentMinigame(game);
    minigames.get(minigameName).addPlayer(p);
  }

  /**
   * Called when a player leaves a minigame
   *
   * @param p The player
   * @return true if the player was in a minigame, false if otherwise
   */
  public boolean onPlayerLeaveMinigame(Player p) {
    if (players.get(p) == null) {
      players.put(p, new MinigamePlayer(p));
    }
    return players.get(p).leaveCurrentMinigame();
  }

  public void onPlayerLeaveServer(Player p) {
    onPlayerLeaveMinigame(p);
    players.remove(p);
  }

  public void start(String minigameName) {
    if (runningGame != null) {
      throw new RuntimeException("Minigame " + runningGame.getName() + " is already running!");
    }
    if (!minigames.containsKey(minigameName)) {
      throw new RuntimeException("Minigame " + minigameName + " has not been registered!");
    }
    Minigame game = minigames.get(minigameName);
    String error = game.checkStart();
    if (error != null) {
      Bukkit.broadcastMessage(ChatColor.RED + error);
      return;
    }
    Bukkit.broadcastMessage(ChatColor.YELLOW + minigameName + " is starting!");
    runningGame = game;
    scoreManager.setMinigameName(runningGame.getName());
    scoreManager.showMinigameScores();
    runningGame.start();
  }

  public void stop() {
    if (runningGame == null) {
      throw new RuntimeException("No minigame is running!");
    }
    runningGame.forceStop();
    for (MinigamePlayer p : players.values()) {
      if (p.getCurrentMinigame() == runningGame) {
        p.leaveCurrentMinigame();
      }
    }
    runningGame = null;
  }

  public MinigameAPI getAPI() {
    return api;
  }

  public void registerMinigame(Minigame minigame) {
    if (minigames.containsKey(minigame.getName())) {
      throw new RuntimeException(
          "Minigame " + minigame.getName() + " has already been registered!");
    }
    minigames.put(minigame.getName(), minigame);
    cola.updateTabComplete(true, false);
  }

  public void finish(Minigame game) {
    if (runningGame == game) {
      Bukkit.broadcastMessage(ChatColor.YELLOW + game.getName() + " has ended!");
      runningGame.reset();
      runningGame = null;
    } else {
      throw new RuntimeException("Minigame " + game.getName() + " is not running!");
    }
  }

  public Location getSpawn() {
    return new Location(Bukkit.getWorld("world"), 0, 201, 0);
  }

  public boolean hasMinigame(String name) {
    return minigames.containsKey(name);
  }

  public boolean running() {
    return runningGame != null;
  }

  public List<String> getMinigameList() {
    return new ArrayList<>(minigames.keySet());
  }

  public ScoreManager getScoreboard() {
    return scoreManager;
  }
}
