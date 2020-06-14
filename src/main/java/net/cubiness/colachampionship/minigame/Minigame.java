package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class Minigame {

  protected final MinigameAPI api;
  private boolean running = false;
  private final List<Player> players = new ArrayList<>();

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
  }

  public final void addPlayer(Player p) {
    players.add(p);
    p.teleport(getLobby());
    onPlayerJoin(p);
  }

  public final void removePlayer(Player p) {
    players.remove(p);
    p.teleport(api.getSpawn());
    onPlayerLeave(p);
  }

  public final void start() {
    running = true;
    onStart();
  }

  public final void stop() {
    running = false;
    forceStop();
  }

  protected final boolean isRunning() {
    return running;
  }

  protected final Iterator<Player> getPlayers() {
    return players.iterator();
  }

  protected abstract void onPlayerJoin(Player p);

  protected abstract void onPlayerLeave(Player p);

  public abstract void onStart();

  public abstract void forceStop();

  public abstract Location getLobby();

  public abstract String getName();
}
