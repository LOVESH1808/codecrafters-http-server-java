package response;

import static enums.ServerConstants.CR_LF;
import static enums.ServerConstants.SPACE;

import enums.HttpStatus;

public class StatusLine implements HttpResponseLine {

  private String httpVersion;
  private HttpStatus httpStatus;
  private StatusLine(String httpVersion, HttpStatus httpStatus) {
    this.httpVersion = httpVersion;
    this.httpStatus = httpStatus;
  }
  @Override
  public String getResponseLine() {
    StringBuilder stringBuilder = new StringBuilder();
    String statusLine = stringBuilder.append(this.httpVersion)
        .append(SPACE).append(this.httpStatus.getStatus())
        .append(CR_LF).toString();
    return statusLine;
  }

  public static class StatusLineBuilder {
    private String httpVersion;
    private HttpStatus httpStatus;

    public StatusLineBuilder httpVersion(String httpVersion) {
      this.httpVersion = httpVersion;
      return this;
    }
    public StatusLineBuilder status(HttpStatus status) {
      this.httpStatus = status;
      return this;
    }
    public StatusLine build() {
      return new StatusLine(httpVersion, httpStatus);
    }
  }
}