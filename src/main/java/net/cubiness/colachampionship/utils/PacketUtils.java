package net.cubiness.colachampionship.utils;

import java.lang.reflect.InvocationTargetException;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;

public class PacketUtils {

  private final ProtocolManager manager;

  public PacketUtils(ProtocolManager manager) {
    this.manager = manager;
  }

  /**
   * Spawns an armor stand at the given location, but just for the one player
   *
   * @param p The player that will see the armor stand
   * @param l The location of the armor stand
   */
  public int spawnArmorStand(Player p, Location l) {
    PacketContainer packet = new PacketContainer(Server.SPAWN_ENTITY_LIVING);

    int id = (int) (Math.random() * Integer.MAX_VALUE);

    packet.getModifier().writeDefaults();
    packet.getIntegers().write(0, id); // id of entity
    packet.getIntegers().write(1, 1); // type id of armor stand
    packet.getDoubles().write(0, l.getX());
    packet.getDoubles().write(1, l.getY());
    packet.getDoubles().write(2, l.getZ());

    try {
      manager.sendServerPacket(p, packet);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
    return id;
  }
}
