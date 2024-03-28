package request;

public class RequestLine{

  private String httpMethod;
  private String requestTarget;
  private String httpVersion;

  private RequestLine(String httpMethod, String requestTarget, String httpVersion) {
    this.httpMethod = httpMethod;
    this.requestTarget = requestTarget;
    this.httpVersion = httpVersion;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public String getHttpMethod() {
    return httpMethod;
  }

  public String getRequestTarget() {
    return requestTarget;
  }

  public static class RequestLineBuilder {
    private String httpMethod;
    private String requestTarget;
    private String httpVersion;

    public RequestLineBuilder httpVersion(String httpVersion) {
      this.httpVersion = httpVersion;
      return this;
    }
    public RequestLineBuilder requestTarget(String requestTarget) {
      this.requestTarget = requestTarget;
      return this;
    }
    public RequestLineBuilder httpMethod(String httpMethod) {
      this.httpMethod = httpMethod;
      return this;
    }
    public RequestLine build() {
      return new RequestLine(httpMethod, requestTarget, httpVersion);
    }
  }
}