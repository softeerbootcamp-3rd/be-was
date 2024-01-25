package webserver.mapper;

import webserver.MyHttpServletRequest;
import webserver.handler.ControllerHandler;
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
    //ControllerMapper가 찾아낸 method의 parameter 리스트 획득
    Parameter[] parameters = handler.getMethod().getParameters();
    //매핑된 parameter는 mappedParameters에 저장 (매핑 실패해도 null로 저장)
    List<Object> mappedParameters = new ArrayList<>();

    for(Parameter p : parameters){
      if(!p.isNamePresent())
        throw new ParameterMapperException();
      //파라미터의 이름, 변수타입 이름(Integer,Double..) 획득
      String parameterTypeName=p.getType().getSimpleName();
      String parameterName=p.getName();
      //쿼리파라미터에서 파라미터 이름에 맞는 value 획득
      String parameterValue = request.getQueryParameterValue(parameterName);

      boolean successParsing=false;
      for (Parser parser : parsers){
        //특정 Parser가 parsing이 가능할 때, 파싱 로직 수행
        if (parser.canParse(parameterTypeName)){
          Object convertedParameter = null;
          //parse수행, exception 발생시에는 mappedParameters에 null 삽입
          try {
            convertedParameter = parser.parse(parameterValue);
          }finally {
            mappedParameters.add(convertedParameter);
          }
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
