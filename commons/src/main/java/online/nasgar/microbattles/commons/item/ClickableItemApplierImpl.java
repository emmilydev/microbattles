package online.nasgar.microbattles.commons.item;

import me.yushust.message.MessageHandler;
import net.cosmogrp.storage.ModelService;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import online.nasgar.microbattles.api.model.kit.item.ClickableItemApplier;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.item.ItemBuilder;

import javax.inject.Inject;

public class ClickableItemApplierImpl implements ClickableItemApplier {
  private final MessageHandler messageHandler;
  private final ModelService<ClickableItem> itemService;
  private final NbtHandler nbtHandler;

  @Inject
  public ClickableItemApplierImpl(MessageHandler messageHandler,
                                  ModelService<ClickableItem> itemService,
                                  NbtHandler nbtHandler) {
    this.messageHandler = messageHandler;
    this.itemService = itemService;
    this.nbtHandler = nbtHandler;
  }

  @Override
  public void apply(Player player,
                    ClickableItem itemStack) {
    player.getInventory().addItem(buildItem(player, itemStack));
  }

  @Override
  public void apply(Player player,
                    ClickableItem itemStack,
                    int index) {
    player.getInventory().setItem(index, buildItem(player, itemStack));
  }

  @Override
  public void apply(Player player,
                    String itemStack) {
    apply(player, itemService.findSync(itemStack));
  }

  @Override
  public void apply(Player player,
                    String itemStack,
                    int index) {
    apply(player, itemService.findSync(itemStack), index);
  }

  @Override
  public ItemStack buildItem(Player player,
                             ClickableItem itemStack) {
    try {
      ItemBuilder itemBuilder;

      if (itemStack.getMaterial() == Material.SKULL_ITEM) {
        Bukkit.getLogger().info("sis");
        itemBuilder = ItemBuilder
          .skullBuilder(itemStack.getAmount())
          .owner(player.getName());
      } else {
        Bukkit.getLogger().info("non");
        itemBuilder = ItemBuilder.builder(itemStack.getMaterial(), itemStack.getAmount());
      }

      itemBuilder = itemBuilder
        .name(messageHandler.get(player, itemStack.getName()))
        .lore(messageHandler.getMany(player, itemStack.getLore()));
      return nbtHandler.addTag(
        itemBuilder.build(),
        "microbattles:clickable",
        itemStack.getId()
      );
    } catch (Exception e) {
      Bukkit.getLogger().info(e.toString() + " pene");

      return null;
    }
  }
}
