package net.cubiness.colachampionship.minigame;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.scoreboard.ScoreManager;

public class MinigameManager {

  private final Map<String, Minigame> minigames = new HashMap<>();
  private final Map<Player, MinigamePlayer> players = new HashMap<>();
  private final ScoreManager scoreManager;
  private final ColaCore cola;
  private Minigame runningGame;
  private final ConfigManager configs;
  private YamlConfiguration config;

  public MinigameManager(ColaCore cola, ScoreManager scoreManager) {
    this.cola = cola;
    this.scoreManager = scoreManager;
    configs = new ConfigManager(this, new File(cola.getDataFolder(), "minigames"));
    config = configs.get("hub");
    Bukkit.getScheduler().scheduleSyncRepeatingTask(cola, () -> scoreManager.updateTitle(getPlayers()), 5, 5);
  }

  public ConfigManager getConfigs() {
    return configs;
  }

  /**
   * Called when the entire ColaCore is being reset. All minigames should be killed if this happens
   */
  public void reset() {
    configs.save();
    if (runningGame != null) {
      runningGame.forceStop();
    }
    minigames.clear();
    players.clear();
    runningGame = null;
  }

  /**
   * Called when trying to reload configs
   */
  public void reload() {
    configs.reload();
    config = configs.get("hub");
  }

  /**
   * Called when an operator adds all players to a minigame
   *
   * @param game The minigame to add everyone to
   */
  public void joinAll(Minigame game) {
    assert runningGame == null;
    assert minigames.containsValue(game);
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
    });
  }

  /**
   * Called when a player tries to join a minigame
   *
   * @param game The minigame to join
   * @param player The player that is joining
   */
  public void join(Minigame game, MinigamePlayer player) {
    assert runningGame == null;
    assert minigames.containsValue(game);
    if (player.getCurrentMinigame() != null) {
      player.leaveCurrentMinigame();
    }
    player.setCurrentMinigame(game);
  }

  /**
   * Called when a player tries to leave a minigame
   *
   * @param player The player who is leaving
   */
  public void leave(MinigamePlayer player) {
    assert runningGame == null;
    player.leaveCurrentMinigame();
  }

  /**
   * Called when a player leaves the server
   * @param p The player that is leaving
   */
  public void onPlayerLeaveServer(Player p) {
    leave(getPlayer(p));
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
    Location loc = config.getLocation("lobby");
    if (loc == null) {
      loc = cola.getServer().getWorld("world").getSpawnLocation();
      config.set("lobby", loc);
    }
    return loc;
  }

  /**
   * Checks if a minigame exists
   * @param name The minigame name
   * @return true if the minigame has been registered, false if otherwise
   */
  public boolean hasMinigame(String name) {
    return minigames.containsKey(name);
  }

  /**
   * Used to get a minigame object based on a minigame name
   *
   * @param name The minigame name
   * @return The minigame object, if a minigame has been registered with that name
   */
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

  /**
   * Updates the scoreboard for all players
   */
  public void updateScoreboard() {
    players.values().forEach(p -> {
      p.updateScoreboard();
    });
  }

  /**
   * Gets all players in the server, represented by a MinigamePlayer
   *
   * @return A collection of minigamePlayers
   */
  public Collection<MinigamePlayer> getPlayers() {
    return players.values();
  }

  /**
   * Gets a minigamePlayer based on a spigot Player object
   *
   * @param p The player
   * @return The player represented by a minigamePlayer
   */
  public MinigamePlayer getPlayer(Player p) {
    MinigamePlayer player = players.get(p);
    if (player == null) {
      player = new MinigamePlayer(p, this);
      players.put(p, player);
    }
    return player;
  }

  /**
   * Returns the ColaCore plugin instance.
   *
   * @return The ColaCore plugin.
   */
  public ColaCore getCore() {
    return cola;
  }
}
