package online.nasgar.microbattles.setup.item;

import online.nasgar.microbattles.api.event.arena.ArenaBarrierEditEvent;
import online.nasgar.microbattles.api.event.arena.ArenaBarrierEditSessionEndEvent;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import online.nasgar.microbattles.api.model.point.CoordinatePoint;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;

public class BarrierEditorItem extends ClickableItem {
  public BarrierEditorItem() {
    super(
      "barrier-editor",
      "item.barrier-editor",
      Material.BLAZE_ROD,
      1
    );
  }
}
