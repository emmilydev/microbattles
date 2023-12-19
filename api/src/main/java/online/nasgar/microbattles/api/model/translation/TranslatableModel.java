package online.nasgar.microbattles.api.model.translation;

import online.nasgar.microbattles.api.model.Model;

public interface TranslatableModel extends Model {
  String getTranslationKey();

  void setTranslationKey(String translationKey);
}
