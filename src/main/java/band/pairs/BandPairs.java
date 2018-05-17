package band.pairs;

import java.io.*;
import java.util.*;

public class BandPairs {

    public static void main(String... args) {
        final String inputFilePath = args[0];
        final String outputFilePath = args[1];

        final File inputFile = new File(inputFilePath);

        final Map<String, Integer> bandPairCounts = new HashMap<>();

        try (BufferedReader input = new BufferedReader(new FileReader(inputFile))) {
            String line;
            while((line = input.readLine()) != null) {
                List<String> bands = Arrays.asList(line.split(","));
                Collections.sort(bands);

                for (int i = 0; i < bands.size(); i++) {
                    for (int j = i + 1; j < bands.size(); j++) {
                        String bandPair = bands.get(i) + "," + bands.get(j);
                        bandPairCounts.compute(bandPair, (pair, count) -> count == null ? 1 : count + 1);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            System.exit(1);
        } catch (IOException e) {
            System.exit(2);
        }

        try (PrintWriter output = new PrintWriter(new FileWriter(outputFilePath))) {
            bandPairCounts.entrySet().stream()
                    .filter(e -> e.getValue() >= 50)
                    .map(Map.Entry::getKey)
                    .forEach(bands -> output.println(bands));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
