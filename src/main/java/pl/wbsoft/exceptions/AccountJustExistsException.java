package pl.wbsoft.exceptions;

import static java.lang.String.format;

public class AccountJustExistsException extends Exception {
    
    private static final String BASE_MESSAGE = "Account with pesel: \"%s\" just exists. ";
    private final String pesel;
    
    public AccountJustExistsException() {
        pesel = "";
    }
    
    public AccountJustExistsException(String pesel) {
        super(format(BASE_MESSAGE, pesel));
        this.pesel = pesel;
    }
    
    @Override
    public String getMessage() {
        return format(BASE_MESSAGE, pesel);
    }
    
}
