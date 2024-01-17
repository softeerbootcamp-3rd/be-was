package webserver.mapper;

import webserver.MyHttpServletRequest;
import webserver.io.ControllerHandler;
import webserver.parser.DoubleParser;
import webserver.parser.IntegerParser;
import webserver.parser.Parser;
import webserver.parser.StringParser;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class ParameterMapper {
  private final List<Parser> parsers = new ArrayList<>();
  public ParameterMapper(){
    parsers.add(new IntegerParser());
    parsers.add(new DoubleParser());
    parsers.add(new StringParser());
  }

  public void findAppropriateParameter(ControllerHandler handler, MyHttpServletRequest request){
    Parameter[] parameters = handler.getMethod().getParameters();
    List<Object> mappedParameters = new ArrayList<>();

    for(Parameter p : parameters){
      if(!p.isNamePresent())
        throw new ParameterMapperException();
      String parameterTypeName=p.getType().getSimpleName();
      String parameterName=p.getName();
      String parameterValue = request.getQueryParameterValue(parameterName);

      boolean successParsing=false;
      for (Parser parser : parsers){
        if (parser.canParse(parameterTypeName)){
          mappedParameters.add(parser.parse(parameterValue));
          successParsing=true;
          break;
        }
      }
      if(!successParsing)
        throw new ParameterMapperException();
    }
    handler.setByParameterMapper(mappedParameters.toArray());
  }
  private static class ParameterMapperException extends RuntimeException{

  }
}
