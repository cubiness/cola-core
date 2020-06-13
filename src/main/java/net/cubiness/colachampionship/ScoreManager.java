package net.cubiness.colachampionship;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

public class ScoreManager {

  private final Map<OfflinePlayer, Integer> minigameScores = new HashMap<>();
  private final Map<OfflinePlayer, Integer> totalScores = new HashMap<>();
  private final ScoreboardDisplay display;
  private boolean showingTotal = true;
  private String minigameName = "";

  public ScoreManager(ScoreboardDisplay display) {
    this.display = display;
  }

  public void addMinigameScore(OfflinePlayer p, int amount) {
    if (minigameScores.containsKey(p)) {
      minigameScores.put(p, minigameScores.get(p) + amount);
    } else {
      minigameScores.put(p, amount);
    }
    update();
  }

  public void addTotalScore(OfflinePlayer p, int amount) {
    if (totalScores.containsKey(p)) {
      totalScores.put(p, totalScores.get(p) + amount);
    } else {
      totalScores.put(p, amount);
    }
    update();
  }

  private void update() {
    if (showingTotal) {
      display.show("" + ChatColor.GREEN + ChatColor.BOLD + "Total Points", totalScores);
    } else {
      display.show("" + ChatColor.GREEN + ChatColor.BOLD + minigameName + " Points", minigameScores);
    }
  }

  public void showMinigameScores() {
    showingTotal = false;
    update();
  }

  public void showTotalScores() {
    showingTotal = true;
    update();
  }

  public void setMinigameName(String name) {
    minigameName = name;
  }

  public void clearMinigameScores() {
    minigameScores.clear();
  }
}
