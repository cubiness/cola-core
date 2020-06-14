package net.cubiness.colachampionship;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
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

  public void show(String title, Map<String, Integer> scores) {
    int index = 0;
    List<Entry<String, Integer>> entries = new ArrayList<>(scores.entrySet());
    entries.sort(Comparator.comparingInt(Entry::getValue));
    setRow(15, "");
    setRow(14, title);
    setRow(2, "");
    setRow(1, ChatColor.YELLOW + "www.cola2.net");
    for (int i = 13; i >= 3; i--) {
      if (index < entries.size()) {
        Entry<String, Integer> e = entries.get(index);
        setRow(i, e.getKey() + ": " + e.getValue());
      } else {
        setRow(i, "");
      }
      index++;
    }
  }

  public void setTitle(String title) {
    scoreboardObjective.setDisplayName(title);
  }

  public void showScoreboard(Player sender) {
    sender.setScoreboard(scoreboard);
  }
}
