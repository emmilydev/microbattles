package online.nasgar.microbattles.api.model.kit;

import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.entity.Player;

public interface KitApplier {
  void apply(Player player,
             Kit kit);

  void apply(Player player,
             String kit);

  default void apply(User user, Kit
    kit) {
    apply(user.player(), kit);
  }

  default void apply(User user,
                     String kit) {
    apply(user.player(), kit);
  }

}
