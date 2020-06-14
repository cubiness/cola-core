package net.cubiness.colachampionship.minigame;

import org.bukkit.Location;

public class MinigameAPI {

  private final MinigameManager manager;

  public MinigameAPI(MinigameManager manager) {
    this.manager = manager;
  }

  public void registerMinigame(Minigame minigame) {
    manager.registerMinigame(minigame);
  }

  public void addScore(String p, int amount) {
    manager.addScore(p, amount);
  }

  public void finish(Minigame game) {
    manager.finish(game);
  }

  public Location getSpawn() {
    return manager.getSpawn();
  }
}
