package net.cubiness.colachampionship.utils;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.cubiness.colachampionship.ColaCore;
import net.cubiness.colachampionship.minigame.MinigamePlayer;

public class HologramManager {
  private final ColaCore core;
  private final PacketUtils packetUtils;
  private final Map<MinigamePlayer, Collection<Hologram>> holograms = new HashMap<>();

  public HologramManager(ColaCore core) {
    this.core = core;
    this.packetUtils = core.getPacketUtils();
  }

  /**
   * Adds a holograms for the specified player at the given location
   *
   * @param player The player to show this hologram to
   * @param location The location of the hologram
   * @param name The name of the armor stand
   * @return The Hologram that was created for that player
   */
  public Hologram add(MinigamePlayer player, Location location, String name) {
    int id = core.getPacketUtils().spawnArmorStand(player.getPlayer(), location);
    Hologram h = new Hologram(id, player, this);
    h.setName(name);
    getHolograms(player).add(h);
    return h;
  }

  /**
   * Gets all of the holograms that the given player can see
   *
   * @param player The player
   * @return A collection of holograms that the player can see
   */
  public Collection<Hologram> getHolograms(MinigamePlayer player) {
    if (!holograms.containsKey(player)) {
      holograms.put(player, new HashSet<>());
    }
    return holograms.get(player);
  }

  /**
   * Removes the player from the list of players. This also removes all holograms that the player could see
   *
   * @param player The player to remove the holograms from
   */
  public void removePlayer(MinigamePlayer player) {
    if (holograms.containsKey(player)) {
      holograms.remove(player);
    }
  }

  private PacketUtils getPacketUtils() {
    return packetUtils;
  }

  public static class Hologram {
    private final int id;
    private final HologramManager manager;
    private final Player player;
    private String name;

    private Hologram(int id, MinigamePlayer player, HologramManager manager) {
      this.id = id;
      this.manager = manager;
      this.player = player.getPlayer();
    }

    /**
     * Changes the title of the hologram. This will send packets to the player that is ascociated with this hologram.
     * This will fail if the player is not online.
     *
     * @param newName The new name of the armor stand
     */
    public void setName(String newName) {
      name = newName;
      manager.getPacketUtils().setMarkerArmorstand(player, id, name);
    }
  }
}
