package net.cubiness.colachampionship.minigame;

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
      minigame.addPlayer(player);
    } else {
      player.sendMessage(
          "Cannot join " + minigame.getName() + ", as you are already in " + currentMinigame
              .getName());
    }
  }

  public void leaveCurrentMinigame() {
    currentMinigame.removePlayer(player);
    currentMinigame = null;
  }
}
