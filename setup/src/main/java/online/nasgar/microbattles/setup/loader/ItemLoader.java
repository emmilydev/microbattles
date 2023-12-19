package online.nasgar.microbattles.setup.loader;

import net.cosmogrp.storage.ModelService;
import online.nasgar.microbattles.api.loader.Loader;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import online.nasgar.microbattles.setup.item.BarrierEditorItem;

import javax.inject.Inject;

public class ItemLoader implements Loader {
  private final ModelService<ClickableItem> itemService;

  @Inject
  public ItemLoader(ModelService<ClickableItem> itemService) {
    this.itemService = itemService;
  }

  @Override
  public void load() {
    itemService.saveSync(new BarrierEditorItem());
  }

  @Override
  public void unload() {
    for (ClickableItem clickableItem : itemService.findAllSync()) {
      itemService.deleteSync(clickableItem);
    }
  }
}
