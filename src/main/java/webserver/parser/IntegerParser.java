package webserver.parser;

public class IntegerParser implements Parser{
  @Override
  public boolean canParse(String typename) {
    return typename.equals("int")||typename.equals("Integer");
  }

  @Override
  public Object parse(String object) {
    if(object==null)
      return null;
    return Integer.valueOf(object);
  }
}
