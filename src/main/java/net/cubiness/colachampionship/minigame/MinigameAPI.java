package net.cubiness.colachampionship.minigame;

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
}
