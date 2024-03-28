import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class ConnectionHandler implements Runnable {
  Socket clientSocket;
  public ConnectionHandler(Socket clientSocket) {
    this.clientSocket = clientSocket;
  }
  public void run() {
    try {
      InputStream inputStream = clientSocket.getInputStream();
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(inputStream));
      String startLine = bufferedReader.readLine();
      String[] startLineArr = startLine.split(" ");
      String output;
      if (startLineArr[0].equals("GET")) {
        if (startLineArr[1].equals("/")) {
          output = "HTTP/1.1 200 OK \r\n\r\n";
        } else if (startLineArr[1].startsWith("/echo")) {
          String content = startLineArr[1].replace("/echo/", "");
          output = "HTTP/1.1 200 OK \r\n"
                   + "Content-Type: text/plain\r\n"
                   + "Content-Length: " + content.length() + " \r\n\r\n" +
                   content + "\r\n\r\n";
        } else if (startLineArr[1].equals("/user-agent")) {
          String userAgentHeader = "";
          while ((userAgentHeader = bufferedReader.readLine()) != null) {
            if (userAgentHeader.startsWith("User-Agent")) {
              break;
            }
          }
          assert userAgentHeader != null;
          String[] userAgentHeaderArr = userAgentHeader.split(" ");
          output = "HTTP/1.1 200 OK \r\n"
                   + "Content-Type: text/plain\r\n"
                   + "Content-Length: " + userAgentHeaderArr[1].length() +
                   " \r\n\r\n" + userAgentHeaderArr[1] + "\r\n\r\n";
        } else {
          output = "HTTP/1.1 404 Not Found \r\n\r\n";
        }
        clientSocket.getOutputStream().write(
            (output).getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }

  }
}