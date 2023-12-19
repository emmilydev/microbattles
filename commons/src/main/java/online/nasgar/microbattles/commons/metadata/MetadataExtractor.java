package online.nasgar.microbattles.commons.metadata;

import org.bukkit.entity.Player;

import java.util.function.Function;

public interface MetadataExtractor {
  static <T> T extract(Player player,
                       String key,
                       int index,
                       Function<Object, T> parser) {
    return parser.apply(player
      .getMetadata(key)
      .get(index)
      .value()
    );
  }

  static <T> T extract(Player player,
                       String key,
                       Function<Object, T> parser) {
    return extract(player, key, 0, parser);
  }
}
