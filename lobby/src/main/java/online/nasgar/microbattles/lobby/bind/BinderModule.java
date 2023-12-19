package online.nasgar.microbattles.lobby.bind;

import me.yushust.inject.AbstractModule;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.adapt.MaterialProvider;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import online.nasgar.microbattles.api.adapt.TitleSender;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.model.kit.KitApplier;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import online.nasgar.microbattles.commons.adapt.AdaptionFactory;
import online.nasgar.microbattles.commons.bind.ModelModule;
import online.nasgar.microbattles.commons.item.ClickableItemApplierImpl;
import online.nasgar.microbattles.commons.kit.KitApplierImpl;
import online.nasgar.microbattles.commons.match.SimpleMatchManager;
import online.nasgar.microbattles.commons.message.MessageHandlerProvider;
import online.nasgar.microbattles.lobby.MicroBattlesLobbyPlugin;
import online.nasgar.microbattles.lobby.bind.bukkit.CommandModule;
import online.nasgar.microbattles.lobby.bind.bukkit.ListenerModule;
import online.nasgar.microbattles.lobby.bind.loader.LoaderModule;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.scoreboard.platform.BoardHandlerFactory;
import team.unnamed.scoreboard.BoardRegistry;

public class BinderModule extends AbstractModule {
  private final MicroBattlesLobbyPlugin plugin;

  public BinderModule(MicroBattlesLobbyPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(JavaPlugin.class).toInstance(plugin);
    bind(MicroBattlesLobbyPlugin.class).toInstance(plugin);

    bind(ClickableItemApplier.class).to(ClickableItemApplierImpl.class).singleton();
    bind(MessageHandler.class).toProvider(MessageHandlerProvider.class).singleton();

    bind(TitleSender.class).toInstance(AdaptionFactory.createTitleSender());

    bind(MaterialProvider.class).toInstance(AdaptionFactory.createMaterialProvider());

    bind(NbtHandler.class).toInstance(AdaptionFactory.createNbtHandler());

    bind(BoardRegistry.class).toInstance(new BoardRegistry(BoardHandlerFactory.createHandler()));

    bind(KitApplier.class).to(KitApplierImpl.class).singleton();

    bind(MatchManager.class).to(SimpleMatchManager.class).singleton();

    install(
      new CommandModule(),
      new ListenerModule(),
      new LoaderModule(),
      new ModelModule(plugin)
    );
  }
}
