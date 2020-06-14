package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Minigame {

  protected final MinigameAPI api;
  private final List<Player> players = new ArrayList<>();
  private boolean running = false;

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
  }

  public final void addPlayer(Player p) {
    if (!players.contains(p)) {
      players.add(p);
      p.teleport(getLobby());
      onPlayerJoin(p);
    }
  }

  public final void removePlayer(Player p) {
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

  /***
   * Called from ColaCore when a minigame needs to stop
   */
  public final void forceStop() {
    running = false;
    onForceStop();
  }

  protected final boolean isRunning() {
    return running;
  }

  protected final Iterator<Player> getPlayers() {
    return players.iterator();
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

  protected abstract void onReset();

  protected abstract void onPlayerJoin(Player p);

  protected abstract void onPlayerLeave(Player p);

  public abstract void onStart();

  public abstract void onForceStop();

  public abstract Location getLobby();

  public abstract String getName();

  public abstract int getMinimumPlayers();
}
