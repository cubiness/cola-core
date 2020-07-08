package net.cubiness.colachampionship.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.Registry;
import com.comphenix.protocol.wrappers.WrappedDataWatcher.WrappedDataWatcherObject;

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
   * @return The entity id of the new armor stand
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

  /**
   * Sends an armor stand update packet to ther given player
   * This should be used if you want the armor stand to be an invisible marker armor stand
   *
   * @param player The player to send this packet to
   * @param id The entity id of the armor stand
   * @param name The new name to give this armor stand
   */
  public void setMarkerArmorstand(Player player, int id, String name) {
    PacketContainer packet = new PacketContainer(Server.ENTITY_METADATA);
    packet.getModifier().writeDefaults();

    WrappedDataWatcher metadata = new WrappedDataWatcher();
    metadata.setObject(new WrappedDataWatcherObject(0, Registry.get(Byte.class)),
        (byte) 0b00100000);
    metadata.setObject(new WrappedDataWatcherObject(2, Registry.getChatComponentSerializer(true)),
        Optional.of(WrappedChatComponent.fromText(name).getHandle()));
    metadata.setObject(new WrappedDataWatcherObject(3, Registry.get(Boolean.class)),
        true);
    metadata.setObject(new WrappedDataWatcherObject(14, Registry.get(Byte.class)),
        (byte) 0b00010000);

    packet.getIntegers().write(0, id); // entity id
    packet.getWatchableCollectionModifier().write(0, metadata.getWatchableObjects()); // the metadata

    try {
      manager.sendServerPacket(player, packet);
    } catch (InvocationTargetException e) {
      e.printStackTrace();
    }
  }
}
