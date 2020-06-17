package net.cubiness.colachampionship.scoreboard.section;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PointsSection extends ScoreboardSection {

  private final Map<UUID, Integer> points = new HashMap<>();

  public PointsSection(int height, String title, int row) {
    super(height, title, row);
  }

  public void setPoints(UUID id, int score) {
    points.put(id, score);
    updateRows();
  }

  private void updateRows() {
    clearRows();
    List<UUID> ids = points.keySet().stream()
        .sorted((a, b) -> points.get(b).compareTo(points.get(a)))
        .collect(Collectors.toList());
    int i = 0;
    for (UUID id : ids) {
      setRow(i, Bukkit.getPlayer(id).getName() + ": " + points.get(id));
      i++;
    }
  }

  public void setPoints(Player p, int score) {
    setPoints(p.getUniqueId(), score);
  }

  public void addPoints(UUID id, int score) {
    if (points.containsKey(id)) {
      points.put(id, points.get(id) + score);
    } else {
      points.put(id, score);
    }
    updateRows();
  }

  public void addPoints(Player p, int score) {
    addPoints(p.getUniqueId(), score);
  }
}