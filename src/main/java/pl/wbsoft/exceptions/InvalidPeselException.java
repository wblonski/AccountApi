package pl.wbsoft.exceptions;

import static java.lang.String.format;

public class InvalidPeselException extends Exception {
    
    private static final String BASE_MESSAGE = "Pesel: \"%s\" is not valid. ";
    private final String pesel;
    private final String extMessage;
    
    public InvalidPeselException(String pesel) {
        this.pesel = pesel;
        this.extMessage = "";
    }
    
    public InvalidPeselException(String message, String pesel) {
        this.pesel = pesel;
        this.extMessage = message;
    }
    
    @Override
    public String getMessage() {
        return format(BASE_MESSAGE + extMessage, pesel);
    }
    
}
