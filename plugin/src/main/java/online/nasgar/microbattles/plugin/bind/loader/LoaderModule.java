package online.nasgar.microbattles.plugin.bind.loader;

import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.commons.loader.CommandLoader;
import online.nasgar.microbattles.commons.loader.ListenerLoader;
import online.nasgar.microbattles.plugin.loader.GameLoader;
import online.nasgar.microbattles.plugin.loader.KitLoader;
import online.nasgar.microbattles.plugin.loader.UserLoader;

public class LoaderModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Loader.class)
      .asSet()
      .to(ListenerLoader.class)
      .to(CommandLoader.class)
      .to(GameLoader.class)
      .to(UserLoader.class)
      .to(KitLoader.class);
  }
}
