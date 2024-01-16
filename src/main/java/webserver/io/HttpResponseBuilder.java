package webserver.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpResponseBuilder {

  private static final Logger logger = LoggerFactory.getLogger(HttpResponseBuilder.class);
  private static final String absoluteRootPath = Paths.get("").toAbsolutePath().toString();
  private static final String templateResourcePath = "/src/main/resources/templates";
  private static final String staticResourcePath = "/src/main/resources/static";

  public byte[] getFileBytes(String uri){
    byte[] body = null;
    try {
      body = Files.readAllBytes(buildPath(uri));
    }catch (IOException ioException){
      logger.error(ioException.getMessage());
    }
    return body;
  }

  private Path buildPath(String uri){
    String path = absoluteRootPath+templateResourcePath+uri;
    return Paths.get(path);
  }
}
