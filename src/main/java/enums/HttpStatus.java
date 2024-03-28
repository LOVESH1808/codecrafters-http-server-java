package enums;

public enum HttpStatus {
  OK("200 OK"),
  NOT_FOUND("404 Not Found");
  private final String status;

  HttpStatus(String status) {
    this.status = status;
  }
  public String getStatus() {
    return status;
  }
}