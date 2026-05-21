package Supatop;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Supatop {

    public static void main(String[] args) {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        System.out.println("Starting Supatop... Press Ctrl+C to quit.\n");

        executor.scheduleAtFixedRate(() -> {
            try {

                StringBuilder buffer = new StringBuilder();

                buffer.append("\033[H\033[2J");

                buffer.append("==================================================================\n");
                buffer.append("  SUPATOP - Realtime System Status                               \n");
                buffer.append("==================================================================\n");


                buffer.append(MemoryMetrics.getMemoryString());

                buffer.append("------------------------------------------------------------------\n");
                buffer.append(String.format("%-7s %-20s %-5s %-10s %-10s%n", "PID", "COMMAND", "STATE", "UTIME", "STIME"));
                buffer.append("------------------------------------------------------------------\n");


                List<String> pids = getActivePids();


                pids.stream()
                    .limit(15)
                    .forEach(pid -> {
                        String stats = ProcessMetrics.getProcessStatsString(pid);
                        if (!stats.isEmpty()) {
                            buffer.append(stats);
                        }
                    });


                System.out.print(buffer.toString());
                System.out.flush();

            } catch (Exception e) {
                // quiet catch for glitches during render looping
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private static List<String> getActivePids() {
        try (Stream<Path> stream = Files.list(Path.of("/proc"))) {
            return stream
                    .map(Path::getFileName)
                    .map(Path::toString)
                    .filter(name -> name.matches("\\d+"))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return List.of();
        }
    }
}
