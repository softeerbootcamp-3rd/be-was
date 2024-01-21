package webserver.parser;

public class StringParser implements Parser{
  @Override
  public boolean canParse(String typename) {
    return typename.equals("String");
  }

  @Override
  public Object parse(String object) {
    return object;
  }
}
