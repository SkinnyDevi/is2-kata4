package software.ulpgc.kata4.view;

import software.ulpgc.kata4.model.HistogramDataField;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapHistogramDataFieldBuilder implements HistogramDataFieldBuilder{
    private final Map<String, Integer> statistics;

    public MapHistogramDataFieldBuilder(Map<String, Integer> statistics) {
        this.statistics = statistics;
    }

    @Override
    public List<HistogramDataField> build() {
        List<HistogramDataField> fields = new ArrayList<>();
        for (String entry: this.statistics.keySet())
            fields.add(new HistogramDataField(entry, statistics.get(entry)));

        return fields;
    }
}