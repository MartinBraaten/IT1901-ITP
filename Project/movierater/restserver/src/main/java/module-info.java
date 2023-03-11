module movierater.restserver {
  requires com.fasterxml.jackson.databind;
  requires spring.web;
  requires spring.beans;
  requires spring.boot;
  requires spring.context;
  requires spring.boot.autoconfigure;
  requires transitive movierater.core;

  opens movierater.restserver to
      spring.beans,
      spring.context,
      spring.web;

  exports movierater.restserver;
}
