package online.nasgar.microbattles.setup.menu.kit.prompt;

import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.setup.MicroBattlesSetupPlugin;
import online.nasgar.microbattles.setup.menu.kit.KitSettingsMenu;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class KitSettingPromptFactory {
  private final MicroBattlesSetupPlugin plugin;
  private final MessageHandler messageHandler;
  private final KitSettingsMenu kitSettingsMenu;

  @Inject
  public KitSettingPromptFactory(MicroBattlesSetupPlugin plugin,
                                 MessageHandler messageHandler,
                                 KitSettingsMenu kitSettingsMenu) {
    this.plugin = plugin;
    this.messageHandler = messageHandler;
    this.kitSettingsMenu = kitSettingsMenu;
  }

  public Conversation create(Player player,
                             Kit kit,
                             String field,
                             Map<String, String> session,
                             Function<Player, String> promptConsumer,
                             BiConsumer<Player, String> acceptAction,
                             BiConsumer<Player, ConversationAbandonedEvent> cancelAction) {
    return new ConversationFactory(plugin)
      .withEscapeSequence("cancel")
      .withFirstPrompt(new StringPrompt() {
        @Override
        public String getPromptText(ConversationContext conversationContext) {
          return promptConsumer.apply(player);
        }

        @Override
        public Prompt acceptInput(ConversationContext conversationContext,
                                  String input) {
          session.put(field, input);

          acceptAction.accept(player, input);

          return Prompt.END_OF_CONVERSATION;
        }
      })
      .addConversationAbandonedListener(event -> {
        cancelAction.accept(player, event);

        if (!event.gracefulExit()) {
          session.remove(field);
        }

        kitSettingsMenu.open(player, kit);
      })
      .buildConversation(player);
  }
}
