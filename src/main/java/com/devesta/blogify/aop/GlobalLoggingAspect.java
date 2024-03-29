package com.devesta.blogify.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class GlobalLoggingAspect {

    Logger logger = LoggerFactory.getLogger(this.getClass());
    final String pointCutInterceptions = "execution(* com.devesta.blogify.user.UserController.*(..)) || " +
            "execution(* com.devesta.blogify.community.CommunityController.*(..)) || " +
            "execution(* com.devesta.blogify.comment.CommentController.*(..)) || " +
            "execution(* com.devesta.blogify.post.PostController.*(..)) || " +
            "execution(* com.devesta.blogify.authentication.controller.AuthenticationController.*(..)) || " +
            "execution(* com.devesta.blogify.search.SearchController.*(..))";


    @Before(pointCutInterceptions)
    public void logBefore(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Entering method: {}", methodName);
    }

    @AfterReturning(pointcut = pointCutInterceptions, returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        if (result instanceof ResponseEntity<?> responseEntity) {

            HttpStatus statusCode = (HttpStatus) responseEntity.getStatusCode();
            Object body = responseEntity.getBody();
            if (statusCode == HttpStatus.OK || statusCode == HttpStatus.CREATED) {
                logger.info("Method {} returned with status code: {}", methodName, statusCode);
                logger.info("Method {} returned with result: {}", methodName, body);
            } else if (statusCode == HttpStatus.NO_CONTENT) {
                logger.info("Method {} returned with status code: {}", methodName, statusCode);
            } else {
                logger.warn("Method {} returned with status code: {}", methodName, statusCode);
            }
        } else {
            logger.info("Method {} returned with result: {}", methodName, result);
        }
    }


}
