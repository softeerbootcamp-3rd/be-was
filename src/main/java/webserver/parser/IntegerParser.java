package webserver.parser;

public class IntegerParser implements Parser{
  @Override
  public boolean canParse(String typename) {
    return typename.equals("int");
  }

  @Override
  public Object parse(String object) {
    return Integer.valueOf(object);
  }
}
