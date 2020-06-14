package net.cubiness.colachampionship;

import net.cubiness.colachampionship.minigame.MinigameAPI;
import net.cubiness.colachampionship.minigame.MinigameManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class ColaCore extends JavaPlugin implements Listener, CommandExecutor {

  private ScoreboardDisplay display;
  private ScoreManager scoreManager;
  private MinigameManager minigames;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    display = new ScoreboardDisplay();
    scoreManager = new ScoreManager(this, display);
    minigames = new MinigameManager(scoreManager);
  }

  public MinigameAPI getAPI() {
    return minigames.getAPI();
  }

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (label.equals("minigame")) {
      if (!sender.hasPermission("cc.minigame")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length == 0) {
          return false;
        } else if (args[0].equals("start") && args.length == 2) {
          if (minigames.running()) {
            sender.sendMessage(ChatColor.RED + "Theres is already a minigame in progress!");
          } else if (minigames.hasMinigame(args[1])) {
            minigames.start(args[1]);
          } else {
            sender.sendMessage(ChatColor.RED + "Minigame " + args[1] + " has not been registered!");
          }
        } else if (args[0].equals("stop") && args.length == 1) {
          sender.sendMessage(ChatColor.YELLOW + "Stopping minigame");
        } else {
          return false;
        }
      }
    } else if (label.equals("score")) {
      if (!sender.hasPermission("cc.score")) {
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
            sender.sendMessage(ChatColor.RED + "`" + args[2] + "` is not a valid number");
            return true;
          }
          int score = Integer.parseInt(args[2]);
          scoreManager.addTotalScore(p.getName(), score);
        } else if (args[0].equals("remove") && args.length == 3) {
          Player p = Bukkit.getPlayer(args[1]);
          if (p == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return true;
          }
          if (!StringUtils.isNumeric(args[2])) {
            sender.sendMessage(ChatColor.RED + "`" + args[2] + "` is not a valid number");
            return true;
          }
          int score = Integer.parseInt(args[2]);
          scoreManager.addTotalScore(p.getName(), -score);
        } else if (args[0].equals("set") && args.length == 3) {
          Player p = Bukkit.getPlayer(args[1]);
          if (p == null) {
            sender.sendMessage(ChatColor.RED + "Player not found");
            return true;
          }
          if (!StringUtils.isNumeric(args[2])) {
            sender.sendMessage(ChatColor.RED + "`" + args[2] + "` is not a valid number");
            return true;
          }
          int score = Integer.parseInt(args[2]);
          scoreManager.setTotalScore(p.getName(), score);
        } else {
          return false;
        }
      }
    } else if (label.equals("showscore")) {
      if (!sender.hasPermission("cc.score")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length == 1) {
          if (args[0].equals("total")) {
            scoreManager.showTotalScores();
          } else if (args[0].equals("minigame")) {
            scoreManager.showMinigameScores();
          } else {
            return false;
          }
        } else {
          return false;
        }
      }
    } else if (label.equals("updatescoreboard")) {
      if (!sender.hasPermission("cc.updatescorebord")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length == 0) {
          display.showScoreboard((Player) sender);
        } else {
          return false;
        }
      }
    } else if (label.equals("join")) {
      if (!sender.hasPermission("cc.join")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length == 1) {
          if (sender instanceof Player) {
            if (minigames.running()) {
              sender.sendMessage(ChatColor.RED + "There is already a minigame in progress!");
            } else if (minigames.hasMinigame(args[0])) {
              minigames.addPlayer((Player) sender, args[0]);
            } else {
              sender
                  .sendMessage(ChatColor.RED + "Minigame " + args[0] + " has not been registered!");
            }
          } else {
            sender.sendMessage(ChatColor.RED + "You must be a player to run this command!");
          }
        } else {
          return false;
        }
      }
    } else {
      return false;
    }
    return true;
  }

  @Override
  public void onDisable() {
    minigames.reset();
  }

  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent e) {
    display.showScoreboard(e.getPlayer());
  }
}
