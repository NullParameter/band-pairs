package band.pairs.strategies;

import java.util.List;

/**
 * Track lists of bands and their occurrences together.
 */
public interface BandTrackingStrategy {

    void addBandList(List<String> bands);

    void visitBands(BandPairVisitor visitor);

    interface BandPairVisitor {
        void visit(String band1, String band2, int count);
    }
}
