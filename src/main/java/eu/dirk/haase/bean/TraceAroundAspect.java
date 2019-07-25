package eu.dirk.haase.bean;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class TraceAroundAspect {

    private String globalIndent = "";
    private int level = 0;

    @Around("execution(* eu.dirk.haase.bean.aop..*(..))")
    public Object traceAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) {
        String indent = globalIndent;
        ++level;
        try {
            String levelStr = (level > 9 ? "" + level : " " + level);
            globalIndent = "   " + globalIndent;
            System.out.println(levelStr + "|" + globalIndent + ">>> " + proceedingJoinPoint.getSignature());
            Object value = null;
            try {
                value = proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println(levelStr + "|" + globalIndent + "<<< " + proceedingJoinPoint.getSignature());
            return value;
        } finally {
            --level;
            globalIndent = indent;
        }
    }

}
