package online.nasgar.microbattles.api.model.effect;

import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.user.User;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface Effect extends Model {
  Type getType();

  Consumer<User> getAction();

  enum Type {
    WIN,
    KILL,
    DEATH,
    LEVEL_UP,
    ACHIEVEMENT
  }
}
