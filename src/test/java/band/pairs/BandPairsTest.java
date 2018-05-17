package band.pairs;

import band.pairs.strategies.BandTrackingStrategy;
import org.apache.commons.io.IOUtils;
import org.assertj.core.util.Lists;
import org.junit.Test;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class BandPairsTest {

    private final List<String> lines = Lists.newArrayList(
      "BAND1,BAND2,BAND3",
            "BAND2,BAND3,BAND4",
            "BAND1,BAND3,BAND4",
            "BAND3,BAND1,BAND4"
    );

    @Test
    public void testRun() {
        BandTrackingStrategy strategy = mock(BandTrackingStrategy.class);
        InputStream input = IOUtils.toInputStream(lines.stream().collect(Collectors.joining("\n")));
        OutputStream output = mock(OutputStream.class);

        BandPairs bandPairs = new BandPairs(strategy);
        bandPairs.run(input, output, 2);

        verify(strategy).addBandList(Lists.newArrayList("BAND1", "BAND2", "BAND3"));
        verify(strategy).addBandList(Lists.newArrayList("BAND2", "BAND3", "BAND4"));
        verify(strategy).addBandList(Lists.newArrayList("BAND1", "BAND3", "BAND4"));
        verify(strategy).addBandList(Lists.newArrayList("BAND3", "BAND1", "BAND4"));
        verify(strategy).visitBands(any());
    }
}
