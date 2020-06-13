package net.cubiness.colachampionship.minigame;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;

public abstract class Minigame {

  protected final BukkitScheduler scheduler;
  protected final MinigameAPI api;

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
    scheduler = Bukkit.getScheduler();
  }

  public abstract void onStart();
  public abstract String getName();
}
