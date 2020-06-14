package net.cubiness.colachampionship.minigame;

import java.util.HashMap;
import java.util.Map;
import net.cubiness.colachampionship.ScoreManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class MinigameManager {

  private final Map<String, Minigame> minigames = new HashMap<>();
  private final ScoreManager scoreManager;
  private final MinigameAPI api = new MinigameAPI(this);
  private Minigame runningGame;

  public MinigameManager(ScoreManager scoreManager) {
    this.scoreManager = scoreManager;
  }

  public void reset() {
    minigames.clear();
    runningGame = null;
  }

  public void clearScores() {
    scoreManager.clearMinigameScores();
  }

  public void start(String minigameName) {
    if (runningGame != null) {
      throw new RuntimeException("Minigame " + runningGame.getName() + " is already running!");
    }
    if (!minigames.containsKey(minigameName)) {
      throw new RuntimeException("Minigame " + minigameName + " has not been registered!");
    }
    Minigame game = minigames.get(minigameName);
    String error = game.checkStart();
    if (error != null) {
      Bukkit.broadcastMessage(ChatColor.RED + error);
      return;
    }
    Bukkit.broadcastMessage(ChatColor.YELLOW + minigameName + " is starting!");
    runningGame = game;
    scoreManager.setMinigameName(runningGame.getName());
    scoreManager.showMinigameScores();
    runningGame.start();
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
    Bukkit.broadcastMessage(ChatColor.YELLOW + game.getName() + " has ended!");
    if (runningGame == game) {
      runningGame = null;
    } else {
      throw new RuntimeException("Minigame " + game.getName() + " is not running!");
    }
  }

  public Location getSpawn() {
    return new Location(Bukkit.getWorld("world"), 0, 201, 0);
  }

  public boolean hasMinigame(String name) {
    return minigames.containsKey(name);
  }

  public boolean running() {
    return runningGame != null;
  }
}
