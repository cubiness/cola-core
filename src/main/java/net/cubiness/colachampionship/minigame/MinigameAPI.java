package net.cubiness.colachampionship.minigame;

import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;
import org.bukkit.Location;

public class MinigameAPI {

  private final MinigameManager manager;

  public MinigameAPI(MinigameManager manager) {
    this.manager = manager;
  }

  public void registerMinigame(Minigame minigame) {
    manager.registerMinigame(minigame);
  }

  public void addSection(Minigame game, ScoreboardSection section) {
    manager.addMinigameSection(game, section);
  }

  public void updateScoreboard() {
    manager.getScoreboard().update();
  }

  public void finish(Minigame game) {
    manager.finish(game);
  }

  public Location getSpawn() {
    return manager.getSpawn();
  }
}
