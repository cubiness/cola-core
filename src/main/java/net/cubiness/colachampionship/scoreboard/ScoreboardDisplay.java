package net.cubiness.colachampionship.scoreboard;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardDisplay {

  private final Scoreboard scoreboard;
  private final Objective scoreboardObjective;
  private final Map<Integer, String> scoreboardData = new HashMap<>();
  private final Set<ScoreboardSection> sections = new HashSet<>();

  public ScoreboardDisplay() {
    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    scoreboardObjective = scoreboard
        .registerNewObjective("championship", "dummy", "Cola2 Championship");
    for (int i = 1; i < 16; i++) {
      String str = new String(new char[i]).replace('\0', ' ');
      scoreboardData.put(i, str);
      scoreboardObjective.getScore(str).setScore(i);
    }
    scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(scoreboard));
  }

  private void setRow(int row, String data) {
    while (scoreboardData.containsValue(data)) {
      data += " ";
    }
    scoreboard.resetScores(scoreboardData.get(row));
    Score score = scoreboardObjective.getScore(data);
    score.setScore(row);
    scoreboardData.put(row, data);
  }

  public void setSections(Set<ScoreboardSection> newSections) {
    boolean[] filledRows = new boolean[16];
    filledRows[0] = true;
    for (ScoreboardSection s : newSections) {
      for (int i = s.getPosition(); i > s.getPosition() - s.getHeight(); i--) {
        if (filledRows[i]) {
          throw new RuntimeException("Sections at " + i + " overlap in scoreboard!");
        }
        filledRows[i] = true;
      }
    }
    sections.clear();
    sections.addAll(newSections);
  }

  public void update() {
    for (ScoreboardSection s : sections) {
      List<String> contents = s.getContents();
      int index = 0;
      for (int i = s.getPosition(); i > s.getPosition() - s.getHeight(); i--) {
        setRow(i, contents.get(index));
        index++;
      }
    }
  }

  public void setTitle(String title) {
    scoreboardObjective.setDisplayName(title);
  }

  public void showScoreboard(Player sender) {
    sender.setScoreboard(scoreboard);
  }
}
