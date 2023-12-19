package online.nasgar.microbattles.adapt.v1_18_R2;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import online.nasgar.microbattles.api.adapt.TitleSender;
import org.bukkit.entity.Player;

public class TitleSender_v1_18_R2 implements TitleSender {
  @Override
  public void sendTitle(Player player,
                        String title,
                        int fadeIn,
                        int stay,
                        int fadeOut) {
    player.sendTitle(
      title,
      null,
      fadeIn,
      stay,
      fadeOut
    );
  }

  @Override
  public void sendSubtitle(Player player,
                           String subtitle,
                           int fadeIn,
                           int stay,
                           int fadeOut) {

  }

  @Override
  public void sendBoth(Player player,
                       String title,
                       String subtitle,
                       int fadeIn,
                       int stay,
                       int fadeOut) {
    player.sendTitle(
      title,
      subtitle,
      fadeIn,
      stay,
      fadeOut
    );
  }

  @Override
  public void sendActionbar(Player player,
                            String value) {
    player.spigot().sendMessage(
      ChatMessageType.ACTION_BAR,
      new TextComponent(value)
    );
  }
}
