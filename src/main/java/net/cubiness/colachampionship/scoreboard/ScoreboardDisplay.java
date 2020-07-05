package net.cubiness.colachampionship.scoreboard;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import net.cubiness.colachampionship.minigame.MinigamePlayer;
import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;

public class ScoreboardDisplay {

  private final Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
  private final Objective scoreboardObjective;
  private final Map<Integer, String> scoreboardData = new HashMap<>();
  private final Collection<ScoreboardSection> sections = new HashSet<>();
  private final MinigamePlayer player;

  public ScoreboardDisplay(MinigamePlayer p) {
    player = p;
    scoreboardObjective = scoreboard
        .registerNewObjective(p.getPlayer().getUniqueId().toString().substring(24), "dummy", "Cola2 Championship");
    for (int i = 1; i < 16; i++) {
      String str = new String(new char[i]).replace('\0', ' ');
      scoreboardData.put(i, str);
      scoreboardObjective.getScore(str).setScore(i);
    }
    scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
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

  public void setSections(Collection<ScoreboardSection> newSections) {
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
    update();
  }

  public void update() {
    for (int i = 1; i < 16; i++) {
      setRow(i, " ");
    }
    for (ScoreboardSection s : sections) {
      s.update();
      List<String> contents = s.getContents(player);
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

  public void showScoreboard() {
    player.getPlayer().setScoreboard(scoreboard);
  }
}
