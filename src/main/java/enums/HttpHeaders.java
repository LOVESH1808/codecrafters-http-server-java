package enums;

public enum HttpHeaders {
  CONTENT_TYPE("Content-Type"),
  CONTENT_LENGTH("Content-Length"),
  CONNECTION("Connection"),
  DATE("Date"),
  SERVER("Server"),
  HOST("Host"),
  LOCATION("Location"),
  WWW_AUTHENTICATE("WWW-Authenticate"),
  AUTHORIZATION("Authorization"),
  SET_COOKIE("Set-Cookie"),
  COOKIE("Cookie"),
  UPGRADE("Upgrade"),
  USER_AGENT("User-Agent");

  private final String header;

  HttpHeaders(String header) {
    this.header = header;
  }

  public String getHeader() {
    return header;
  }
}