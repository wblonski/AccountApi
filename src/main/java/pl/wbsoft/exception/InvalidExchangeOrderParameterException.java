package pl.wbsoft.exception;

import pl.wbsoft.dto.Order;

import static java.lang.String.format;

public class InvalidExchangeOrderParameterException extends Exception {
    
    private static final String BASE_MESSAGE = "Invalid order parameter for order: \"%s\". ";
    private final String messageExt;
    private final Order order;
    
    public InvalidExchangeOrderParameterException(String messageExt, Order order) {
        this.messageExt = messageExt;
        this.order = order;
    }
    
    @Override
    public String getMessage() {
        return format(BASE_MESSAGE + messageExt, order.toString());
    }
    
}
