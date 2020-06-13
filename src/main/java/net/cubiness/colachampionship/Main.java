package net.cubiness.colachampionship;

import net.cubiness.colachampionship.minigame.Minigame;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

  private ScoreboardDisplay display;
  private ScoreManager scoreManager;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    display = new ScoreboardDisplay();
    scoreManager = new ScoreManager(display);
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
        if (args.length == 0) {
          return false;
        }
        if (args[0].equals("add") && args.length == 3) {
          Player p = Bukkit.getPlayer(args[1]);
          if (p == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return true;
          }
          if (!StringUtils.isNumeric(args[2])) {
            sender.sendMessage(ChatColor.RED + "Value is not a valid number");
            return true;
          }
          int score = Integer.parseInt(args[2]);
          sender.sendMessage("Adding " + score + " to " + p.getName() + "'s score");
          scoreManager.addTotalScore(p, score);
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  public void addMinigame(Minigame minigame) {

  }
}
