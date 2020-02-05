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

  public double getDiskSpaceAvailable() {
    delay();
    double available = systemInfo.getOperatingSystem().getFileSystem().getFileStores()[0].getUsableSpace();
    double total = systemInfo.getOperatingSystem().getFileSystem().getFileStores()[0].getTotalSpace();
    return available / total;
  }

  public double getRamAvailable() {
    double available = systemInfo.getHardware().getMemory().getAvailable();
    double total = systemInfo.getHardware().getMemory().getTotal();
    return available / total;
  }

  private void delay() {
    try {
      Thread.sleep(10000l);
    } catch (InterruptedException e) {
      LOGGER.debug("Delay operation interrupted: ", e);
    }
  }
}
