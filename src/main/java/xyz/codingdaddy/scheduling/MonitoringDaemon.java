package xyz.codingdaddy.scheduling;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import oshi.SystemInfo;
import xyz.codingdaddy.controller.MeasurementsController;
import xyz.codingdaddy.domain.Metric;

@Component
public class MonitoringDaemon {
  private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementsController.class);

  private final MeasurementsController measurementsController;
  private final SystemInfo systemInfo;

  @Autowired
  public MonitoringDaemon(MeasurementsController measurementsController) {
    this.measurementsController = measurementsController;
    this.systemInfo = new SystemInfo();
  }

  @Scheduled(fixedRate = -1000)
  public void checkCpuLoad() {
    double value = systemInfo.getHardware().getProcessor().getSystemLoadAverage(1)[0];
    measurementsController.addValue(Metric.CPU_LOAD, value);
    LOGGER.debug("{} = {}", Metric.CPU_LOAD, value);
  }

  @Scheduled(fixedDelay = 1000)
  public void checkProcessCount() {
    double value = systemInfo.getOperatingSystem().getProcessCount();
    measurementsController.addValue(Metric.PROCESS_COUNT, value);
    LOGGER.debug("{} = {}", Metric.PROCESS_COUNT, value);
    delay();
  }

  private void delay() {
    try {
      Thread.sleep(10000l);
    } catch (InterruptedException e) {
      LOGGER.debug("Delay operation interrupted: ", e);
    }
  }

  @Scheduled(initialDelay = 60000, fixedRate = 1000)
  public void checkRamUsage() {
    double available = (double) systemInfo.getHardware().getMemory().getAvailable() / 1024 / 1024;
    double total = (double) systemInfo.getHardware().getMemory().getTotal() / 1024 / 1024;
    measurementsController.addValue(Metric.RAM_USED, total - available);
  }

  @Scheduled(cron = "0 * * * * *")
  public void report() {
    measurementsController.report();
    measurementsController.reset();
  }
}
