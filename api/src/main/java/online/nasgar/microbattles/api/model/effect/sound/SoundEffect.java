package online.nasgar.microbattles.api.model.effect.sound;

import com.cryptomorin.xseries.XSound;
import online.nasgar.microbattles.api.model.effect.Effect;
import online.nasgar.microbattles.api.model.user.User;
import online.nasgar.microbattles.api.sound.SoundPlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.beans.ConstructorProperties;
import java.util.function.Consumer;

public class SoundEffect implements Effect {

  private final String id;
  private final Type type;
  private final XSound sound;

  @ConstructorProperties({
    "id",
    "type",
    "sound"
  })
  public SoundEffect(String id,
                     Type type,
                     XSound sound) {
    this.id = id;
    this.type = type;
    this.sound = sound;
  }

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public Consumer<User> getAction() {
    return user -> SoundPlayer.playNormal(
      user.player(),
      sound
    );
  }
}
