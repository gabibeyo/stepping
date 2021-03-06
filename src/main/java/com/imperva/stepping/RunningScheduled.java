package com.imperva.stepping;

import java.util.concurrent.*;

 public class RunningScheduled extends IRunning {
     private String id;
     private long delay;
     private long initialdelay;
     private ScheduledFuture scheduledFuture;
     private ScheduledExecutorService scheduledExecutorService;
     private TimeUnit timeUnit;

     protected RunningScheduled(String id, long delay, long initialdelay, TimeUnit timeUnit, Runnable runnable) {
         this.id = id;
         this.delay = delay;
         this.initialdelay = initialdelay;
         this.runnable = runnable;
         this.timeUnit = timeUnit;
         this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new ThreadFactory() {
             @Override
             public Thread newThread(Runnable r) {
                 Thread t = new Thread(r);
                 t.setName(id);
                 return t;
             }
         });
     }

     protected Future<?> awake() {
         this.scheduledFuture = scheduledExecutorService.scheduleWithFixedDelay(runnable, initialdelay, delay, timeUnit);
         return scheduledFuture;
     }

     ScheduledExecutorService getScheduledExecutorService() {
         return scheduledExecutorService;
     }

     public void changeDelay(long delay, long initialdelay, TimeUnit timeUnit) {
         scheduledFuture.cancel(false);
         scheduledExecutorService.scheduleWithFixedDelay(runnable, initialdelay, delay, timeUnit);
     }

     public void changeDelay(long delay, TimeUnit timeUnit) {
         scheduledFuture.cancel(false);
         scheduledExecutorService.scheduleWithFixedDelay(runnable, delay, delay, timeUnit);
     }

     public void stop() {
         close(scheduledFuture, false);
     }

     @Override
     public void close() {
         close(scheduledFuture, true);
     }
 }