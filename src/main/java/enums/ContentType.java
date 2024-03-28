package enums;

public enum ContentType {
  PLAIN_TEXT("text/plain"),
  HTML("text/html"),
  JSON("application/json"),
  OCTET_STREAM("application/octet-stream");


  private final String type;

  ContentType(String type) {
    this.type = type;
  }

  public String getType() {
    return type;
  }
}