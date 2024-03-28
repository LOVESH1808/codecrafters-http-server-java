package response;

import static enums.ServerConstants.CR_LF;

import enums.HttpStatus;
import java.util.List;

public class HttpResponse {
  private StatusLine statusLine;
  private List<ResponseHeaderLine> responseHeaderLines;
  private ResponseBodyLine body;
  public HttpResponse(){}
  public StatusLine getStatusLine(String httpVersion, HttpStatus httpStatus ) {
    StatusLine.StatusLineBuilder statusLineBuilder = new StatusLine.StatusLineBuilder();
    return statusLineBuilder
        .httpVersion(httpVersion)
        .status(HttpStatus.valueOf(httpStatus.name())).build();
  }
  public <T> ResponseHeaderLine getHeaderLine(String headerName, T headerValue) {
    ResponseHeaderLine.HeaderLineBuilder headerLineBuilder = new ResponseHeaderLine.HeaderLineBuilder();
    return headerLineBuilder
        .headerName(headerName)
        .headerValue(headerValue).build();
  }

  public <T> ResponseBodyLine getResponseBodyLine(T body) {
    ResponseBodyLine.ResponseBodyLineBuilder responseBodyLineBuilder = new ResponseBodyLine.ResponseBodyLineBuilder();
    return responseBodyLineBuilder
        .body(body).build();
  }
  public String writeResponse() {
    StringBuilder response = new StringBuilder();
    response.append(statusLine.getResponseLine());
    for (ResponseHeaderLine responseHeaderLine : responseHeaderLines) {
      response.append(responseHeaderLine.getResponseLine());
    }
    // marks the end of headers
    response.append(CR_LF);
    if(body != null)
      response.append(body.getResponseLine());
    System.out.println("Logging response body :: " + response);
    return response.toString();
  }

  public void setStatusLine(StatusLine statusLine) {
    this.statusLine = statusLine;
  }

  public void setHeaderLines(List<ResponseHeaderLine> responseHeaderLines) {
    this.responseHeaderLines = responseHeaderLines;
  }

  public void setBody(ResponseBodyLine body) {
    this.body = body;
  }
}