package net.cubiness.colachampionship.scoreboard.section;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.minigame.MinigameAPI;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

public class TimerSection extends ScoreboardSection {

  private final Plugin plugin;
  private final MinigameAPI api;
  public int secondsLeft;
  private BukkitTask timerCountID;

  public TimerSection(Plugin plugin, String title, int row, int startSeconds) {
    super(2, title, row);
    this.plugin = plugin;
    secondsLeft = startSeconds;
    api = ((ColaCore) Bukkit.getPluginManager().getPlugin("ColaCore")).getAPI();
  }

  public void start() {
    timerCountID = Bukkit.getScheduler().runTaskTimer(plugin, this::decreaseTime, 20, 20);
  }

  public void stop() {
    timerCountID.cancel();
  }

  private void decreaseTime() {
    secondsLeft -= 1;
    api.updateScoreboard();
  }

  public int getTimeLeft() {
    return secondsLeft;
  }

  public void setTimeLeft(int seconds) {
    secondsLeft = seconds;
  }

  @Override
  protected void updateRows() {
    setRow(0, (secondsLeft / 60) + ":" + (secondsLeft % 60));
  }

  @Override
  protected boolean needsUpdate() {
    return true;
  }

}
