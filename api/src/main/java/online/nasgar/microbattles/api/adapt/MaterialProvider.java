package online.nasgar.microbattles.api.adapt;

import org.bukkit.Material;

import java.util.List;

/**
 * Version-nonspecific interface that is in
 * charge of providing the list of materials
 * available in the current server version.
 */
public interface MaterialProvider {
  /**
   * Returns the material list of
   * this server version.
   *
   * @return The material list of
   * this server version.
   */
  List<Material> getAvailableMaterials();

  /**
   * Returns the version-specific material
   * matching the given {@code name}.
   *
   * @param name The name of the material.
   * @return The version-specific material
   * matching the given {@code name}.
   */
  Material getMaterial(String name);

}
