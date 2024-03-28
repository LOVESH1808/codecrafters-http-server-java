package response;

import static enums.ServerConstants.COLON;
import static enums.ServerConstants.CR_LF;
import static enums.ServerConstants.SPACE;

public class ResponseHeaderLine<T> implements HttpResponseLine {
  private String headerName;
  private T headerValue;

  private ResponseHeaderLine(String headerName, T headerValue) {
    this.headerName = headerName;
    this.headerValue = headerValue;
  }

  public String getHeaderName() {
    return headerName;
  }

  public T getHeaderValue() {
    return headerValue;
  }

  @Override
  public String getResponseLine() {
    StringBuilder stringBuilder = new StringBuilder();
    String statusLine = stringBuilder.append(this.headerName)
        .append(COLON).append(SPACE).append(this.headerValue)
        .append(CR_LF).toString();
    return statusLine;
  }

  public static class HeaderLineBuilder<T> {
    private String headerName;
    private T headerValue;

    public ResponseHeaderLine.HeaderLineBuilder headerName(String headerName) {
      this.headerName = headerName.substring(0, headerName.length());
      return this;
    }
    public ResponseHeaderLine.HeaderLineBuilder headerValue(T headerValue) {
      this.headerValue = headerValue;
      return this;
    }
    public ResponseHeaderLine build() {
      return new ResponseHeaderLine(headerName, headerValue);
    }
  }
}