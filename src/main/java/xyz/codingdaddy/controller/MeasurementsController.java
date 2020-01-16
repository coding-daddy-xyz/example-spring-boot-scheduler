package xyz.codingdaddy.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import xyz.codingdaddy.domain.Metric;

/**
 *
 */
@Controller
public class MeasurementsController {

  private Map<Metric, Double> maxValues = new HashMap<>();

  public void addValue(Metric metric, double value) {
    maxValues.put(metric, Math.max(maxValues.getOrDefault(metric, 0.0d), value));
  }

  public void reset() {
    maxValues.clear();
  }

  public void report() {
    maxValues.forEach((k, v) -> System.out.println("Maximum " + k + ": " + v));
  }
}
