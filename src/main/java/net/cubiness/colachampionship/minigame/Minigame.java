package net.cubiness.colachampionship.minigame;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public abstract class Minigame {

  protected final MinigameAPI api;
  private boolean running = false;
  private List<OfflinePlayer> players = new ArrayList<>();

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
  }

  public void addPlayer(Player p) {
    players.add(p);
  }

  public void removePlayer(Player p) {
    players.remove(p);
  }

  public abstract Location getLobby();

  public void start() {
    running = true;
    onStart();
  }

  public abstract void onStart();

  public abstract String getName();
}
