package online.nasgar.microbattles.plugin.bind;

import me.yushust.inject.AbstractModule;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.MicroBattlesAPI;
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
import online.nasgar.microbattles.commons.message.MessageHandlerProvider;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import online.nasgar.microbattles.plugin.api.MicroBattlesAPIImpl;
import online.nasgar.microbattles.plugin.bind.bukkit.CommandModule;
import online.nasgar.microbattles.plugin.bind.bukkit.ListenerModule;
import online.nasgar.microbattles.plugin.bind.loader.LoaderModule;
import online.nasgar.microbattles.commons.match.SimpleMatchManager;
import org.bukkit.plugin.java.JavaPlugin;
import team.unnamed.scoreboard.BoardRegistry;
import team.unnamed.scoreboard.platform.BoardHandlerFactory;

public class BinderModule extends AbstractModule {
  private final MicroBattlesPlugin plugin;

  public BinderModule(MicroBattlesPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(JavaPlugin.class).toInstance(plugin);
    bind(MicroBattlesPlugin.class).toInstance(plugin);
    install(new ModelModule(plugin));

    bind(MessageHandler.class).toProvider(MessageHandlerProvider.class).singleton();

    bind(MatchManager.class).to(SimpleMatchManager.class).singleton();

    bind(KitApplier.class).to(KitApplierImpl.class).singleton();

    bind(ClickableItemApplier.class).to(ClickableItemApplierImpl.class).singleton();

    bind(MicroBattlesAPI.class).to(MicroBattlesAPIImpl.class).singleton();

    bind(BoardRegistry.class).toInstance(new BoardRegistry(BoardHandlerFactory.createHandler()));

    bind(TitleSender.class).toInstance(AdaptionFactory.createTitleSender());

    bind(MaterialProvider.class).toInstance(AdaptionFactory.createMaterialProvider());

    bind(NbtHandler.class).toInstance(AdaptionFactory.createNbtHandler());

    install(
      new ListenerModule(),
      new CommandModule(),
      new LoaderModule()
    );
  }
}
