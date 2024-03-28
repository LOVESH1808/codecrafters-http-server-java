import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
public class Main {
  static String directoryPath = "";
  public static void main(String[] args) {
    for (int i = 0; i < args.length; i++) {
      if ("--directory".equals(args[i]) && i + 1 < args.length) {
        directoryPath = args[i + 1];
        break;
      }
    }
    ServerSocket serverSocket = null;
    Socket clientSocket = null;
    try {
      serverSocket = new ServerSocket(4221);
      serverSocket.setReuseAddress(true);
      while (true) {
        clientSocket =
            serverSocket.accept(); // Wait for connection from client.
        System.out.println("accepted new connection");
        ConnectionHandler connectionHandler =
            new ConnectionHandler(clientSocket);
        new Thread(connectionHandler).start();
      }
    } catch (IOException e) {
      System.out.println("IOException: " + e.getMessage());
    }
  }
}