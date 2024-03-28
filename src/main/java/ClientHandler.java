import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static final String basePath = Main.directoryPath;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             OutputStream out = clientSocket.getOutputStream()) {

            String line;
            StringBuilder requestBuilder = new StringBuilder();
            while (!(line = in.readLine()).isEmpty()) {
                requestBuilder.append(line + "\n");
            }
            String request = requestBuilder.toString();
            System.out.println("Request from client:\n"+ request);

            String[] requestLines = request.split("\n");
            System.out.println("-------> "+requestLines[0]);

            String startLine = requestLines[0];
            String[] splitStartLine = startLine.split(" ");
            String path = splitStartLine[1];
            System.out.println("-------> " + path);

            String httpResponse="";
            // Handling POST request
            if (startLine.startsWith("POST /files/") && path.startsWith("/files/")) {
                String filename = path.substring(7);
                Path filePath = Paths.get(basePath, filename).normalize();

                if(!filePath.startsWith(basePath)) {
                    String response = "HTTP/1.1 403 Forbidden\r\n\r\n";
                    out.write(response.getBytes(StandardCharsets.UTF_8));
                } else {
                    // Prepare to read the request body (file content)
                    StringBuilder payload = new StringBuilder();
                    while(in.ready()) {
                        payload.append((char) in.read());
                    }
                    String content = payload.toString();

                    // Saving the file content
                    Files.write(filePath, content.getBytes(StandardCharsets.UTF_8));

                    // Respond with 201 Created
                    String response = "HTTP/1.1 201 Created\r\n\r\n";
                    out.write(response.getBytes(StandardCharsets.UTF_8));
                }
            }
            
            if (path.startsWith("/files/")) {
                String filename = path.substring(7);
                Path filePath = Paths.get(basePath, filename).normalize();

                //Security check to prevent path traversal attacks
                if(!filePath.startsWith(basePath)) {
                    String response = "HTTP/1.1 403 Forbidden\r\n\r\n";
                    out.write(response.getBytes(StandardCharsets.UTF_8));

                } else if (Files.exists(filePath)) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    String header = "HTTP/1.1 200 OK\r\n" +
                            "Content-Type: application/octet-stream\r\n" +
                            "Content-Length: " + fileContent.length + "\r\n\r\n";
                    out.write(header.getBytes(StandardCharsets.UTF_8));
                    out.write(fileContent);

                } else {
                    String response = "HTTP/1.1 404 Not Found\r\n\r\n";
                    out.write(response.getBytes(StandardCharsets.UTF_8));
                }

            }

            if(path.equals("/")){
                httpResponse = "HTTP/1.1 200 OK\r\n\r\n";
            } else if (path.startsWith("/echo/")) {
                String responseString = path.substring(6);
                System.out.println(responseString);
                httpResponse = "HTTP/1.1 200 OK\r\n"+
                        "Content-Type: text/plain\r\n"+
                        "Content-Length: " +responseString.getBytes(StandardCharsets.UTF_8).length+ "\r\n"+
                        "\r\n"+
                        responseString;
                System.out.println(httpResponse);
            } else if (path.startsWith("/user-agent")) {
                String header = requestLines[2];
                System.out.println("Header  " + header);
                String[] values = header.split(": ");
                String value = values[1];
                httpResponse = "HTTP/1.1 200 OK\r\n"+
                        "Content-Type: text/plain\r\n"+
                        "Content-Length: " +value.getBytes(StandardCharsets.UTF_8).length+ "\r\n"+
                        "\r\n"+
                        value;
                System.out.println(value);

            } else {
                httpResponse = "HTTP/1.1 404 Not Found\r\n\r\n";
            }
            OutputStream outputStream = clientSocket.getOutputStream();
            outputStream.write(httpResponse.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            in.close();
            outputStream.close();
            clientSocket.close();
        } catch (IOException e) {
            System.out.println("IOException in client handler: " + e.getMessage());
        }
    }
}