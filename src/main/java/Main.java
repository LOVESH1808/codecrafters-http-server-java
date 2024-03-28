import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Main {
  public static void main(String[] args) {
    // You can use print statements as follows for debugging, they'll be visible when running tests.
    System.out.println("Logs from your program will appear here!");

    // Uncomment this block to pass the first stage
    //
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      clientSocket = serverSocket.accept(); // Wait for connection from client.
      System.out.println("accepted new connection");
      InputStream inputStream = clientSocket.getInputStream();
      BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
      String startLine = bufferedReader.readLine();
      String startLineArr[] = startLine.split(" ");
      for(String s : startLineArr) {
        System.out.println(s);
      }
      if(startLineArr[1].equals("/")) {
        clientSocket.getOutputStream().write(
        "HTTP/1.1 200 OK\r\n\r\n".getBytes(StandardCharsets.UTF_8)
      );
      } else {
        clientSocket.getOutputStream().write(
          "HTTP/1.1 404 NOT FOUND \r\n\r\n".getBytes(StandardCharsets.UTF_8)
        );
      }
      
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}
