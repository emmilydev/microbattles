package online.nasgar.microbattles.lobby.loader;

import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.lobby.kit.LobbyKit;

import javax.inject.Inject;

public class KitLoader implements Loader {
  private final CachedRemoteModelService<Kit> kitService;

  @Inject
  public KitLoader(CachedRemoteModelService<Kit> kitService) {
    this.kitService = kitService;
  }

  @Override
  public void load() {
    kitService
      .getOrFind("lobby")
      .whenComplete((kit, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          return;
        }

        if (kit == null) {
          kit = new LobbyKit();
          kitService.saveSync(kit);
        }
      });
  }
}
