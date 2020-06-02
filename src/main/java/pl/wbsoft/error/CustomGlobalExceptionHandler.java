package pl.wbsoft.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    // Let Spring handle the exception, we just override the status code
    @ExceptionHandler(InvalidParam.class)
    public void springHandleNotFound(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value());
    }

    @ExceptionHandler(AccountExists.class)
    public void springHandleAccountExists(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.ALREADY_REPORTED.value());
    }

    @ExceptionHandler(NotAdultClient.class)
    public void springHandleNotAdultClient(HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.EXPECTATION_FAILED.value());
    }

}
