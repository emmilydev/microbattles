package online.nasgar.microbattles.setup.bind.loader;

import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.commons.loader.CommandLoader;
import online.nasgar.microbattles.commons.loader.ListenerLoader;
import online.nasgar.microbattles.setup.loader.ItemLoader;

public class LoaderModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Loader.class)
      .asSet()
      .to(CommandLoader.class)
      .to(ListenerLoader.class)
      .to(ItemLoader.class);
  }
}
