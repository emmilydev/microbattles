package online.nasgar.microbattles.plugin.task;

import online.nasgar.microbattles.api.model.arena.Arena;
import online.nasgar.microbattles.api.model.match.Match;

import java.lang.reflect.Field;
import java.util.Random;

public class ArenaBreakerTask implements Runnable {
  private final Match match;
  private final Arena arena;

  public ArenaBreakerTask(Match match,
                          Arena arena) {
    this.match = match;
    this.arena = arena;
  }

  @Override
  public void run() {
    /*
    Block block = arena.getBounds().getBlocks().get(new Random().nextInt(arena.getBounds().getBlocks().size() - 1));
    Material type = block.getType();
    if (type == Material.AIR) {
      return;
    }
    block.setType(Material.AIR);
    Bukkit.getOnlinePlayers().forEach(player -> player.teleport(block.getLocation()));
    block.getWorld().spawnFallingBlock(block.getLocation(), type, (byte) 0);
     */
  }
}
