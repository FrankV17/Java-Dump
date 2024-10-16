package basicio;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.*;
import basicio.LogManager.LogType;

public class LogSearcher {

    public void searchLogByPattern(String logDirectory, String pattern) throws IOException {
        File dir = new File(logDirectory);
        File[] files = dir.listFiles();
        Pattern regex = Pattern.compile(pattern);

        for (File file : files) {
            Matcher matcher = regex.matcher(file.getName());
            if (matcher.find()) {
                System.out.println("Found log: " + file.getName());
                displayLog(file);
            }
        }
    }

    // TODO To be complete
    public List<Path> searchLogListByPattern(String pattern) {
        List<Path> pathList = new ArrayList<>();
        return pathList;
    }

    // TODO To be complete
    public List<Path> searchLogListByPattern(String pattern, LogType logType) {
        List<Path> pathList = new ArrayList<>();
        return pathList;
    }

    // Method with exception chaining
    private void displayLog(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            // Chaining exception: File not found with cause
            throw new IOException("File not found: " + file.getName(), e);
        } catch (IOException e) {
            // Chaining exception: IO error during log display
            throw new IOException("Error reading log file: " + file.getName(), e);
        }
    }
}
