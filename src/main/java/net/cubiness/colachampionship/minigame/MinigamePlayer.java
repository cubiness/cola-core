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
      throw new RuntimeException(
          "Player is already in " + currentMinigame.getName() + ", cannnot join " + minigame
              .getName());
    }
  }

  public void leaveCurrentMinigame() {
    currentMinigame.removePlayer(player);
    currentMinigame = null;
  }
}
