package webserver.parser;

public class DoubleParser implements Parser{
  @Override
  public boolean canParse(String typename) {
    return typename.equals("double");
  }

  @Override
  public Object parse(String object) {
    return Double.valueOf(object);
  }
}
