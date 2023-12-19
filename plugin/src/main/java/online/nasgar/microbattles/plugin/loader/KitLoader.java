package online.nasgar.microbattles.plugin.loader;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.plugin.MicroBattlesPlugin;
import online.nasgar.microbattles.plugin.kit.ArcherKit;
import online.nasgar.microbattles.plugin.kit.FighterKit;
import online.nasgar.microbattles.plugin.kit.WorkerKit;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Inject;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class KitLoader implements Loader {
  private final CachedRemoteModelService<Kit> kitService;

  @Inject
  public KitLoader(CachedRemoteModelService<Kit> kitService) {
    this.kitService = kitService;
  }

  @Override
  public void load() {
    CompletableFuture.runAsync(() -> {
      createIfAbsent("archer", ArcherKit::new);
      createIfAbsent("fighter", FighterKit::new);
      createIfAbsent("worker", WorkerKit::new);
    });
  }

  private void createIfAbsent(String id,
                              Supplier<Kit> supplier) {
    Kit kit = kitService.getOrFindSync(id);

    if (kit == null) {
      kitService.saveInCache(supplier.get());
    }
  }

}
