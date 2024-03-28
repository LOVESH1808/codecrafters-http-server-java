import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
public class Main {
  public static void main(String[] args) {
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");
      InputStream inputStream = clientSocket.getInputStream();
      BufferedReader bufferedReader =
          new BufferedReader(new InputStreamReader(inputStream));
      String startLine = bufferedReader.readLine();
      String[] startLineArr = startLine.split(" ");
      if (startLineArr[1].equals("/")) {
        clientSocket.getOutputStream().write(
            ("HTTP/1.1 200 OK \r\n\r\n").getBytes(StandardCharsets.UTF_8));
      } else if (startLineArr[1].startsWith("/echo")) {
        String content = startLineArr[1].replace("/echo/", "");
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HTTP/1.1 200 OK \r\n");
        stringBuilder.append("Content-Type: text/plain\r\n");
        stringBuilder.append("Content-Length: ")
            .append(content.length())
            .append(" \r\n\r\n");
        stringBuilder.append(content).append("\r\n\r\n");
        clientSocket.getOutputStream().write(
            (stringBuilder.toString()).getBytes(StandardCharsets.UTF_8));
      } else {
        clientSocket.getOutputStream().write(
            ("HTTP/1.1 404 Not Found \r\n\r\n")
                .getBytes(StandardCharsets.UTF_8));
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}