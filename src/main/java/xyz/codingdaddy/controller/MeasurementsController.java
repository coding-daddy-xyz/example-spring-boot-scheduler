package xyz.codingdaddy.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import xyz.codingdaddy.domain.Metric;

import java.util.DoubleSummaryStatistics;
import java.util.HashMap;
import java.util.Map;

/**
 * Manages the statistics for provided measurements
 *
 * @author serhiy
 */
@Controller
public class MeasurementsController {
    private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementsController.class);

    private Map<Metric, DoubleSummaryStatistics> maxValues = new HashMap<>();

    public void addValue(Metric metric, double value) {
        maxValues.computeIfAbsent(metric, v -> new DoubleSummaryStatistics()).accept(value);
    }

    public void reset() {
        maxValues.clear();
    }

    public void report() {
        maxValues.forEach((k, v) -> LOGGER.info("'{}' - AVG: {}, MIN: {}, MAX: {}", k, v.getAverage(), v.getMin(), v.getMax()));
    }
}
