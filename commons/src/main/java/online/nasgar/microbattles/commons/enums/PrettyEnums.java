package online.nasgar.microbattles.commons.enums;

public interface PrettyEnums {
  static String prettify(Enum<?> value) {
    return value.name().replace("_", "-").toLowerCase();
  }
}
