package com.basaki.aspect;

import com.basaki.annotation.CustomAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * {@code CustomAnnotationAspect} intercepts any private method execution if a
 * method is tagged with {@code com.basaki.annotation.CustomAnnotation}
 * annotation.
 * <p>
 *
 * @author Indra Basak
 * @since 02/07/18
 */
@Component
@Aspect
@Slf4j
public class CustomAnnotationAspect {

    @Before("@annotation(anno) && execution(private * *(..))")
    public void inspectPrivateMethod(JoinPoint jp, CustomAnnotation anno) {
        log.info(
                "Entering CustomAnnotationAspect.inspectPrivateMethod() in class "
                        + jp.getSignature().getDeclaringTypeName()
                        + " - method: " + jp.getSignature().getName()
                        + " description: " + anno.description());
    }
}
