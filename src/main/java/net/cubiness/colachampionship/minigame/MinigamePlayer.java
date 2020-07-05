package net.cubiness.colachampionship.minigame;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.cubiness.colachampionship.scoreboard.ScoreboardDisplay;
import net.cubiness.colachampionship.scoreboard.section.ScoreboardSection;

public class MinigamePlayer {

  private final Player player;
  private Minigame currentMinigame = null;
  private final ScoreboardDisplay display;
  private final MinigameManager manager;

  public MinigamePlayer(Player p, MinigameManager manager) {
    player = p;
    this.manager = manager;
    display = new ScoreboardDisplay(this);
    display.showScoreboard();
  }

  public Minigame getCurrentMinigame() {
    return currentMinigame;
  }

  public void setCurrentMinigame(Minigame minigame) {
    assert currentMinigame == null;
    currentMinigame = minigame;
    player.sendMessage(ChatColor.GREEN + "Joining " + currentMinigame.getName());
    minigame.addPlayer(this);
    display.setSections(minigame.getScoreboard());
  }

  /**
   * Called when the player tries to leave whatever minigame they are in
   * @return true if they were in a minigame, false if otherwise
   */
  public boolean leaveCurrentMinigame() {
    if (currentMinigame == null) {
      return false;
    } else {
      currentMinigame.removePlayer(this);
      currentMinigame = null;
      return true;
    }
  }

  public void setScoreboard(Collection<ScoreboardSection> sections) {
    assert currentMinigame != null;
    assert currentMinigame.isRunning();
    display.setSections(sections);
  }

  public void setScoreboardTitle(String title) {
    display.setTitle(title);
  }

  public void showTotalScores() {
    assert currentMinigame == null;
    display.setSections(manager.getScoreboard().getTotalScoreboard());
  }

  public void showMinigameScore(Minigame game) {
    assert currentMinigame == null;
    display.setSections(game.getScoreboard());
  }

  public void teleport(Location l) {
    player.teleport(l);
  }

  public void updateScoreboard() {
    display.update();
  }

  public Player getPlayer() {
    return player;
  }
}
