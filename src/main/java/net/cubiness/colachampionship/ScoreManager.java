package net.cubiness.colachampionship;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScoreManager {

  private final Map<String, Integer> minigameScores = new HashMap<>();
  private final Map<String, Integer> totalScores = new HashMap<>();
  private final ScoreboardDisplay display;
  private boolean showingTotal = true;
  private String minigameName = "";
  private String title = "Cola2 Championship   ";

  public ScoreManager(Plugin plugin, ScoreboardDisplay display) {
    this.display = display;
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::updateTitle, 5, 5);
    showTotalScores();
  }

  public void addMinigameScore(String p, int amount) {
    if (minigameScores.containsKey(p)) {
      minigameScores.put(p, minigameScores.get(p) + amount);
    } else {
      minigameScores.put(p, amount);
    }
    update();
  }

  public void addTotalScore(String p, int amount) {
    if (totalScores.containsKey(p)) {
      totalScores.put(p, totalScores.get(p) + amount);
    } else {
      totalScores.put(p, amount);
    }
    update();
  }

  public void setTotalScore(String p, int amount) {
    totalScores.put(p, amount);
    update();
  }

  private void update() {
    if (showingTotal) {
      display.show("" + ChatColor.GREEN + ChatColor.BOLD + "Total Points", totalScores);
    } else {
      display.show("" + ChatColor.GREEN + ChatColor.BOLD + minigameName + " Points", minigameScores);
    }
  }

  private void updateTitle() {
    title = title.charAt(title.length() - 1) + title.substring(0, title.length() - 1);
    display.setTitle(ChatColor.BOLD + title);
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
