package online.nasgar.microbattles.plugin.api;

import me.yushust.inject.Injector;
import me.yushust.inject.key.TypeReference;
import net.cosmogrp.storage.ModelService;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import net.cosmogrp.storage.dist.LocalModelService;
import net.cosmogrp.storage.dist.RemoteModelService;
import online.nasgar.microbattles.api.MicroBattlesAPI;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.kit.KitApplier;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import online.nasgar.microbattles.api.storage.StorageType;

import javax.inject.Inject;

public class MicroBattlesAPIImpl implements MicroBattlesAPI {
  private final MatchManager matchManager;
  private final KitApplier kitApplier;
  private final ClickableItemApplier clickableItemApplier;
  private final MicroBattlesEngine engine;
  private final Injector injector;

  @Inject
  public MicroBattlesAPIImpl(MatchManager matchManager,
                             KitApplier kitApplier,
                             ClickableItemApplier clickableItemApplier,
                             MicroBattlesEngine engine,
                             Injector injector) {
    this.matchManager = matchManager;
    this.kitApplier = kitApplier;
    this.clickableItemApplier = clickableItemApplier;
    this.engine = engine;
    this.injector = injector;
  }

  @Override
  public MatchManager getMatchManager() {
    return matchManager;
  }

  @Override
  public KitApplier getKitApplier() {
    return kitApplier;
  }

  @Override
  public ClickableItemApplier getItemApplier() {
    return clickableItemApplier;
  }

  @Override
  public MicroBattlesEngine getEngine() {
    return engine;
  }

  @Override
  public <T extends Model> ModelService<T> getService(Class<T> model,
                                                      StorageType storageType) {
    Class<? extends ModelService> serviceType = null;

    switch (storageType) {
      case REDIS: {
        serviceType = RemoteModelService.class;

        break;
      }
      case LOCAL: {
        serviceType = ModelService.class;

        break;
      }
      case MONGO: {
        serviceType = CachedRemoteModelService.class;
        break;
      }
    }

    return injector.getInstance(TypeReference.of(serviceType, model));
  }
}
