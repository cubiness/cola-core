package net.cubiness.colachampionship.scoreboard;

import java.util.HashSet;
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

  private void saveTotals() {
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

  public void addMinigameSection(ScoreboardSection section) {
    minigameSections.add(section);
    update();
  }

  public void clearMinigameSections() {
    minigameSections.clear();
    update();
  }
}
