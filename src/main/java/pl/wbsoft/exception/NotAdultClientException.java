package pl.wbsoft.exception;

import static java.lang.String.format;

public class NotAdultClientException extends Exception {
    
    private static final String BASE_MESSAGE = "Bussines Logic Error: The Account is not created because the Owner " +
            "of a pesel: \"%s\" is not adult. Repair request body data and try again. ";
    private final String pesel;
    
    public NotAdultClientException() {
        pesel = "";
    }
    
    public NotAdultClientException(String pesel) {
        super(format(BASE_MESSAGE, pesel));
        this.pesel = pesel;
    }
    
    @Override
    public String getMessage() {
        return format(BASE_MESSAGE, pesel);
    }
}
