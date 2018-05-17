package band.pairs.strategies.graph;

import band.pairs.strategies.BandTrackingStrategy;

import java.util.List;

public class GraphStrategy implements BandTrackingStrategy {

    private final Graph graph = new Graph();

    @Override
    public void addBandList(List<String> bands) {
        for (int i = 0; i < bands.size(); i++) {
            for (int j = i + 1; j < bands.size(); j++) {
                graph.incrementEdgeWeight(bands.get(i), bands.get(j));
            }
        }
    }

    @Override
    public void visitBands(BandPairVisitor visitor) {
        graph.walkEdges(visitor);
    }
}
