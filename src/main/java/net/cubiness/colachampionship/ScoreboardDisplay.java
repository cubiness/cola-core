package net.cubiness.colachampionship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

public class ScoreboardDisplay {

  private final Scoreboard scoreboard;
  private final Objective scoreboardObjective;
  private final Map<Integer, String> scoreboardData = new HashMap<>();

  public ScoreboardDisplay() {
    scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
    scoreboardObjective = scoreboard
        .registerNewObjective("championship", "dummy", "Cola2 Championship");
    for (int i = 1; i < 16; i++) {
      String str = "Score: " + i;
      scoreboardData.put(i, str);
      scoreboardObjective.getScore(str).setScore(i);
    }
    scoreboardObjective.setDisplaySlot(DisplaySlot.SIDEBAR);
    Bukkit.getOnlinePlayers().forEach(p -> {
      p.setScoreboard(scoreboard);
    });
  }

  public void setRow(int row, String data) {
    while (scoreboardData.containsValue(data)) {
      data += " ";
    }
    scoreboard.resetScores(scoreboardData.get(row));
    Score score = scoreboardObjective.getScore(data);
    score.setScore(row);
    scoreboardData.put(row, data);
  }

  public void show(Map<OfflinePlayer, Integer> scores) {
  }

  public void update() {
  }
}
