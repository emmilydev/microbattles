package online.nasgar.microbattles.commons.message;

import com.cryptomorin.xseries.XSound;
import me.yushust.message.MessageHandler;
import me.yushust.message.bukkit.BukkitMessageAdapt;
import me.yushust.message.language.Linguist;
import me.yushust.message.send.MessageSender;
import me.yushust.message.source.MessageSourceDecorator;
import online.nasgar.microbattles.api.adapt.TitleSender;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;

public class MessageHandlerProvider implements Provider<MessageHandler> {
  private final JavaPlugin plugin;
  private final MicroBattlesEngine engine;
  private final TitleSender titleSender;

  @Inject
  public MessageHandlerProvider(JavaPlugin plugin,
                                MicroBattlesEngine engine, TitleSender titleSender) {
    this.plugin = plugin;
    this.engine = engine;
    this.titleSender = titleSender;
  }

  @Override
  public MessageHandler get() {
    return MessageHandler.of(
      MessageSourceDecorator
        .decorate(BukkitMessageAdapt.newYamlSource(plugin, new File(plugin.getDataFolder() + "/lang/")))
        .addFallbackLanguage(engine.getDefaultLanguage())
        .get(),
      configurationHandle -> {
        Linguist<Player> playerLinguist = new PlayerLinguist();
        MessageSender<Player> playerMessageSender = (player, mode, message) -> {
          XSound soundToPlay = null;

          switch (mode.toLowerCase()) {
            case MessageMode.INFO: {
              soundToPlay = XSound.UI_BUTTON_CLICK;

              break;
            }
            case MessageMode.SUCCESS: {
              soundToPlay = XSound.ENTITY_EXPERIENCE_ORB_PICKUP;

              break;
            }
            case MessageMode.ERROR: {
              soundToPlay = XSound.BLOCK_NOTE_BLOCK_BASS;

              break;
            }
            case MessageMode.TITLE: {
              titleSender.sendTitle(
                player,
                message,
                1,
                2,
                1
              );

              break;
            }
            case MessageMode.SUBTITLE: {
              titleSender.sendSubtitle(
                player,
                message,
                1,
                2,
                1
              );

              break;
            }
            case MessageMode.ACTION_BAR: {
              try {
                titleSender.sendActionbar(player, message);
              } catch (Exception e) {
                Bukkit.getScheduler().runTask(plugin, e::printStackTrace);
              }
              break;
            }
          }

          if (soundToPlay != null) {
            SoundPlayer.playNormal(player, soundToPlay);
          }

          player.sendMessage(message);
        };

        configurationHandle
          .specify(Player.class)
          .setLinguist(playerLinguist)
          .setMessageSender(playerMessageSender);
        configurationHandle
          .specify(User.class)
          .setLinguist(user -> playerLinguist.getLanguage(user.player()))
          .setMessageSender((user, mode, message) -> playerMessageSender.send(user.player(), mode, message));
        configurationHandle.addInterceptor(s -> ChatColor.translateAlternateColorCodes('&', s));
      }
    );
  }

}
