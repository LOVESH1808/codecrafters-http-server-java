package response;

import static enums.ServerConstants.CR_LF;

public class ResponseBodyLine<T> implements HttpResponseLine{

  private final T body;

  public ResponseBodyLine(T body) {
    this.body =  body;
  }

  @Override
  public String getResponseLine() {
    StringBuilder stringBuilder = new StringBuilder();
    String bodyLine = stringBuilder.append(this.body).append(CR_LF).toString();
    return bodyLine;
  }

  public static class ResponseBodyLineBuilder<T> {
    private T body;
    public ResponseBodyLineBuilder body(T body) {
      this.body = body;
      return this;
    }
    public ResponseBodyLine build() {
      return new ResponseBodyLine(body);
    }
  }
}