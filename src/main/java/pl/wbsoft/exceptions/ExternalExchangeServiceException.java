package pl.wbsoft.exceptions;

public class ExternalExchangeServiceException extends Exception {
    
    private static final String BASE_MESSAGE = "There is an external exchange service error. ";
    private final String messageExt;
    
    public ExternalExchangeServiceException(String messageExt) {
        this.messageExt = messageExt;
    }
    
    @Override
    public String getMessage() {
        return BASE_MESSAGE + messageExt;
    }
    
}
