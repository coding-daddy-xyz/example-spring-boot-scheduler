package xyz.codingdaddy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;

@Service
public class HostStatusService {
  private static final Logger LOGGER = LoggerFactory.getLogger(HostStatusService.class);

  private final SystemInfo systemInfo = new SystemInfo();

  public double getCpuLoad() {
    return systemInfo.getHardware().getProcessor().getSystemLoadAverage(1)[0];
  }

  public double getNumberOfProcesses() {
    delay();
    return systemInfo.getOperatingSystem().getProcessCount();
  }

  public double getRamUsed() {
    double available = (double) systemInfo.getHardware().getMemory().getAvailable() / 1024 / 1024;
    double total = (double) systemInfo.getHardware().getMemory().getTotal() / 1024 / 1024;
    return total - available;
  }

  private void delay() {
    try {
      Thread.sleep(10000l);
    } catch (InterruptedException e) {
      LOGGER.debug("Delay operation interrupted: ", e);
    }
  }
}
