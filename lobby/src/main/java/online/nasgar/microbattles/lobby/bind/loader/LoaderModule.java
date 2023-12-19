package online.nasgar.microbattles.lobby.bind.loader;

import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.commons.loader.CommandLoader;
import online.nasgar.microbattles.commons.loader.ListenerLoader;
import online.nasgar.microbattles.lobby.loader.KitLoader;

public class LoaderModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Loader.class)
      .asSet()
      .to(ListenerLoader.class)
      .to(CommandLoader.class)
      .to(KitLoader.class);
  }
}
