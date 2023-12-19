package online.nasgar.microbattles.setup.bind;

import me.yushust.inject.AbstractModule;
import me.yushust.message.MessageHandler;
import online.nasgar.microbattles.api.adapt.MaterialProvider;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import online.nasgar.microbattles.api.adapt.TitleSender;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import online.nasgar.microbattles.commons.adapt.AdaptionFactory;
import online.nasgar.microbattles.commons.bind.ModelModule;
import online.nasgar.microbattles.commons.item.ClickableItemApplierImpl;
import online.nasgar.microbattles.commons.message.MessageHandlerProvider;
import online.nasgar.microbattles.setup.MicroBattlesSetupPlugin;
import online.nasgar.microbattles.setup.bind.bukkit.CommandModule;
import online.nasgar.microbattles.setup.bind.bukkit.ListenerModule;
import online.nasgar.microbattles.setup.bind.loader.LoaderModule;
import org.bukkit.plugin.java.JavaPlugin;

public class BinderModule extends AbstractModule {
  private final MicroBattlesSetupPlugin plugin;

  public BinderModule(MicroBattlesSetupPlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    bind(JavaPlugin.class).toInstance(plugin);
    bind(MicroBattlesSetupPlugin.class).toInstance(plugin);

    bind(ClickableItemApplier.class).to(ClickableItemApplierImpl.class).singleton();

    bind(MessageHandler.class).toProvider(MessageHandlerProvider.class).singleton();

    bind(TitleSender.class).toInstance(AdaptionFactory.createTitleSender());

    bind(MaterialProvider.class).toInstance(AdaptionFactory.createMaterialProvider());

    bind(NbtHandler.class).toInstance(AdaptionFactory.createNbtHandler());

    install(
      new CommandModule(),
      new ListenerModule(),
      new LoaderModule(),
      new ModelModule(plugin)
    );
  }
}
