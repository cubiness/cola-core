package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;

public abstract class Minigame {

  protected final MinigameAPI api;
  protected Collection<ScoreboardSection> scoreboard = new HashSet<>();
  private final List<MinigamePlayer> players = new ArrayList<>();
  private boolean running = false;
  private final YamlConfiguration config;

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
    config = api.getConfig(this);
    if (config == null) {
      throw new RuntimeException("Cannot load config for minigame " + getName());
    }
  }

  /**
   * Called from ColaCore when a new player joins the game
   *
   * @param p The new player
   */
  public final void addPlayer(MinigamePlayer p) {
    if (!players.contains(p)) {
      players.add(p);
      p.teleport(getLobby());
      onPlayerJoin(p);
    }
  }

  /**
   * Called from ColaCore when a player leaves the minigame
   *
   * @param p The player that is leaving
   */
  public final void removePlayer(MinigamePlayer p) {
    if (players.contains(p)) {
      players.remove(p);
      p.teleport(api.getSpawn());
      onPlayerLeave(p);
    }
  }

  /**
   * Called from ColaCore when a minigame needs to start
   */
  public final void start() {
    running = true;
    onStart();
  }

  /**
   * Called from ColaCore when a minigame needs to stop
   */
  public final void forceStop() {
    running = false;
    onForceStop();
  }

  /**
   * Getter to see if this minigame is running
   */
  protected final boolean isRunning() {
    return running;
  }

  /**
   * Checks if the players map contains the given player
   *
   * @param p The player to check
   * @return true if they are in the minigame, false if otherwise
   */
  public boolean hasPlayer(Player p) {
    for (MinigamePlayer player : players) {
      if (player.getPlayer().getUniqueId() == p.getUniqueId()) {
        return true;
      }
    }
    return false;
  }

  /**
   * Getter to get a list of players that are in this game
   *
   * @return An iterator for all players current in the game or it's lobby
   */
  protected final Iterator<MinigamePlayer> getPlayers() {
    return players.iterator();
  }

  /**
   * Getter to get the number of players in this game
   *
   * @return The number of players
   */
  protected final int getNumPlayers() {
    return players.size();
  }

  /**
   * Called to see if the minigame meets the conditions to start
   *
   * @return A string that is either an error message, or null if you can start
   */
  public final String checkStart() {
    if (players.size() < getMinimumPlayers()) {
      return getName() + " needs at least " + getMinimumPlayers() + " players to start!";
    }
    return null;
  }

  /**
   * Called from ColaCore when this minigame is about to be destroyed
   */
  public final void reset() {
    players.clear();
    onReset();
  }

  /**
   * Called from ColaCore when it needs to display the scoreboard ofr this minigame
   *
   * @return A Collection of ScoreboardSection that will be used to display the scoreboard of the side
   */
  public final Collection<ScoreboardSection> getScoreboard() {
    return scoreboard;
  }

  /**
   * Called from the minigame when a player joins, and needs to be teleported to the lobby
   *
   * @return The location of the lobby
   */
  public final Location getLobby() {
    Location loc = config.getLocation("lobby");
    if (loc == null) {
      loc = api.getCore().getServer().getWorld("world").getSpawnLocation();
      config.set("lobby", loc);
    }
    return loc;
  }

  /**
   * Gets the config for ths minigame
   *
   * @return The Config object
   */
  public final YamlConfiguration getConfig() {
     return config;
  }

  /**
   * Called from the minigame when this game is about to be destroyed
   */
  protected abstract void onReset();

  /**
   * Called from the minigame when a player joins the game
   *
   * @param p The player who is joining
   */
  protected abstract void onPlayerJoin(MinigamePlayer p);

  /**
   * Called from the minigame when a player leaves the game
   *
   * @param p The player who is leaving
   */
  protected abstract void onPlayerLeave(MinigamePlayer p);

  /**
   * Called from the minigame when the minigame is starting
   */
  protected abstract void onStart();

  /**
   * Called from the minigame when the minigame is getting force stopped
   */
  protected abstract void onForceStop();

  /**
   * Called from ColaCore when registering the minigame
   *
   * @return A unique name to represent this minigame
   */
  public abstract String getName();

  /**
   * Called from the minigame when it is checking if it can start
   *
   * @return The minimum amount if players required to start
   */
  public abstract int getMinimumPlayers();
}
