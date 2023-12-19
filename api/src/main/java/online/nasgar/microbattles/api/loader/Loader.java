package online.nasgar.microbattles.api.loader;

public interface Loader {
  default void load() {}

  default void unload() {}
}
