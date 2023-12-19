package online.nasgar.microbattles.api;

import net.cosmogrp.storage.ModelService;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.match.MatchManager;
import online.nasgar.microbattles.api.model.engine.MicroBattlesEngine;
import online.nasgar.microbattles.api.model.kit.KitApplier;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import online.nasgar.microbattles.api.storage.StorageType;

public interface MicroBattlesAPI {
  MatchManager getMatchManager();

  KitApplier getKitApplier();

  ClickableItemApplier getItemApplier();

  MicroBattlesEngine getEngine();

  <T extends Model> ModelService<T> getService(Class<T> model,
                                               StorageType storageType);

}
