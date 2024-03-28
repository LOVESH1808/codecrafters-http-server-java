import static enums.ServerConstants.CR_LF;
import static enums.ServerConstants.HTTP_VERSION_1_1;

import enums.ContentType;
import enums.HttpHeaders;
import enums.HttpStatus;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import response.FileHandler;
import response.ResponseHeaderLine;
import request.HttpRequest;
import response.HttpResponse;
import response.ResponseBodyLine;
import response.StatusLine;

public class ResponseHandler {

  public void handleOK(Socket clientSocket) throws IOException {
    var osStream = clientSocket.getOutputStream();
    HttpResponse response = new HttpResponse();
    StatusLine statusLine = response.getStatusLine(HTTP_VERSION_1_1, HttpStatus.OK);
    osStream.write(statusLine.getResponseLine().concat(CR_LF).getBytes(StandardCharsets.UTF_8));
    osStream.flush();
  }

  public void handle404(Socket clientSocket) throws IOException {
    var osStream = clientSocket.getOutputStream();
    HttpResponse response = new HttpResponse();
    StatusLine statusLine = response.getStatusLine(HTTP_VERSION_1_1, HttpStatus.NOT_FOUND);
    osStream.write(statusLine.getResponseLine().concat(CR_LF).getBytes(StandardCharsets.UTF_8));
    osStream.flush();
  }
  public static void handleEcho(Socket clientSocket, HttpRequest parsedRequest) throws IOException {
    var osStream = clientSocket.getOutputStream();
    String echoString = parsedRequest.getRequestLine().getRequestTarget().substring(6);
    HttpResponse response = buildStatusHeadersAndPayload(echoString);
    osStream.write(response.writeResponse().getBytes(StandardCharsets.UTF_8));
    osStream.flush();
  }

  public static void handleUserAgent(Socket clientSocket, HttpRequest parsedRequest) throws IOException {
    var osStream = clientSocket.getOutputStream();

    String userAgent = (String) parsedRequest.getHeaderLines().stream()
        .filter(responseHeaderLine1 -> responseHeaderLine1.getHeaderName().equals(HttpHeaders.USER_AGENT.getHeader()))
        .findFirst()
        .map(responseHeaderLine1 -> responseHeaderLine1.getHeaderValue())
        .orElse("Unknown");

    HttpResponse response = buildStatusHeadersAndPayload(userAgent);

    osStream.write(response.writeResponse().getBytes(StandardCharsets.UTF_8));
    osStream.flush();
  }

  private static HttpResponse buildStatusHeadersAndPayload(String echoString) {
    HttpResponse response = new HttpResponse();

    StatusLine statusLine = response.getStatusLine(HTTP_VERSION_1_1, HttpStatus.OK);

    List<ResponseHeaderLine> responseHeaderLines = new ArrayList<>();
    ResponseHeaderLine responseHeaderLine = response.getHeaderLine(HttpHeaders.CONTENT_TYPE.getHeader(), ContentType.PLAIN_TEXT.getType());
    responseHeaderLines.add(responseHeaderLine);

    responseHeaderLine = response.getHeaderLine(HttpHeaders.CONTENT_LENGTH.getHeader(), echoString.length());
    responseHeaderLines.add(responseHeaderLine);

    ResponseBodyLine responseBodyLine = response.getResponseBodyLine(echoString);

    response.setStatusLine(statusLine);
    response.setHeaderLines(responseHeaderLines);
    response.setBody(responseBodyLine);
    return response;
  }

  public void handleFiles(Socket finalSocket, HttpRequest parsedRequest, String directory)
      throws IOException {

    String fileName = parsedRequest.getRequestLine().getRequestTarget().substring(7);
    String filePath = directory + fileName;
    System.out.println(filePath);
    if (!FileHandler.fileExists(filePath)) {
      this.handle404(finalSocket);
    } else {
      try {
        var osStream = finalSocket.getOutputStream();
        HttpResponse response = new HttpResponse();
        StatusLine statusLine = response.getStatusLine(HTTP_VERSION_1_1, HttpStatus.OK);
        List<ResponseHeaderLine> responseHeaderLines = new ArrayList<>();
        ResponseHeaderLine responseHeaderLine = response.getHeaderLine(HttpHeaders.CONTENT_TYPE.getHeader(), ContentType.OCTET_STREAM.getType());
        responseHeaderLines.add(responseHeaderLine);

        File file = FileHandler.getFile(filePath);
        responseHeaderLine = response.getHeaderLine(HttpHeaders.CONTENT_LENGTH.getHeader(), file.length());
        responseHeaderLines.add(responseHeaderLine);

        response.setStatusLine(statusLine);
        response.setHeaderLines(responseHeaderLines);
        osStream.write(response.writeResponse().getBytes(StandardCharsets.UTF_8));
        osStream.flush();

        try(FileInputStream fis = new FileInputStream(file)){
          byte[] dataBuffer = new byte[2048];
          int bytesRead;
          while((bytesRead = fis.read(dataBuffer)) != -1) {
            osStream.write(dataBuffer, 0, bytesRead);
          }
        }
        osStream.flush();
      } catch (IOException e) {
        System.out.println("IOException: " + e.getMessage());
      }
    }



  }
}