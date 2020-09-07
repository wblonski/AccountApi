package pl.wbsoft.pojos;

import java.io.Serializable;
import java.math.BigDecimal;

public class Order implements Serializable {
    private final String pesel;
    private final String currencyFrom;
    private final String currencyTo;
    private final BigDecimal value;
    
    public Order(final String pesel, final String currencyFrom, final String currencyTo, final BigDecimal value) {
        this.pesel = pesel;
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.value = value;
    }
    
    public String getPesel() {
        return pesel;
    }
    
    public String getCurrencyFrom() {
        return currencyFrom;
    }
    
    public String getCurrencyTo() {
        return currencyTo;
    }
    
    public BigDecimal getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "pesel='" + pesel + '\'' +
                ", currencyFrom='" + currencyFrom + '\'' +
                ", currencyTo='" + currencyTo + '\'' +
                ", value=" + value +
                '}';
    }
}
