package webserver.parser;

public class DoubleParser implements Parser{
  @Override
  public boolean canParse(String typename) {
    return typename.equals("double") || typename.equals("Double");
  }

  @Override
  public Object parse(String object) {
    if(object==null)
      return null;
    return Double.valueOf(object);
  }
}
