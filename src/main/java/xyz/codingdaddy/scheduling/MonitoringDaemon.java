package xyz.codingdaddy.scheduling;

import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xyz.codingdaddy.controller.MeasurementsController;
import xyz.codingdaddy.domain.Metric;
import xyz.codingdaddy.service.HostStatusService;

@Component
public class MonitoringDaemon {
  private static final Logger LOGGER = LoggerFactory.getLogger(MonitoringDaemon.class);

  private HostStatusService hostStatusService;
  private MeasurementsController measurementsController;

  @Autowired
  public MonitoringDaemon(HostStatusService hostStatusService, MeasurementsController measurementsController) {
    this.hostStatusService = hostStatusService;
    this.measurementsController = measurementsController;
  }

  @Scheduled(fixedRate = 1000)
  public void checkCpuLoad() {
    double value = hostStatusService.getCpuLoad();
    measurementsController.addValue(Metric.CPU_LOAD, value);
    LOGGER.info("{} = {}", Metric.CPU_LOAD, value);
  }

  @Scheduled(fixedDelay = 1000)
  public void checkDiskSpaceAvailable() {
    double value = hostStatusService.getDiskSpaceAvailable();
    measurementsController.addValue(Metric.DISK_SPACE_AVAILABLE, value);
    LOGGER.info("{} = {}", Metric.DISK_SPACE_AVAILABLE, value);
  }

  @Scheduled(initialDelay = 10000, fixedRate = 1000)
  public void checkRamAvailable() {
    double value = hostStatusService.getRamAvailable();
    measurementsController.addValue(Metric.RAM_AVAILABLE, value);
    LOGGER.debug("{} = {}", Metric.RAM_AVAILABLE, value);
  }

  @Scheduled(cron = "0 * * * * *")
  public void report() {
    measurementsController.report();
    measurementsController.reset();
  }
}
