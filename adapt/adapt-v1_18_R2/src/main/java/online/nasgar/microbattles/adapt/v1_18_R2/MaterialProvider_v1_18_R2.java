package online.nasgar.microbattles.adapt.v1_18_R2;

import online.nasgar.microbattles.api.adapt.MaterialProvider;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class MaterialProvider_v1_18_R2 implements MaterialProvider {
  private static final List<Material> MATERIALS = Arrays.asList(Material.values());

  @Override
  public List<Material> getAvailableMaterials() {
    return MATERIALS;
  }

  @Override
  public Material getMaterial(String name) {
    return Material.getMaterial(name);
  }
}
