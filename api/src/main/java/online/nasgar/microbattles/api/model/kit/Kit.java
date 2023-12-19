package online.nasgar.microbattles.api.model.kit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import online.nasgar.microbattles.api.model.Model;
import online.nasgar.microbattles.api.model.kit.item.ClickableItem;
import online.nasgar.microbattles.api.model.translation.TranslatableModel;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.beans.ConstructorProperties;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Kit implements Model, TranslatableModel {
  private final String id;
  private String translationKey;
  private final Map<Integer, ClickableItem> equipment;
  private final Set<ClickableItem> armor;
  private Material icon;
  private int price;
  // TODO: add 'em
  private String permission;
  private int delay;

  @ConstructorProperties({
    "id",
    "translationKey",
    "equipment",
    "armor",
    "icon",
    "price",
    "permission",
    "delay"
  })
  public Kit(String id,
             String translationKey,
             Map<Integer, ClickableItem> equipment,
             Set<ClickableItem> armor,
             Material icon,
             int price,
             String permission,
             int delay) {
    this.id = id;
    this.translationKey = translationKey;
    this.equipment = equipment;
    this.armor = armor;
    this.icon = icon;
    this.price = price;
    this.permission = permission;
    this.delay = Math.toIntExact(TimeUnit.SECONDS.toMillis(delay));
  }

  @Override
  public String getId() {
    return id;
  }

  public Map<Integer, ClickableItem> getEquipment() {
    return equipment;
  }

  public Set<ClickableItem> getArmor() {
    return armor;
  }

  @Override
  public String getTranslationKey() {
    return translationKey;
  }

  @Override
  public void setTranslationKey(String translationKey) {
    this.translationKey = translationKey;
  }

  public Material getIcon() {
    return icon;
  }

  public void setIcon(Material icon) {
    this.icon = icon;
  }

  @JsonIgnore
  public String getName() {
    return translationKey + ".name";
  }

  @JsonIgnore
  public String getDescription() {
    return translationKey + ".description";
  }

  public int getPrice() {
    return price;
  }

  public void setPrice(int price) {
    this.price = price;
  }

  public String getPermission() {
    return permission;
  }

  public void setPermission(String permission) {
    this.permission = permission;
  }

  public int getDelay() {
    return delay;
  }

  public void setDelay(int delay) {
    this.delay = delay;
  }
}
