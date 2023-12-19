package online.nasgar.microbattles.lobby.kit;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import online.nasgar.microbattles.api.model.kit.Kit;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import org.bukkit.Material;

public class LobbyKit extends Kit {
  private static final String TRANSLATION_KEY = "kit.defaults.lobby";

  public LobbyKit() {
    super(
      "lobby",
      TRANSLATION_KEY,
      ImmutableMap
        .<Integer, ClickableItem>builder()
        .put(0, new ClickableItem(
          "play",
          TRANSLATION_KEY + ".items.play",
          Material.BOW,
          1
        ))
        .put(1, new ClickableItem(
          "spectate",
          TRANSLATION_KEY + ".items.spectate",
          Material.EYE_OF_ENDER,
          1
        ))
        .put(4, new ClickableItem(
          "party",
          TRANSLATION_KEY + ".items.party",
          Material.CAKE,
          1
        ))
        .put(7, new ClickableItem(
          "kits",
          TRANSLATION_KEY + ".items.kits",
          Material.LEATHER_CHESTPLATE,
          1
        ))
        .put(8, new ClickableItem(
          "profile",
          TRANSLATION_KEY + ".items.profile",
          Material.DIRT,
          1
        ))
        .build(),
      ImmutableSet.of(),
      Material.DIRT,
      0,
      "",
      0
    );
  }
}
