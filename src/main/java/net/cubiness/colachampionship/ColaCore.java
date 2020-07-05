package net.cubiness.colachampionship;

import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;

import net.cubiness.colachampionship.commands.TabCompleteManager;
import net.cubiness.colachampionship.minigame.MinigameAPI;
import net.cubiness.colachampionship.minigame.MinigameManager;
import net.cubiness.colachampionship.scoreboard.ScoreManager;
import net.cubiness.colachampionship.utils.PacketUtils;

public class ColaCore extends JavaPlugin implements Listener, CommandExecutor {

  private ScoreManager scoreManager;
  private MinigameManager minigames;
  private TabCompleteManager tabComplete;
  private PacketUtils packetUtils;

  @Override
  public void onEnable() {
    getServer().getPluginManager().registerEvents(this, this);
    scoreManager = new ScoreManager(this);
    minigames = new MinigameManager(this, scoreManager);
    tabComplete = new TabCompleteManager();
    getCommand("minigame").setTabCompleter(tabComplete);
    getCommand("score").setTabCompleter(tabComplete);
    getCommand("showscore").setTabCompleter(tabComplete);
    getCommand("joinall").setTabCompleter(tabComplete);
    getCommand("updatescoreboard").setTabCompleter(tabComplete);
    tabComplete.addCompletion("minigame",
        Collections.emptyList(),
        Arrays.asList("start", "stop"));
    tabComplete.addCompletion("score",
        Collections.emptyList(),
        Arrays.asList("set", "add", "remove"));
    tabComplete.addCompletion("showscore",
        Collections.emptyList(),
        Arrays.asList("total", "minigame"));
    updateTabComplete(false, true);
    packetUtils = new PacketUtils(ProtocolLibrary.getProtocolManager());
  }

  public void updateTabComplete(boolean newMinigames, boolean newPlayers) {
    if (newMinigames) {
      tabComplete.addCompletion("minigame",
          Arrays.asList("start"),
          minigames.getMinigameList());
      tabComplete.addCompletion("joinall",
          Collections.emptyList(),
          minigames.getMinigameList());
    }
    if (newPlayers) {
      tabComplete.addCompletion("score",
          Arrays.asList("set"),
          Bukkit.getOnlinePlayers().stream()
              .map(HumanEntity::getName)
              .collect(Collectors.toList()));
      tabComplete.addCompletion("score",
          Arrays.asList("add"),
          Bukkit.getOnlinePlayers().stream()
              .map(HumanEntity::getName)
              .collect(Collectors.toList()));
      tabComplete.addCompletion("score",
          Arrays.asList("remove"),
          Bukkit.getOnlinePlayers().stream()
              .map(HumanEntity::getName)
              .collect(Collectors.toList()));
    }
  }

  public MinigameAPI getAPI() {
    return minigames.getAPI();
  }

  public TabCompleteManager getTabComplete() {
    return tabComplete;
  }

  public PacketUtils getPacketUtils() {
    return packetUtils;
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
          if (minigames.running()) {
            sender.sendMessage(ChatColor.YELLOW + "Stopping minigame");
            minigames.stop();
          } else {
            sender.sendMessage(ChatColor.RED + "There is not a minigame running!");
          }
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
          scoreManager.addTotalScore(minigames.getPlayer(p), score);
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
          scoreManager.addTotalScore(minigames.getPlayer(p), -score);
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
          scoreManager.setTotalScore(minigames.getPlayer(p), score);
        } else {
          return false;
        }
      }
    // } else if (label.equals("showscore")) {
    //   if (!sender.hasPermission("cc.score")) {
    //     sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
    //   } else {
    //     if (!(sender instanceof Player)) {
    //       sender.sendMessage(ChatColor.RED + "Only players can run this command!");
    //     }
    //     if (args.length == 1) {
    //       Player p = (Player) sender;
    //       if (args[0].equals("total")) {
    //         minigames.get(p).showTotalScores();
    //       } else if (args[0].equals("minigame")) {
    //         minigames.get(p).showMinigameScores();
    //       } else {
    //         return false;
    //       }
    //     } else {
    //       return false;
    //     }
    //   }
    } else if (label.equals("updatescoreboard")) {
      if (!sender.hasPermission("cc.updatescorebord")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length == 0) {
          Bukkit.getOnlinePlayers().forEach(p -> {
            //display.showScoreboard(p);
          });
        } else {
          return false;
        }
      }
    } else if (label.equals("joinall")) {
      if (!sender.hasPermission("cc.joinall")) {
        sender.sendMessage(ChatColor.RED + "You do not have permission to run this command!");
      } else {
        if (args.length == 1) {
          if (minigames.running()) {
            sender.sendMessage(ChatColor.RED + "There is already a minigame in progress!");
          } else if (minigames.hasMinigame(args[0])) {
            minigames.addAll(args[0]);
          } else {
            sender
                .sendMessage(ChatColor.RED + "Minigame " + args[0] + " has not been registered!");
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
    updateTabComplete(false, true);
  }

  @EventHandler
  public void onPlayerLeave(PlayerQuitEvent e) {
    Bukkit.getScheduler().runTaskLater(this, () -> {
      updateTabComplete(false, true);
    }, 1);
  }
}
