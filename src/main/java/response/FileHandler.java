package response;

import java.io.File;

public class FileHandler {
  public static boolean fileExists(String filePath) {
      File file = new File(filePath);
      return file.exists();
  }

  public static File getFile(String filePath) {
    return new File(filePath);
  }
}