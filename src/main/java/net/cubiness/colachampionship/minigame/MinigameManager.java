package net.cubiness.colachampionship.minigame;

import java.util.HashMap;
import java.util.Map;
import net.cubiness.colachampionship.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;

public class MinigameManager {

  private final Map<String, Minigame> minigames = new HashMap<>();
  private final ScoreManager scoreManager;
  private final MinigameAPI api = new MinigameAPI(this);
  private Minigame selectedMinigame;
  private boolean runningGame = false;

  public MinigameManager(ScoreManager scoreManager) {
    this.scoreManager = scoreManager;
  }

  public void reset() {
    minigames.clear();
    selectedMinigame = null;
    runningGame = false;
  }

  public void clearScores() {
    scoreManager.clearMinigameScores();
  }

  public void start() {
    if (runningGame) {
      throw new RuntimeException("Minigame " + selectedMinigame.getName() + " is already running!");
    }
    scoreManager.showMinigameScores();
    selectedMinigame.start();
    runningGame = true;
    scoreManager.setMinigameName(selectedMinigame.getName());
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
    if (runningGame && selectedMinigame == game) {
      runningGame = false;
      selectedMinigame = null;
    } else {
      throw new RuntimeException("Minigame " + selectedMinigame.getName() + " is not running!");
    }
  }

  public Location getSpawn() {
    return new Location(Bukkit.getWorld("world"), 0, 201, 0);
  }

  public boolean hasMinigame(String name) {
    return minigames.containsKey(name);
  }

  public void select(String name) {
    if (runningGame) {
      throw new RuntimeException("Minigame is already running!");
    }
    if (!minigames.containsKey(name)) {
      throw new RuntimeException("Minigame " + name + " has not been registered!");
    }
    selectedMinigame = minigames.get(name);
  }
}
