package net.cubiness.colachampionship.minigame;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MinigamePlayer {

  private final Player player;
  private Minigame currentMinigame = null;

  public MinigamePlayer(Player p) {
    player = p;
  }

  public Minigame getCurrentMinigame() {
    return currentMinigame;
  }

  public void setCurrentMinigame(Minigame minigame) {
    if (currentMinigame == null) {
      currentMinigame = minigame;
      player.sendMessage(ChatColor.GREEN + "Joining " + currentMinigame.getName());
      minigame.addPlayer(player);
    } else {
      throw new RuntimeException(
          "Player is already in " + currentMinigame.getName() + ", cannnot join " + minigame
              .getName());
    }
  }

  /***
   * Called when the player tries to leave whatever minigame they are in
   * @return true if they were in a minigame, false if otherwise
   */
  public boolean leaveCurrentMinigame() {
    if (currentMinigame == null) {
      return false;
    } else {
      player.sendMessage(ChatColor.GREEN + "Leaving " + currentMinigame.getName());
      currentMinigame.removePlayer(player);
      currentMinigame = null;
      return true;
    }
  }
}
