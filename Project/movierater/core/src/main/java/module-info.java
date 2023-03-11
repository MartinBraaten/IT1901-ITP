module movierater.core {
  requires transitive com.fasterxml.jackson.databind;

  exports movierater.core;
  exports movierater.json;

  opens movierater.core to
      com.fasterxml.jackson.databind;
}
