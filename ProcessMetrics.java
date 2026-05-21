package Supatop;

import java.nio.file.Files;
import java.nio.file.Path;

public class ProcessMetrics {
    public static String getProcessStatsString(String pid) {
        Path path = Path.of("/proc/" + pid + "/stat");
        try {
            String line = Files.readAllLines(path).get(0);

            int closingParenthesis = line.lastIndexOf(')');
            if (closingParenthesis == -1) return "";

            String processName = line.substring(line.indexOf('(') + 1, closingParenthesis);
            String restOfLine = line.substring(closingParenthesis + 2);
            String[] tokens = restOfLine.split(" ");

            String state = tokens[0];
            long utime = Long.parseLong(tokens[11]);
            long stime = Long.parseLong(tokens[12]);


            return String.format("%-7s %-20.20s %-5s %-10d %-10d%n", pid, processName, state, utime, stime);
        } catch (Exception e) {
            return "";
        }
    }
}
