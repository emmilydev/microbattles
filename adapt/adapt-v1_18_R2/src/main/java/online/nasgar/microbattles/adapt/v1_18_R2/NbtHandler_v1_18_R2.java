package online.nasgar.microbattles.adapt.v1_18_R2;

import net.minecraft.nbt.NBTTagCompound;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NbtHandler_v1_18_R2 implements NbtHandler {
  @Override
  public boolean hasTag(ItemStack itemStack,
                        String key) {
    NBTTagCompound compound = CraftItemStack.asNMSCopy(itemStack).t();

    return compound != null && compound.e(key);
  }

  @Override
  public String getTag(ItemStack itemStack,
                       String key) {
    NBTTagCompound compound = CraftItemStack
      .asNMSCopy(itemStack)
      .t();

    if (compound == null) {
      return null;
    }

    return compound.l(key);
  }

  @Override
  public ItemStack addTag(ItemStack itemStack,
                          String key,
                          String value) {
    net.minecraft.world.item.ItemStack copy = CraftItemStack.asNMSCopy(itemStack);
    NBTTagCompound compound = copy.t();

    if (compound == null) {
      compound = new NBTTagCompound();
      copy.c(compound);
    }

    compound.a(key, value);

    return CraftItemStack.asBukkitCopy(copy);
  }
}
