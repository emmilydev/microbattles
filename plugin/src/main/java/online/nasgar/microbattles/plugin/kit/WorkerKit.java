package online.nasgar.microbattles.plugin.kit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import org.bukkit.Material;

public class WorkerKit extends Kit {
  private static final String TRANSLATION_KEY = "kit.worker.";

  public WorkerKit() {
    super(
      "worker",
      TRANSLATION_KEY + "description",
      ImmutableMap
        .<Integer, ClickableItem>builder()
        .put(0, new ClickableItem(
          "sword",
          TRANSLATION_KEY + "item.sword",
          Material.WOOD_SWORD,
          1
        ))
        .put(1, new ClickableItem(
          "shovel",
          TRANSLATION_KEY + "item.shovel",
          Material.STONE_SPADE,
          1
        ))
        .put(2, new ClickableItem(
          "pickaxe",
          TRANSLATION_KEY + "item.pickaxe",
          Material.STONE_PICKAXE,
          1
        ))
        .build(),
      ImmutableSet.of(
        new ClickableItem(
          "helmet",
          TRANSLATION_KEY + "armor.helmet",
          Material.LEATHER_HELMET,
          1
        ),
        new ClickableItem(
          "chestplate",
          TRANSLATION_KEY + "armor.chestplate",
          Material.LEATHER_CHESTPLATE,
          1
        ),
        new ClickableItem(
          "leggings",
          TRANSLATION_KEY + "armor.leggings",
          Material.LEATHER_LEGGINGS,
          1
        ),
        new ClickableItem(
          "boots",
          TRANSLATION_KEY + "armor.boots",
          Material.LEATHER_BOOTS,
          1
        )
      ),
      Material.DIAMOND_PICKAXE,
      0,
      "",
      0
    );
  }
}
