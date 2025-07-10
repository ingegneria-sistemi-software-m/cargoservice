package main.java;

import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class CargoRobotConfigLoader {

    private int Step_len;
    private Map<String, int[]> positions;
    private Map<String, String> directions;

    // Getters
    public int getStepLen() {
        return Step_len;
    }

    public Map<String, int[]> getPositions() {
        return positions;
    }

    public Map<String, String> getDirections() {
        return directions;
    }

    // Metodo statico per caricare i dati dal file JSON
    public static CargoRobotConfigLoader loadFromFile(String filePath) throws IOException {
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            return gson.fromJson(reader, CargoRobotConfigLoader.class);
        }
    }

    // Test main
    public static void main(String[] args) {
        try {
        	String currentDir = System.getProperty("user.dir");
            System.out.println("Directory corrente: " + currentDir);
            
            CargoRobotConfigLoader config = CargoRobotConfigLoader.loadFromFile("./src/main/resources/cargorobot_conf.json");

            System.out.println("Step_len: " + config.getStepLen());

            System.out.println("\nPositions:");
            for (Map.Entry<String, int[]> entry : config.getPositions().entrySet()) {
                int[] coords = entry.getValue();
                System.out.printf("  %s -> [%d, %d]%n", entry.getKey(), coords[0], coords[1]);
            }

            System.out.println("\nDirections:");
            for (Map.Entry<String, String> entry : config.getDirections().entrySet()) {
                System.out.printf("  %s -> %s%n", entry.getKey(), entry.getValue());
            }

        } catch (IOException e) {
            System.err.println("Errore nella lettura del file: " + e.getMessage());
        }
    }
}
