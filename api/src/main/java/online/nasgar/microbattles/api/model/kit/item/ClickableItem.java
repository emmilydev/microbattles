package online.nasgar.microbattles.api.model.kit.item;

import com.fasterxml.jackson.annotation.JsonIgnore;
import online.nasgar.microbattles.api.model.Model;
import org.bukkit.Material;

import java.beans.ConstructorProperties;

public class ClickableItem implements Model {
  private final String id;
  private final String translationKey;
  private final Material material;
  private final int amount;

  @ConstructorProperties({
    "id",
    "translationKey", 
    "material",
    "amount"
  })
  public ClickableItem(String id,
                       String translationKey,
                       Material material,
                       int amount) {
    this.id = id;
    this.translationKey = translationKey;
    this.material = material;
    this.amount = amount;
  }

  @Override
  public String getId() {
    return id;
  }

  public String getTranslationKey() {
    return translationKey;
  }

  public Material getMaterial() {
    return material;
  }

  @JsonIgnore
  public String getName() {
    return translationKey + ".name";
  }

  @JsonIgnore
  public String getLore() {
    return translationKey + ".lore";
  }

  public int getAmount() {
    return amount;
  }
}
