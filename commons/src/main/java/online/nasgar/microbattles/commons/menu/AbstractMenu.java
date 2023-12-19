package online.nasgar.microbattles.commons.menu;

import me.yushust.message.MessageHandler;

import javax.inject.Inject;
import java.util.List;

public class AbstractMenu {
  private static final String MENU_FORMAT = "menu.%s";
  private static final String ITEM_FORMAT = MENU_FORMAT + ".item.%s";
  private static final String ITEM_NAME_FORMAT = ITEM_FORMAT + ".name";
  private static final String ITEM_LORE_FORMAT = ITEM_FORMAT + ".lore";

  @Inject protected MessageHandler messageHandler;
  private final String menuId;

  public AbstractMenu(String menuId) {
    this.menuId = menuId;
  }

  public String getTitle(Object entity,
                         Object... replacements) {
    if (replacements != null) {
      return messageHandler.replacing(
        entity,
        String.format(MENU_FORMAT, menuId),
        replacements
      );
    } else {
      return messageHandler.get(
        entity,
        String.format(MENU_FORMAT, menuId)
      );
    }
  }

  public String getItemName(Object entity,
                            String item,
                            Object... replacements) {
    if (replacements != null) {
      return messageHandler.replacing(
        entity,
        String.format(ITEM_NAME_FORMAT, menuId, item),
        replacements
      );
    } else {
      return messageHandler.get(
        entity,
        String.format(ITEM_NAME_FORMAT, menuId, item)
      );
    }
  }

  public List<String> getItemLore(Object entity,
                                  String item,
                                  Object... replacements) {
    if (replacements != null) {
      return messageHandler.replacingMany(
        entity,
        String.format(ITEM_LORE_FORMAT, menuId, item),
        replacements
      );
    } else {
      return messageHandler.getMany(
        entity,
        String.format(ITEM_LORE_FORMAT, menuId, item)
      );
    }
  }
}
