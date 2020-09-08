package pl.wbsoft.exception;

import static java.lang.String.format;

public class AccountNotFoundException extends Exception {
    
    private static final String BASE_MESSAGE = "Account with pesel: \"%s\" does not exist. ";
    private final String pesel;
    
    public AccountNotFoundException(String pesel) {
        this.pesel = pesel;
    }
    
    @Override
    public String getMessage() {
        return format(BASE_MESSAGE, pesel);
    }
    
}
