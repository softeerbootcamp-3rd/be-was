package webserver.parser;

public interface Parser {
  boolean canParse(String typename);
  Object parse(String object);
}
