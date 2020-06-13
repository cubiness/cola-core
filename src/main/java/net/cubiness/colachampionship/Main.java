package net.cubiness.colachampionship;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

  private ScoreboardDisplay scoreboardDisplay;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    scoreboardDisplay = new ScoreboardDisplay();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (label.equals("minigame")) {
      if (!sender.hasPermission("cc.minigame")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        sender.sendMessage(ChatColor.YELLOW + "Setting minigame");
      }
    } else if (label.equals("score")) {
      if (!sender.hasPermission("cc.minigame")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length != 2) {
          return false;
        }
        sender.sendMessage(ChatColor.YELLOW + "Setting score");
        scoreboardDisplay.setRow(Integer.parseInt(args[0]), args[1]);
      }
    } else {
      return false;
    }
    return true;
  }
}
