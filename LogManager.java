package basicio;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import io.github.cdimascio.dotenv.Dotenv;

public class LogManager {
    private static final Dotenv dotenv = Dotenv.load();

    public static enum LogType {
        CHARGING_STATION,
        ENERGY_SOURCE,
        WHOLE_SYSTEM,
        DEFAULT,
        ARCHIVE
    }

    public final static Map<LogType, String> logTypeDirMap = Map.of(
        LogType.CHARGING_STATION, dotenv.get("LOG_DIR_CHARGING_STATION"),
        LogType.ENERGY_SOURCE, dotenv.get("LOG_DIR_CHARGING_STATION"),
        LogType.WHOLE_SYSTEM, dotenv.get("LOG_DIR_CHARGING_STATION"),
        LogType.DEFAULT, dotenv.get("LOG_DIR_CHARGING_STATION"),
        LogType.ARCHIVE, dotenv.get("LOG_DIR_ARCHIVE")
    );

    private final Path logDirPath;

    public LogManager(LogType logType) {
        logDirPath = Paths.get(logTypeDirMap.get(logType));

        if (!Files.exists(logDirPath) || !Files.isDirectory(logDirPath)) {
            try {
                Files.createDirectories(logDirPath);
                System.out.format("Log manager %s has created!\n", logType.toString());
            } catch (IOException e) {
                throw new RuntimeException("Failed to create log directory for " + logType, e);
            }
        }
    }

    public Path addContentToLog(String equipmentName, String content) {
        String logName = generateLogName(equipmentName);
        Path logFilePath = logDirPath.resolve(logName);

        try (BufferedWriter writer = Files.newBufferedWriter(logFilePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            writer.write(content);
            return logFilePath;
        } catch (IOException e) {
            throw new IOException("Error adding content to log file: " + logName, e);
        }
    }

    public void moveLog(Path logPath, Path targetDirPath) throws IOException {
        if (!Files.exists(logPath)) {
            throw new FileNotFoundException("The log file does not exist!");
        }

        if (!Files.exists(targetDirPath)) {
            try {
                Files.createDirectories(targetDirPath);
            } catch (IOException e) {
                throw new IOException("Error creating target directory for moving log file", e);
            }
        }

        try {
            Files.move(logPath, targetDirPath.resolve(logPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Error moving log file: " + logPath.getFileName(), e);
        }
    }

    public void deleteLog(Path logPath) throws IOException {
        try {
            Files.deleteIfExists(logPath);
        } catch (IOException e) {
            throw new IOException("Error deleting log file: " + logPath.getFileName(), e);
        }
    }

    public void archiveLog(Path logPath) throws IOException {
        if (!Files.exists(logPath)) {
            throw new FileNotFoundException("The log file does not exist!");
        }

        Path archivePath = Paths.get(logTypeDirMap.get(LogType.ARCHIVE));

        if (!Files.exists(archivePath)) {
            try {
                Files.createDirectories(archivePath);
            } catch (IOException e) {
                throw new IOException("Error creating archive directory", e);
            }
        }

        try {
            Files.move(logPath, archivePath.resolve(logPath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Error archiving log file: " + logPath.getFileName(), e);
        }
    }

    public static String generateLogName(String equipmentName) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        return equipmentName + "_" + date + ".log";
    }
}
