package pl.wbsoft.entities;

import pl.wbsoft.utils.Pesel;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class Account {
    private String name;
    private String surname;
    private Pesel pesel;
    private Map<String, BigDecimal> amounts = new HashMap<>();

    public Account(Map<String, Object> validParams) {

        name = (String) validParams.get("name");
        surname = (String) validParams.get("surname");
        pesel = (Pesel) validParams.get("pesel");
        amounts.put("PLN", (BigDecimal) validParams.get("amount"));
        amounts.put("USD", new BigDecimal(0));
    }

    public Pesel getPesel() {
        return pesel;
    }

    public Optional<Account> getAccount(Pesel pesel) {
        if (this.pesel.equals(pesel)) {
            return Optional.of(this);
        }
        else return Optional.empty();
    }
}
