public class DirectoryStore {
    private static String directory;
    public static String getDirectory() { return directory; }
    public static void setDirectory(String directory) {
        System.out.println(directory);
      DirectoryStore.directory = directory;
  
    }
  }