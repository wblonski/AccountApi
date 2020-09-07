package pl.wbsoft.exceptions.advices;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pl.wbsoft.entities.Account;
import pl.wbsoft.exceptions.InvalidPeselException;

import java.util.Optional;

@RestControllerAdvice
public class InvalidPeselAdvice {
    
    @ExceptionHandler(InvalidPeselException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Optional<Account> invalidPeselHandler(InvalidPeselException ex) {
        return Optional.empty();
    }
}
