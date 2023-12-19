package online.nasgar.microbattles.setup.bind.bukkit;

import me.yushust.inject.AbstractModule;
import online.nasgar.microbattles.setup.listener.ArenaBarrierEditListener;
import online.nasgar.microbattles.setup.listener.ArenaBarrierEditSessionEndListener;
import online.nasgar.microbattles.setup.listener.item.BarrierEditorItemListener;
import org.bukkit.event.Listener;

public class ListenerModule extends AbstractModule {
  @Override
  protected void configure() {
    multibind(Listener.class)
      .asSet()
      .to(BarrierEditorItemListener.class);
      //.to(ArenaBarrierEditSessionEndListener.class);
  }
}
