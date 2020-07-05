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

  public void updateScoreboard() {
    manager.getPlayers().forEach(p -> p.updateScoreboard());
  }

  public void finish(Minigame game) {
    manager.finish(game);
  }

  public Location getSpawn() {
    return manager.getSpawn();
  }
}
