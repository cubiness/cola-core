package net.cubiness.colachampionship.scoreboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;
import net.cubiness.colachampionship.scoreboard.section.LineSection;
import net.cubiness.colachampionship.scoreboard.section.PointsSection;
import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class ScoreManager {

  private final Set<ScoreboardSection> totalSections = new HashSet<>();
  private final Set<ScoreboardSection> minigameSections = new HashSet<>();
  private final PointsSection totalPoints;
  private final ScoreboardDisplay display;
  private final File totalPointsPath;
  private boolean showingTotal = true;
  private String minigameName = "";
  private String title = "Cola2 Championship   ";

  public ScoreManager(Plugin plugin, ScoreboardDisplay display) {
    this.display = display;
    Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::updateTitle, 5, 5);
    totalPoints = new PointsSection(13,
        "" + ChatColor.GREEN + ChatColor.BOLD + "Total Points",
        14);
    totalSections.add(totalPoints);
    totalSections.add(new LineSection(ChatColor.YELLOW + "www.cola2.net", 1));
    minigameSections.add(new LineSection(ChatColor.YELLOW + "www.cola2.net", 1));
    totalPointsPath = new File(plugin.getDataFolder().getAbsolutePath() + "/total-points.csv");
    if (!plugin.getDataFolder().exists()) {
      plugin.getDataFolder().mkdir();
    }
    plugin.getLogger().info("Total Points path: " + totalPointsPath);
    readTotals();
    showTotalScores();
  }

  public void addTotalScore(Player p, int amount) {
    totalPoints.addPoints(p, amount);
    update();
  }

  public void setTotalScore(Player p, int amount) {
    totalPoints.setPoints(p, amount);
    update();
  }

  public void update() {
    if (showingTotal) {
      saveTotals();
    }
    display.update();
  }

  private void readTotals() {
    BufferedReader w;
    try {
      w = new BufferedReader(new FileReader(totalPointsPath));
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    totalPoints.clear();
    try {
      String line;
      while ((line = w.readLine()) != null) {
        UUID id = UUID.fromString(line.split(":")[0]);
        int score = Integer.parseInt(line.split(":")[1]);
        totalPoints.setPoints(id, score);
      }
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void saveTotals() {
    BufferedWriter w;
    try {
      w = new BufferedWriter(new FileWriter(totalPointsPath));
    } catch (IOException e) {
      e.printStackTrace();
      return;
    }
    try {
      for (Iterator<UUID> it = totalPoints.getPlayers(); it.hasNext(); ) {
        UUID p = it.next();
        w.write(p + ":" + totalPoints.getPoints(p) + "\n");
      }
      w.flush();
      w.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void updateTitle() {
    title = title.charAt(title.length() - 1) + title.substring(0, title.length() - 1);
    display.setTitle(ChatColor.BOLD + title);
  }

  public void showMinigameScores() {
    showingTotal = false;
    display.setSections(minigameSections);
    update();
  }

  public void showTotalScores() {
    showingTotal = true;
    display.setSections(totalSections);
    update();
  }

  public void setMinigameName(String name) {
    minigameName = name;
  }

  public void setMinigameSections(Set<ScoreboardSection> sections) {
    minigameSections.clear();
    minigameSections.addAll(sections);
    update();
  }

  public void clearMinigameSections() {
    minigameSections.clear();
    update();
  }
}
