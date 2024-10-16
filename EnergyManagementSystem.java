package basicio;

import java.io.IOException;
import java.nio.file.Path;

public class EnergyManagementSystem {
    public static void main(String[] args) throws IOException {
        LogManager logManager = new LogManager(LogManager.LogType.CHARGING_STATION);
        String equipmentName = "ChargingStation1";

        // Create log for a specific equipment
        logManager.addContentToLog(equipmentName, "Charging session started.\n");

        // Simulate sending and receiving sensor data
        Path sensorData = DataExchange.sendSensorData("temp_data/sensor.temp");
        try {
            DataExchange.receiveSensorData(sensorData.toString());
        } catch (IOException e) {
            // Chain exception with additional context about sensor data reception
            throw new IOException("Error while receiving sensor data from " + sensorData.getFileName(), e);
        }

        // Log data for charging station
        Path chargingStationLogPath = DataExchange.writeChargingStationLog(equipmentName, "Charging session started.\n");
        try {
            DataExchange.readChargingStationLog(chargingStationLogPath);
        } catch (IOException e) {
            // Chain exception with more context when reading the charging station log
            throw new IOException("Error while reading the charging station log from " + chargingStationLogPath.getFileName(), e);
        }

        // Search for logs by name or date using regex
        LogSearcher searcher = new LogSearcher();
        try {
            searcher.searchLogByPattern("logs/charging_station", "ChargingStation1_\\d{4}-\\d{2}-\\d{2}");
        } catch (IOException e) {
            // Chain exception when searching logs
            throw new IOException("Error during log search in logs/charging_station", e);
        }
    }
}
