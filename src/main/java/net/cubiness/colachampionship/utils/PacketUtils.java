package net.cubiness.colachampionship.utils;

import com.comphenix.protocol.PacketType.Play.Server;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedDataWatcher;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class PacketUtils {

  private final ProtocolManager manager;

  public PacketUtils(ProtocolManager manager) {
    this.manager = manager;
  }

  /**
   * Sets the custom name of an entity for one player
   * @param p The player the name should be displayed to
   * @param e The entity to change the name of
   * @param name the new name
   */
  public void setCustomName(Player p, Entity e, String name) {
    PacketContainer packet = new PacketContainer(Server.ENTITY_METADATA);
    packet.getIntegers().write(0, e.getEntityId());

    WrappedDataWatcher metadata = WrappedDataWatcher.getEntityWatcher(e);
    metadata.setObject(2, Optional.of(WrappedChatComponent.fromText(name).getHandle()));
    packet.getWatchableCollectionModifier().write(0, metadata.getWatchableObjects());
    try {
      manager.sendServerPacket(p, packet);
    } catch (InvocationTargetException err) {
      throw new RuntimeException("Cannot send packet " + packet, err);
    }
  }

}
