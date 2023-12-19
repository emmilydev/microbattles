package online.nasgar.microbattles.commons.adapt;

import online.nasgar.microbattles.api.adapt.MaterialProvider;
import online.nasgar.microbattles.api.adapt.NbtHandler;
import online.nasgar.microbattles.api.adapt.TitleSender;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static online.nasgar.microbattles.api.version.ServerVersionProvider.SERVER_VERSION;

public class AdaptionFactory {
  private static final String CLASS_PREFIX;
  private static final Constructor<?> TITLE_SENDER_CONSTRUCTOR;
  private static final Constructor<?> MATERIAL_PROVIDER_CONSTRUCTOR;
  private static final Constructor<?> NBT_HANDLER_CONSTRUCTOR;

  static {
    CLASS_PREFIX = "online.nasgar.microbattles.adapt.v" + SERVER_VERSION + ".";

    try {
      TITLE_SENDER_CONSTRUCTOR = getConstructor("TitleSender");
      MATERIAL_PROVIDER_CONSTRUCTOR = getConstructor("MaterialProvider");
      NBT_HANDLER_CONSTRUCTOR = getConstructor("NbtHandler");
    } catch (NoSuchMethodException | ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  public static Constructor<?> getConstructor(String name) throws ClassNotFoundException, NoSuchMethodException {
    return Class
      .forName(CLASS_PREFIX + name + "_v" + SERVER_VERSION)
      .getConstructor();
  }

  public static TitleSender createTitleSender() {
    try {
      return (TitleSender) TITLE_SENDER_CONSTRUCTOR.newInstance();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  public static MaterialProvider createMaterialProvider() {
    try {
      return (MaterialProvider) MATERIAL_PROVIDER_CONSTRUCTOR.newInstance();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }

  public static NbtHandler createNbtHandler() {
    try {
      return (NbtHandler) NBT_HANDLER_CONSTRUCTOR.newInstance();
    } catch (ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }
  }
}
