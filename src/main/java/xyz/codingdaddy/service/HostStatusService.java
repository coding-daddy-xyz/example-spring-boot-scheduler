package xyz.codingdaddy.service;

import java.util.Arrays;
import java.util.concurrent.atomic.DoubleAdder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oshi.SystemInfo;
import xyz.codingdaddy.domain.Aggregation;

/**
 * Allows to retrieve resource usage metrics from the host where the application is running
 *
 * @author serhiy
 */
@Service
public class HostStatusService {
  private static final Logger LOGGER = LoggerFactory.getLogger(HostStatusService.class);

  private final SystemInfo systemInfo = new SystemInfo();

  /**
   * @return average CPU load over configured aggregation period
   */
  public double getCpuLoad() {
    Aggregation aggregation = Aggregation.ONE_MINUTE;
    return systemInfo.getHardware().getProcessor().getSystemLoadAverage(aggregation.getIdentifier())[aggregation.getIndex()];
  }

  /**
   * @return available disk space (percentage)
   */
  public double getDiskSpaceAvailable() {
    delay();

    DoubleAdder available = new DoubleAdder();
    DoubleAdder total = new DoubleAdder();

    Arrays.stream(systemInfo.getOperatingSystem().getFileSystem().getFileStores())
        .filter(fs -> fs.getName().startsWith("/dev/"))
        .forEach(fs -> {
          available.add(fs.getUsableSpace());
          total.add(fs.getTotalSpace());
        });

    return total.sum() > 0 ? available.sum() / total.sum() : 0;
  }

  /**
   * @return available RAM (percentage)
   */
  public double getRamAvailable() {
    double available = systemInfo.getHardware().getMemory().getAvailable();
    double total = systemInfo.getHardware().getMemory().getTotal();
    return total > 0 ? available / total : 0;
  }

  private void delay() {
    try {
      Thread.sleep(10000l);
    } catch (InterruptedException e) {
      LOGGER.debug("Delay operation interrupted: ", e);
    }
  }
}
