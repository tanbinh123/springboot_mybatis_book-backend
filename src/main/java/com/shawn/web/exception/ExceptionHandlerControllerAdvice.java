package com.shawn.web.exception;

import com.shawn.constant.ErrorCode;
import com.shawn.model.dto.Error;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.apache.commons.logging.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Xiaoyue Xiao
 */
@CommonsLog
@ControllerAdvice
class ExceptionHandlerControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> resourceNotFoundExceptionHandler(HttpServletRequest request, ResourceNotFoundException e) {
        logError(request, e);
        Error f = new Error();
        f.setCode(ErrorCode.RESOURCE_NOT_FOUND_ERROR);
        f.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(f);
    }

    @ExceptionHandler(ParameterIllegalException.class)
    public ResponseEntity<?> parameterIllegalExceptionHandler(HttpServletRequest request, ParameterIllegalException e) {
        logError(request, e);
        Error f = new Error();
        f.setCode(ErrorCode.PARAMETER_ILLEGAL_ERROR);
        f.setMessage("An invalid value was specified for one of the query parameters in the request URL.");
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(f);
    }

    @ExceptionHandler(ServerInternalErrorException.class)
    public ResponseEntity<?> serverInternalErrorExceptionHandler(HttpServletRequest request, ServerInternalErrorException e) {
        logError(request, e);
        Error f = new Error();
        f.setCode(ErrorCode.RESOURCE_NOT_FOUND_ERROR);
        f.setMessage("The server encountered an internal error. Please retry the request.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(f);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(HttpServletRequest request, Exception e) {
        logError(request, e);
        Error f = new Error();
        f.setCode(ErrorCode.SERVER_INTERNAL_ERROR);
        f.setMessage("The server met an unexpected error. Please contact administrators.");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(f);
    }

    /********************************** HELPER METHOD **********************************/
    private void logError(HttpServletRequest request, Exception e) {
        Log log=new Log() {
            @Override
            public boolean isDebugEnabled() {
                return false;
            }

            @Override
            public boolean isErrorEnabled() {
                return false;
            }

            @Override
            public boolean isFatalEnabled() {
                return false;
            }

            @Override
            public boolean isInfoEnabled() {
                return false;
            }

            @Override
            public boolean isTraceEnabled() {
                return false;
            }

            @Override
            public boolean isWarnEnabled() {
                return false;
            }

            @Override
            public void trace(Object message) {

            }

            @Override
            public void trace(Object message, Throwable t) {

            }

            @Override
            public void debug(Object message) {

            }

            @Override
            public void debug(Object message, Throwable t) {

            }

            @Override
            public void info(Object message) {

            }

            @Override
            public void info(Object message, Throwable t) {

            }

            @Override
            public void warn(Object message) {

            }

            @Override
            public void warn(Object message, Throwable t) {

            }

            @Override
            public void error(Object message) {

            }

            @Override
            public void error(Object message, Throwable t) {

            }

            @Override
            public void fatal(Object message) {

            }

            @Override
            public void fatal(Object message, Throwable t) {

            }
        };
        log.error("[URI: " + request.getRequestURI() + "]"
                + "[error: " + e.getMessage() + "]");
    }

}
