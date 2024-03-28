
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    static String directoryPath = "";
  public static void main(String[] args) {
      for (int i = 0; i < args.length; i++ ) {
          if ("--directory".equals(args[i]) && i + 1 < args.length) {
              directoryPath = args[i + 1];
              break;
          }
      }

    System.out.println("Logs from your program will appear here!");

     ServerSocket serverSocket = null;
     Socket clientSocket = null;

     try {
       serverSocket = new ServerSocket(4221);
       System.out.println("Listening fro connections on port 4221...");

       // forever loop to keep the server running
       while (true) {
         serverSocket.setReuseAddress(true);
         clientSocket = serverSocket.accept(); // Wait for connection from client.
         System.out.println("accepted new connection from " + clientSocket);
         ClientHandler clientHandler = new ClientHandler(clientSocket);
         new Thread(clientHandler).start();
       }
     } catch (IOException e) {
       System.out.println("IOException: " + e.getMessage());
     }
  }
}