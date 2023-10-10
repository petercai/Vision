package com.peppermint.vision.common;

import com.google.common.base.Stopwatch;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import java.util.ArrayList;

@Aspect
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Configuration
public class LoggingAspect {

  @Around(
      "execution(* com.peppermint.vision.feed.Feed*.*(..)) || "
          + "execution(* com.peppermint.vision.feed.Http*.*(..)) || "
          + "execution(* com.peppermint.vision.persistence.service.Feed*.*(..))")
  public Object log(final ProceedingJoinPoint point) throws Throwable {
    Logger LOGGER = null;
    String methodName = null;
    Stopwatch timer = null;
    Object retVal = null;
    Object target = point.getTarget();
//    target = target==null?this:target;
    try {
      if (target != null) {
        LOGGER = LoggerFactory.getLogger(target.getClass());
        timer = Stopwatch.createStarted();
        methodName = ((MethodSignature) point.getSignature()).getMethod().getName();

        Object[] args = point.getArgs();
        String params = "";
        if (args != null && args.length > 0) {
          ArrayList<String> list = new ArrayList<>(args.length);
          for (Object o : args) {
            list.add(String.valueOf(o));
          }
          params = String.join(",", list);
        }

        LOGGER.info(String.format("start: %s(%s) -ASPECT-", methodName, params));
      }

        retVal = point.proceed();
        return retVal;
    } catch (final Exception ex) {
      LOGGER.error("Errors caught in LoggingAspect -ASPECT- ", ex);
      throw ex;
    } finally {
      if (target != null) {
        LOGGER.info("end: " + methodName + "() -ASPECT- in " + timer.stop().toString() + " with return = " + String.valueOf(retVal)); // $NON-NLS-1$
      }
    }
  }
}
