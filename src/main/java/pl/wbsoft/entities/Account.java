package pl.wbsoft.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
public class Account implements Serializable {
    @Id
    private String pesel;
    private String name;
    private String surname;
    @ElementCollection
    private Map<String, BigDecimal> currencies = new HashMap<>();
    
    public Account() {
    }
    
    public Account(@NotNull String pesel, @NotNull String name, @NotNull String surname, @NotNull Map<String, BigDecimal> currencies) {
        this.pesel = pesel;
        this.name = name;
        this.surname = surname;
        this.currencies.putAll(currencies);
    }
    
    public String getPesel() {
        return pesel;
    }
    
    public BigDecimal getSubValue(String key) {
        return currencies.get(key);
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }
    
    public Map<String, BigDecimal> getCurrencies() {
        return currencies;
    }
    
    public void setCurrencies(Map<String, BigDecimal> subAccounts) {
        this.currencies = subAccounts;
    }
    
    public void setSubValue(String key, BigDecimal value) {
        currencies.put(key, value);
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "pesel='" + pesel + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", currencies=" + currencies +
                '}';
    }
}
