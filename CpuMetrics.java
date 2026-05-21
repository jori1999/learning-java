package Supatop;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.stream.Stream;

public class CpuMetrics {
    public static void printSystemCpu() {
        Path path = Path.of("/proc/stat");
        try (Stream<String> lines = Files.lines(path)) {
            String cpuLine = lines.findFirst().orElse("");
            String[] tokens = cpuLine.split("\\s+");
            if (tokens.length > 5) {
                long user = Long.parseLong(tokens[1]);
                long nice = Long.parseLong(tokens[2]);
                long system = Long.parseLong(tokens[3]);
                long idle = Long.parseLong(tokens[4]);
                System.out.printf("CPU -> User: %d | System: %d | Idle: %d%n", user, system, idle);
            }
        } catch (IOException e) {
            System.err.println("Kon /proc/stat niet lezen: " + e.getMessage());
        }
    }
}
