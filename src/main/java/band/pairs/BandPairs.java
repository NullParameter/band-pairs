package band.pairs;

import band.pairs.strategies.BandTrackingStrategy;
import band.pairs.strategies.bloom.BloomFilterStrategy;
import band.pairs.strategies.brute.BruteForceStrategy;
import band.pairs.strategies.graph.GraphStrategy;

import java.io.*;
import java.util.*;

public class BandPairs {

    private final BandTrackingStrategy strategy;

    public BandPairs(BandTrackingStrategy strategy) {
        this.strategy = strategy;
    }

    public void run(InputStream inputStream, OutputStream outputStream, int occurenceLimit) {

        try (BufferedReader input = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = input.readLine()) != null) {
                List<String> bands = Arrays.asList(line.split(","));
                strategy.addBandList(bands);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading from input.");
        }

        try (PrintWriter output = new PrintWriter(outputStream)) {
            strategy.visitBands((firstBand, secondBand, occurrences) -> {
               if (occurrences >= occurenceLimit) {
                   output.println(String.format("%s,%s", firstBand, secondBand));
               }
            });
        }
    }

    public static void main(String... args) {
        final String inputFilePath = args[0];
        final String outputFilePath = args[1];
        final String strategyName = args[2].toUpperCase();
        final int limit = Integer.parseInt(args[3]);

        final File inputFile = new File(inputFilePath);

        if (!inputFile.exists()) {
            System.err.println("Specified input file doesn't exist.");
            System.exit(1);
        }

        final BandTrackingStrategy strategy;
        switch(strategyName) {
            case "GRAPH":
                strategy = new GraphStrategy();
                break;
            case "BLOOM":
                strategy = new BloomFilterStrategy();
                break;
            case "BRUTE":
            default:
                strategy = new BruteForceStrategy();
                break;

        }

        final File outputFile = new File(outputFilePath);
        try (
                FileInputStream input = new FileInputStream(inputFile);
                FileOutputStream output = new FileOutputStream(outputFile);
        ) {
            new BandPairs(strategy).run(input, output, limit);
        } catch (IOException e) {
            System.err.println("Error writing to output file.");
            System.exit(3);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(4);
        }
    }
}
