package online.nasgar.microbattles.adapt.v1_8_R3;

import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public interface Packets_v1_8_R3 {
  static void sendPackets(Player player,
                          Packet<?>... packets) {
    PlayerConnection playerConnection = ((CraftPlayer) player)
      .getHandle()
      .playerConnection;

    for (Packet<?> packet : packets) {
      playerConnection.sendPacket(packet);
    }
  }
}
