package basicio;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Random;
import basicio.LogManager.LogType;

public class DataExchange {
    private final static LogManager chargingStationLogManager = new LogManager(LogType.CHARGING_STATION);
    
    public static Path sendSensorData(String targetFile) {
        Path targetPath = Paths.get(targetFile);
        
        if (!Files.exists(targetPath.getParent())) {
            try {
                Files.createDirectories(targetPath.getParent());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        
        try (BufferedOutputStream out = new BufferedOutputStream(Files.newOutputStream(targetPath))) {
            byte[] sensorData = generateSensorData(256);
            out.write(sensorData);
            System.out.println("Sensor data sent to " + targetPath.getFileName());
        } catch (IOException e) {
            System.err.println("Error during sending sensor data: " + e.getMessage());
        }
        
        return targetPath;
    }
    
    public static void receiveSensorData(String sourceFile) throws IOException {
        Path sourceFilePath = Paths.get(sourceFile);
        
        if (!Files.exists(sourceFilePath)) {
            throw new FileNotFoundException("The source file was not found!");
        }
        
        if (!Files.isRegularFile(sourceFilePath)) {
            throw new IOException("The source file is not a regular file!");
        }
        
        try (BufferedInputStream in = new BufferedInputStream(Files.newInputStream(sourceFilePath))) {
            byte[] receivedData = in.readAllBytes();
            processSensorData(receivedData);
            System.out.println("Sensor data received from " + sourceFilePath.getFileName());
        } catch (FileNotFoundException e) {
            // Chaining exception with a meaningful message
            throw new IOException("Error while receiving sensor data: source file not found.", e);
        } catch (IOException e) {
            // Chaining exception with the original cause
            throw new IOException("Error reading sensor data from " + sourceFilePath.getFileName(), e);
        }
    }
    
    private static byte[] generateSensorData(int length) {
        byte[] data = new byte[length];
        new Random().nextBytes(data);
        return data;
    }
    
    private static void processSensorData(byte[] data) {
        System.out.println("Processing sensor data of size: " + data.length + " bytes");
    }
    
    public static Path writeChargingStationLog(String chargingStationName, String message) {
        return chargingStationLogManager.addContentToLog(chargingStationName, message);
    }
    
    public static void readChargingStationLog(Path logPath) throws IOException {
        if (!Files.exists(logPath)) {
            throw new FileNotFoundException(logPath + " was not found!");
        }
        
        if (!Files.isRegularFile(logPath)) {
            throw new IOException(logPath + " is not a regular file!");
        }
        
        try (BufferedReader reader = Files.newBufferedReader(logPath)) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (FileNotFoundException e) {
            // Chaining exception with additional context
            throw new IOException("Error while reading charging station log: file not found.", e);
        } catch (IOException e) {
            // Chaining exception with the original cause
            throw new IOException("Error reading charging station log from " + logPath.getFileName(), e);
        }
    }
}
