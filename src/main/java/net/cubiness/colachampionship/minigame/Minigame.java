package net.cubiness.colachampionship.minigame;

public abstract class Minigame {

  protected final MinigameAPI api;

  public Minigame(MinigameAPI api) {
    this.api = api;
    api.registerMinigame(this);
  }

  public abstract void onStart();

  public abstract String getName();
}
