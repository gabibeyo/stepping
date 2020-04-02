package com.imperva.stepping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 public class Shouter {
     private final Logger logger = LoggerFactory.getLogger(IRunning.class);
     private Container container;
     private IExceptionHandler rootExceptionHandler;

     public Shouter(Container container, IExceptionHandler rootExceptionHandler) {
         this.container = container;
         this.rootExceptionHandler = rootExceptionHandler;
     }

     public void shout(String subjectType, Object value) {
         try {
             container.<Subject>getById(subjectType).publish(value);
         } catch (Exception e) {
             logger.error("Shouter Failed", e);
             rootExceptionHandler.handle(new SteppingDistributionException(subjectType, "Distribution FAILED", e));
         }
     }

     public void shout(String subjectType, Data value) {
         try {
             container.<Subject>getById(subjectType).publish(value);
         } catch (Exception e) {
             logger.error("Shouter Failed", e);
             rootExceptionHandler.handle(new SteppingDistributionException(subjectType, "Distribution FAILED", e));
         }
     }
 }
