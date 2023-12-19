package online.nasgar.microbattles.adapt.v1_8_R3;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import online.nasgar.microbattles.api.adapt.TitleSender;
import org.bukkit.entity.Player;

public class TitleSender_v1_8_R3 implements TitleSender {
  private static IChatBaseComponent createComponent(String text) {
    return IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + text + "\"}");
  }

  @Override
  public void sendTitle(Player player,
                        String title,
                        int fadeIn,
                        int stay,
                        int fadeOut) {
    Packets_v1_8_R3.sendPackets(
      player,
      new PacketPlayOutTitle(
        PacketPlayOutTitle.EnumTitleAction.TITLE,
        createComponent(title),
        fadeIn,
        stay,
        fadeOut
      )
    );
  }

  @Override
  public void sendSubtitle(Player player,
                           String subtitle,
                           int fadeIn,
                           int stay,
                           int fadeOut) {
    Packets_v1_8_R3.sendPackets(
      player,
      new PacketPlayOutTitle(
        PacketPlayOutTitle.EnumTitleAction.SUBTITLE,
        createComponent(subtitle),
        fadeIn,
        stay,
        fadeOut
      )
    );
  }

  @Override
  public void sendBoth(Player player,
                       String title,
                       String subtitle,
                       int fadeIn,
                       int stay,
                       int fadeOut) {
    sendTitle(
      player,
      title,
      fadeIn,
      stay,
      fadeOut
    );
    sendSubtitle(
      player,
      subtitle,
      fadeIn,
      stay,
      fadeOut
    );
  }

  @Override
  public void sendActionbar(Player player,
                            String value) {
    Packets_v1_8_R3.sendPackets(
      player,
      new PacketPlayOutChat(
        createComponent(value),
        (byte) 2
      )
    );
  }
}
