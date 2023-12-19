package online.nasgar.microbattles.commons.kit;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.dist.CachedRemoteModelService;
import online.nasgar.microbattles.api.error.ErrorNotifier;
import online.nasgar.microbattles.api.message.MessageMode;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.model.kit.KitApplier;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class KitApplierImpl implements KitApplier {
  private final ClickableItemApplier itemApplier;
  private final CachedRemoteModelService<Kit> kitService;
  private final MessageHandler messageHandler;

  @Inject
  public KitApplierImpl(ClickableItemApplier itemApplier,
                        CachedRemoteModelService<Kit> kitService,
                        MessageHandler messageHandler) {
    this.itemApplier = itemApplier;
    this.kitService = kitService;
    this.messageHandler = messageHandler;
  }


  @Override
  public void apply(Player player,
                    Kit kit) {
    if (kit == null) {
      messageHandler.sendReplacingIn(
        player,
        MessageMode.ERROR,
        "kit.not-found"
      );

      return;
    }

    try {
      for (Map.Entry<Integer, ClickableItem> itemEntry : kit.getEquipment().entrySet()) {
        Bukkit.getLogger().info(itemEntry.toString());
        itemApplier.apply(player, itemEntry.getValue(), itemEntry.getKey());
      }

      List<ItemStack> armor = new ArrayList<>();

      for (ClickableItem armorItem : kit.getArmor()) {
        armor.add(itemApplier.buildItem(player, armorItem));
      }

      player.getInventory().setArmorContents(armor.toArray(new ItemStack[0]));
    } catch (Exception e) {
      Bukkit.getScheduler().runTask(Bukkit.getPluginManager().getPlugin("spark"), e::printStackTrace);
    }
  }

  @Override
  public void apply(Player player,
                    String kitId) {

    kitService
      .find(kitId)
      .whenComplete((kit, error) -> {
        if (error != null) {
          ErrorNotifier.notify(error);

          messageHandler.sendReplacingIn(
            player,
            MessageMode.ERROR,
            "kit.error",
            "%kit%", kitId
          );

          return;
        }

        apply(player, kit);
      });
  }
}
