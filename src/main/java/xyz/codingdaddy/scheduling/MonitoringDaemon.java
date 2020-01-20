package xyz.codingdaddy.scheduling;

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
  private static final Logger LOGGER = LoggerFactory.getLogger(MeasurementsController.class);

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
    LOGGER.debug("{} = {}", Metric.CPU_LOAD, value);
  }

  @Scheduled(fixedDelay = 1000)
  public void checkProcessCount() {
    double value = hostStatusService.getNumberOfProcesses();
    measurementsController.addValue(Metric.PROCESS_COUNT, value);
    LOGGER.debug("{} = {}", Metric.PROCESS_COUNT, value);
  }

  @Scheduled(initialDelay = 60000, fixedRate = 1000)
  public void checkRamUsage() {
    double value = hostStatusService.getRamUsed();
    measurementsController.addValue(Metric.RAM_USED, value);
    LOGGER.debug("{} = {}", Metric.RAM_USED, value);
  }

  @Scheduled(cron = "0 * * * * *")
  public void report() {
    measurementsController.report();
    measurementsController.reset();
  }
}
