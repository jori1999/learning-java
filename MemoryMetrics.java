package Supatop;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;
import java.util.stream.Stream;

public class MemoryMetrics {
    public static String getMemoryString() {
        Path path = Path.of("/proc/meminfo");
        StringBuilder sb = new StringBuilder();
        try (Stream<String> lines = Files.lines(path)) {
            lines.filter(line -> line.startsWith("MemTotal:") || line.startsWith("MemAvailable:"))
                 .forEach(line -> {
                     String[] parts = line.split(":\\s+");
                     if (parts.length == 2) {

                         sb.append(String.format("%-15s %-20s%n", parts[0] + ":", parts[1]));
                     }
                 });
        } catch (IOException e) {
            sb.append("Geheugen: Niet beschikbaar\n");
        }
        return sb.toString();
    }
}
