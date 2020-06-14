package net.cubiness.colachampionship.minigame;

import java.util.HashMap;
import java.util.Map;
import net.cubiness.colachampionship.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MinigameManager {

  private final Map<String, Minigame> minigames = new HashMap<>();
  private final ScoreManager scoreManager;
  private Minigame runningGame;
  private final MinigameAPI api = new MinigameAPI(this);

  public MinigameManager(ScoreManager scoreManager) {
    this.scoreManager = scoreManager;
  }

  public void clearScores() {
    scoreManager.clearMinigameScores();
  }

  public void start(String name) {
    Minigame game = minigames.get(name);
    if (game == null) {
      throw new RuntimeException("Minigame " + name + " does not exist!");
    }
    if (runningGame != null) {
      throw new RuntimeException("Minigame " + runningGame.getName() + " is already running!");
    }
    scoreManager.showMinigameScores();
    game.onStart();
    runningGame = game;
    scoreManager.setMinigameName(game.getName());
  }

  public void addScore(String p, int amount) {
    scoreManager.addMinigameScore(p, amount);
  }

  public MinigameAPI getAPI() {
    return api;
  }

  public void registerMinigame(Minigame minigame) {
    if (minigames.containsKey(minigame.getName())) {
      throw new RuntimeException(
          "Minigame " + minigame.getName() + " has already been registered!");
    }
    minigames.put(minigame.getName(), minigame);
  }

  public void finish(Minigame game) {
    if (runningGame != game) {
      throw new RuntimeException("Minigame " + runningGame.getName() + " is not running!");
    }
    runningGame = null;
  }

  public Location getSpawn() {
    return new Location(Bukkit.getWorld("world"), 0, 201, 0);
  }
}
