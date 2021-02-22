package net.cubiness.colachampionship.scoreboard.section;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.minigame.MinigameManager;

public class TimerSection extends ScoreboardSection {

  private final Plugin plugin;
  private final MinigameManager manager;
  public int secondsLeft;
  private int startTime;
  private BukkitTask timerCountID;
  private Runnable onFinishTask;

  public TimerSection(Plugin plugin, String title, int row, int startSeconds) {
    super(2, title, row);
    this.plugin = plugin;
    secondsLeft = startSeconds;
    startTime = startSeconds;
    manager = ((ColaCore) Bukkit.getPluginManager().getPlugin("ColaCore")).getMinigames();
  }

  public void onFinish(Runnable task) {
    onFinishTask = task;
  }

  public void start() {
    timerCountID = Bukkit.getScheduler().runTaskTimer(plugin, this::decreaseTime, 20, 20);
  }

  public void stop() {
    timerCountID.cancel();
  }

  private void decreaseTime() {
    secondsLeft -= 1;
    manager.updateScoreboard();
    if (secondsLeft <= 0) {
      stop();
      onFinishTask.run();
    }
  }

  public int getTimeLeft() {
    return secondsLeft;
  }

  public void setTimeLeft(int seconds) {
    secondsLeft = seconds;
  }

  public void reset() {
    secondsLeft = startTime;
  }

  @Override
  protected void updateRows() {
    setRow(0, String.format("%02d:%02d", (secondsLeft / 60), (secondsLeft % 60)));
  }

  @Override
  protected boolean needsUpdate() {
    return true;
  }

}
