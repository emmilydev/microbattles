package online.nasgar.microbattles.adapt.v1_8_R3;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NbtHandler_v1_8_R3 implements NbtHandler {
  @Override
  public boolean hasTag(ItemStack itemStack,
                        String key) {
    NBTTagCompound compound = CraftItemStack.asNMSCopy(itemStack).getTag();

    return compound != null && compound.hasKey(key);
  }

  @Override
  public String getTag(ItemStack itemStack,
                       String key) {
    NBTTagCompound compound = CraftItemStack
      .asNMSCopy(itemStack)
      .getTag();

    if (compound == null) {
      return null;
    }

    return compound.getString(key);
  }

  @Override
  public ItemStack addTag(ItemStack itemStack,
                          String key,
                          String value) {
    net.minecraft.server.v1_8_R3.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
    NBTTagCompound compound = copy.getTag();

    if (compound == null) {
      compound = new NBTTagCompound();
      copy.setTag(compound);
    }

    compound.setString(key, value);

    return CraftItemStack.asBukkitCopy(copy);
  }
}
