package online.nasgar.microbattles.api.adapt;

import org.bukkit.inventory.ItemStack;

/**
 * Version-nonspecific NBT handler.
 */
public interface NbtHandler {
  /**
   * Returns {@code true} if the given
   * {@code itemStack} has a tag, and it
   * contains the given {@code key}; false
   * otherwise.
   *
   * @param itemStack The item to be checked.
   * @param key       The tag to be searched.
   * @return {@code true} if the given
   * {@code itemStack} has a tag, and it
   * contains the given {@code key}; false
   * otherwise.
   */
  boolean hasTag(ItemStack itemStack,
                 String key);

  /**
   * Retrieves the string value matching
   * the given {@code key} in the given
   * {@code itemStack}'s tag, if any.
   *
   * @param itemStack The item to retrieve the
   *                  tag from.
   * @param key       The key of the tag to be
   *                  retrieved.
   * @return The string value matching
   * the given {@code key} in the given
   * {@code itemStack}'s tag, if any.
   */
  String getTag(ItemStack itemStack,
                String key);

  /**
   * Adds the given {@code value} tag
   * with the given {@code key} to the
   * given {@code itemStack}.
   *
   * @param itemStack The item to add
   *                  the tag to.
   * @param key       The key of the tag.
   * @param value     The tag itself.
   * @return The given {@code value} tag
   * with the given {@code key} to the
   * given {@code itemStack}.
   */
  ItemStack addTag(ItemStack itemStack,
                   String key,
                   String value);
}
