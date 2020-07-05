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
    display.setSections(manager.getScoreboard().getTotalScoreboard());
    display.showScoreboard();
  }

  /**
   * Gets the current minigame that a player is in
   *
   * @return A minigame object if the player is in a minigame, null if otherwise
   */
  public Minigame getCurrentMinigame() {
    return currentMinigame;
  }

  /**
   * Sets the current minigame for a playerm, if they are not already in a minigame
   *
   * @param minigame The new minigame a player should join
   */
  public void setCurrentMinigame(Minigame minigame) {
    assert currentMinigame == null;
    currentMinigame = minigame;
    player.sendMessage(ChatColor.GREEN + "Joining " + currentMinigame.getName());
    minigame.addPlayer(this);
    display.setSections(minigame.getScoreboard());
  }

  /**
   * Called when the player tries to leave whatever minigame they are in
   *
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

  /**
   * Sets the scoreboard that a player sees. This only works if they are in a minigame, and it is running
   *
   * @param sections A collection of ScoreboardSections that represents the scoreboard they should see
   */
  public void setScoreboard(Collection<ScoreboardSection> sections) {
    assert currentMinigame != null;
    assert currentMinigame.isRunning();
    display.setSections(sections);
  }

  /**
   * Sets the title of the scoreboard. Used for the animated Cola Chamionship sign.
   * Should not be used by minigames.
   *
   * @param title The new title to use
   */
  public void setScoreboardTitle(String title) {
    display.setTitle(title);
  }

  /**
   * Will display the total scores. Only works if the player is in the hub
   */
  public void showTotalScores() {
    assert currentMinigame == null;
    display.setSections(manager.getScoreboard().getTotalScoreboard());
  }

  /**
   * Will display  the scores for a minigame. Will only work if the player is in the hub
   *
   * @param game The minigame to show the scores of
   */
  public void showMinigameScore(Minigame game) {
    assert currentMinigame == null;
    display.setSections(game.getScoreboard());
  }

  /**
   * Teleports the player to a given location
   *
   * @param l The location to teleport to
   */
  public void teleport(Location l) {
    player.teleport(l);
  }

  /**
   * Updates the contents of the scoreboard
   * This will be called whenever a minigame calles MinigameAPI::update()
   */
  public void updateScoreboard() {
    display.update();
  }

  /**
   * Gets the spigot player
   *
   * @return The Spigot player that this object represents
   */
  public Player getPlayer() {
    return player;
  }
}
