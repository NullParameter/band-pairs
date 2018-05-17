package band.pairs.strategies.brute;

import band.pairs.strategies.BandTrackingStrategy;

import java.util.*;

/**
 * Manually pair each band with each other band in a list, concatenate the strings,
 * and track an overall count of how many times that pair shows up.
 */
public class BruteForceStrategy implements BandTrackingStrategy {

    private final Map<String, Integer> bandPairCounts = new HashMap<>();

    @Override
    public void addBandList(List<String> bands) {
        List<String> bandsSorted = new ArrayList<>(bands);
        Collections.sort(bandsSorted);

        for (int i = 0; i < bandsSorted.size(); i++) {
            for (int j = i + 1; j < bandsSorted.size(); j++) {
                String bandPair = bandsSorted.get(i) + "," + bandsSorted.get(j);
                bandPairCounts.compute(bandPair, (pair, count) -> count == null ? 1 : count + 1);
            }
        }
    }

    @Override
    public void visitBands(BandPairVisitor visitor) {
        bandPairCounts.entrySet().stream().forEach(e -> {
            // This was much more elegant when it wasn't abstracted away.
            // We could refactor the key to be a Guava Pair, so that we don't have to re-split here.
            String[] bands = e.getKey().split(",");
            visitor.visit(bands[0], bands[1], e.getValue());
        });
    }
}
