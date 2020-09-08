package pl.wbsoft.dto.nbp;

import java.util.List;

public class ExchangeRatesData {
    String Table;
    String Currency;
    String Code;
    List<Rate> Rates;
    
    public String getTable() {
        return Table;
    }
    
    public void setTable(String table) {
        Table = table;
    }

    public String getCurrency() {
        return Currency;
    }

    public void setCurrency(String currency) {
        Currency = currency;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public List<Rate> getRates() {
        return Rates;
    }

    public void setRates(List<Rate> rates) {
        Rates = rates;
    }
}
