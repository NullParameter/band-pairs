package band.pairs.strategies.graph;

import band.pairs.strategies.BandTrackingStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * A super simple implementation of an undirected graph, where each edge represents the occurrences of that pair of bands.
 */
public class Graph {

    private final Map<String, Map<String, Integer>> nodeEdges = new HashMap<>();

    public void incrementEdgeWeight(String node1, String node2) {
        // Sort the two graph nodes in alphabetical order, so that we can consistently update the correct edge.
        final String first, second;
        if (node1.compareTo(node2) < 0) {
            first = node1;
            second = node2;
        } else {
            first = node2;
            second = node1;
        }

        // Make sure that the second map is populated
        Map<String, Integer> edgeWeight = nodeEdges.computeIfAbsent(first, k -> new HashMap<>());

        // Increment the weight for this edge, or get it started.
        edgeWeight.compute(second, (k, count) -> count == null ? 1 : count + 1);
    }

    public void walkEdges(BandTrackingStrategy.BandPairVisitor visitor) {
        nodeEdges.entrySet().stream().forEach(node1 ->
            node1.getValue().entrySet().stream().forEach(node2 ->
                visitor.visit(node1.getKey(), node2.getKey(), node2.getValue())
            )
        );
    }
}
