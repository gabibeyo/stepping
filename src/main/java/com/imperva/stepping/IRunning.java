package com.imperva.stepping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.Closeable;
import java.util.concurrent.*;

abstract class IRunning implements Closeable {
    private final Logger logger = LoggerFactory.getLogger(IRunning.class);
    Runnable runnable;
    protected abstract Future<?> awake();

    void close(Future future) {
        try {

            if (future != null && !future.isDone() && !future.isCancelled()) {
                logger.info("@@@@@ Start Closing Stepping Future");
                boolean isCanceled = future.cancel(true);
                logger.trace("Stepping orchestrator Future canceled successfully?: " + isCanceled);
                logger.info("Finish closing Stepping orchestrator");
            }
        } catch (Exception e) {
            logger.error("Failed closing Stepping orchestrator", e);
        }
    }
}
