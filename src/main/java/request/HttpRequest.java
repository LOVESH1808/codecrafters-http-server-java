package request;

import response.ResponseHeaderLine;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class HttpRequest {
    private BufferedReader bufferedReader;
    private RequestLine requestLine;
    private List<RequestHeaderLine> requestHeaderLines =  new ArrayList<>();

  public HttpRequest(InputStream socketInputStream) {
        this.bufferedReader = new BufferedReader(new InputStreamReader(socketInputStream));
    }
    public HttpRequest parse() throws IOException {
      // read line by line from the bufferedReader
      try {
        //first line is requestLine
        String requestLine = bufferedReader.readLine();
        String [] requestLineSplit = requestLine.split("\\s+");
        this.requestLine = new RequestLine.RequestLineBuilder()
            .httpMethod(requestLineSplit[0])
            .requestTarget(requestLineSplit[1])
            .httpVersion(requestLineSplit[2])
            .build();

        // read header lines
        String line;
        while ((line = bufferedReader.readLine()) != null) {
          if (!line.isEmpty()) {
            String [] lineSplit = line.split("\\s+");
            RequestHeaderLine responseHeaderLine = new RequestHeaderLine.HeaderLineBuilder()
                .headerName(lineSplit[0])
                .headerValue(lineSplit[1])
                .build();
            this.requestHeaderLines.add(responseHeaderLine);
          } else {
            break;
          }
        }

        } catch (Exception e){
          System.out.println("Error parsing request: " + e.getMessage());
        }
        return this;
    }

  public RequestLine getRequestLine() {
    return requestLine;
  }

  public List<RequestHeaderLine> getHeaderLines() {
    return requestHeaderLines;
  }
}