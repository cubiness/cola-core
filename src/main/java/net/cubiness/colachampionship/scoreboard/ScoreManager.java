package net.cubiness.colachampionship.scoreboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

import org.bukkit.ChatColor;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.minigame.MinigamePlayer;
import net.cubiness.colachampionship.scoreboard.section.LineSection;
import net.cubiness.colachampionship.scoreboard.section.PointsSection;
import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;

public class ScoreManager {

  private final Set<ScoreboardSection> totalSections = new HashSet<>();
  private final PointsSection totalPoints;
  private final File totalPointsPath;
  private String title = "Cola2 Championship   ";

  public ScoreManager(ColaCore core) {
    totalPoints = new PointsSection(13,
        "" + ChatColor.GREEN + ChatColor.BOLD + "Total Points",
        14);
    totalSections.add(totalPoints);
    totalSections.add(new LineSection(ChatColor.YELLOW + "www.cola2.net", 1));
    totalPointsPath = new File(core.getDataFolder().getAbsolutePath() + "/total-points.csv");
    if (!core.getDataFolder().exists()) {
      core.getDataFolder().mkdir();
    }
    core.getLogger().info("Total Points path: " + totalPointsPath);
    readTotals();
  }

  /**
   * Gives the player points
   *
   * @param p The player
   * @param amount The amount of points to give
   */
  public void addTotalScore(MinigamePlayer p, int amount) {
    totalPoints.addPoints(p, amount);
    save();
  }

  /**
   * Gives the player points
   *
   * @param id The UUID of a player
   * @param amount The amount of points to give
   */
  public void addTotalScore(UUID id, int amount) {
    totalPoints.addPoints(id, amount);
    save();
  }

  /**
   * Sets the points of a player
   *
   * @param p The player
   * @param amount The points to set the player at
   */
  public void setTotalScore(MinigamePlayer p, int amount) {
    totalPoints.setPoints(p, amount);
    save();
  }

  /**
   * Sets the points of a player
   *
   * @param id The UUID of a player
   * @param amount The points to set the player at
   */
  public void setTotalScore(UUID id, int amount) {
    totalPoints.setPoints(id, amount);
    save();
  }

  /**
   * Will read the total scores from a file
   */
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

  /**
   * Will save the total scores from a file
   */
  public void save() {
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

  /**
   * Updates the title for the given players
   *
   * @param players The players to set the title
   */
  public void updateTitle(Collection<MinigamePlayer> players) {
    title = title.charAt(title.length() - 1) + title.substring(0, title.length() - 1);
    players.forEach(p -> p.setScoreboardTitle(title));
  }

  /**
   * Gets the title of the scoreboard
   */
  public String getTitle() {
    return title;
  }

  /**
   * Gets a collection of ScoreboardSections that the player should display if they are looking at the total score
   *
   * @return The ScoreboardSections
   */
  public Collection<ScoreboardSection> getTotalScoreboard() {
    return totalSections;
  }
}
