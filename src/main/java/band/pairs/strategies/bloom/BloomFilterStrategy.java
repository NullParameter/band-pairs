package band.pairs.strategies.bloom;

import band.pairs.strategies.BandTrackingStrategy;
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BloomFilterStrategy implements BandTrackingStrategy {

    private final Set<String> allBands = new HashSet<>();
    private final List<BloomFilter> filters = new LinkedList<>();

    @Override
    public void addBandList(List<String> bands) {
        allBands.addAll(bands);

        BloomFilter<CharSequence> filter = BloomFilter.create(Funnels.stringFunnel(Charset.defaultCharset()), 50, 0.01);
        bands.stream().forEach(filter::put);
        filters.add(filter);
    }

    @Override
    public void visitBands(BandPairVisitor visitor) {
        for(String band1: allBands) {
            for(String band2: allBands) {
                int count = 0;
                for(BloomFilter filter: filters) {
                    if (filter.mightContain(band1) && filter.mightContain(band2)) {
                        count++;
                    }
                }
                visitor.visit(band1, band2, count);
            }
        }
    }
}
