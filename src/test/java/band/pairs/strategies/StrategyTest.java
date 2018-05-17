package band.pairs.strategies;

import band.pairs.strategies.bloom.BloomFilterStrategy;
import band.pairs.strategies.brute.BruteForceStrategy;
import band.pairs.strategies.graph.GraphStrategy;
import com.google.common.collect.Lists;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(Parameterized.class)
public class StrategyTest {

    private static final String BAND1 = "BAND1";
    private static final String BAND2 = "BAND2";
    private static final String BAND3 = "BAND3";
    private static final String BAND4 = "BAND4";

    @Parameterized.Parameters
    public static ArrayList<Class<? extends BandTrackingStrategy>> strategies() {
        return Lists.newArrayList(BruteForceStrategy.class, GraphStrategy.class/*, BloomFilterStrategy.class*/);
    }

    @Parameterized.Parameter
    public Class<? extends BandTrackingStrategy> strategyClass;

    @Test
    public void testStrategy() throws Exception {
        BandTrackingStrategy strategy = strategyClass.newInstance();

        List<List<String>> bandsWith50 = Collections.nCopies(50, Lists.newArrayList(BAND1, BAND2));
        List<List<String>> bandsWith90 = Collections.nCopies(90, Lists.newArrayList(BAND1, BAND3));
        List<List<String>> bandsWith10 = Collections.nCopies(10, Lists.newArrayList(BAND2, BAND4));

        bandsWith10.stream().forEach(strategy::addBandList);
        bandsWith90.stream().forEach(strategy::addBandList);
        bandsWith50.stream().forEach(strategy::addBandList);

        strategy.visitBands((one, two, count) -> {
            if (one.equals(BAND1) && two.equals(BAND2)) {
                Assertions.assertThat(count).isEqualTo(50);
            } else if (one.equals(BAND1) && two.equals(BAND3)) {
                Assertions.assertThat(count).isEqualTo(90);
            } else if (one.equals(BAND2) && two.equals(BAND4)) {
                Assertions.assertThat(count).isEqualTo(10);
            } else {
                Assertions.assertThat(count).isEqualTo(0);
            }
        });

    }
}
