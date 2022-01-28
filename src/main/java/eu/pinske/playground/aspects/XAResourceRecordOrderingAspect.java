package eu.pinske.playground.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.LoggerFactory;

import com.arjuna.ats.arjuna.common.Uid;
import com.arjuna.ats.internal.jta.resources.arjunacore.XAResourceRecord;

@Aspect
// TODO https://github.com/snowdrop/narayana-spring-boot/issues/85
public class XAResourceRecordOrderingAspect {

    @Around("execution(public * com.arjuna.ats.internal.jta.resources.arjunacore.XAResourceRecord.order())")
    public Object order(ProceedingJoinPoint pjp) throws Throwable {
        Object resource = ((XAResourceRecord) pjp.getThis()).value();
        if (resource.getClass().getName().startsWith("com.ibm.db2")
                || resource.getClass().getName().startsWith("org.h2")) {
            LoggerFactory.getLogger(getClass()).debug("ordering {} as first resource", pjp.getThis());
            return Uid.minUid();
        }
        return pjp.proceed();
    }
}