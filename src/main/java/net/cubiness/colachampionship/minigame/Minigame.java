package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.bukkit.Location;

import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;

public abstract class Minigame {

  protected final MinigameAPI api;
  protected Collection<ScoreboardSection> scoreboard = new HashSet<>();
  private final List<MinigamePlayer> players = new ArrayList<>();
  private boolean running = false;

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
  }

  public final void addPlayer(MinigamePlayer p) {
    if (!players.contains(p)) {
      players.add(p);
      p.teleport(getLobby());
      onPlayerJoin(p);
    }
  }

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

  protected final boolean isRunning() {
    return running;
  }

  protected final Iterator<MinigamePlayer> getPlayers() {
    return players.iterator();
  }

  protected final int getNumPlayers() {
    return players.size();
  }

  public final String checkStart() {
    if (players.size() < getMinimumPlayers()) {
      return getName() + " needs at least " + getMinimumPlayers() + " players to start!";
    }
    return null;
  }

  public final void reset() {
    players.clear();
    onReset();
  }

  public final Collection<ScoreboardSection> getScoreboard() {
    return scoreboard;
  }

  protected abstract void onReset();

  protected abstract void onPlayerJoin(MinigamePlayer p);

  protected abstract void onPlayerLeave(MinigamePlayer p);

  public abstract void onStart();

  public abstract void onForceStop();

  public abstract Location getLobby();

  public abstract String getName();

  public abstract int getMinimumPlayers();
}
