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
    assert currentMinigame == null;
    currentMinigame = minigame;
    player.sendMessage(ChatColor.GREEN + "Joining " + currentMinigame.getName());
    minigame.addPlayer(player);
  }

  /***
   * Called when the player tries to leave whatever minigame they are in
   * @return true if they were in a minigame, false if otherwise
   */
  public boolean leaveCurrentMinigame() {
    if (currentMinigame == null) {
      return false;
    } else {
      currentMinigame.removePlayer(player);
      currentMinigame = null;
      return true;
    }
  }
}
