package pl.wbsoft.entities;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Map;

@Entity
public class Account implements Serializable {
    @Id
    private String pesel;
    private String name;
    private String surname;
    @ElementCollection
    private Map<String, BigDecimal> subAccounts;

    public Account() {
    }

    public Account(@NotNull Map<String, String> params, @NotNull Map<String, BigDecimal> subAccounts) {
        this.pesel = params.get("pesel");
        this.name = params.get("name");
        this.surname = params.get("surname");
        this.subAccounts.putAll(subAccounts);
    }

    public String getPesel() {
        return pesel;
    }

    public BigDecimal getSubValue(String key) {
        return subAccounts.get(key);
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

    public Map<String, BigDecimal> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(Map<String, BigDecimal> subAccounts) {
        this.subAccounts = subAccounts;
    }

    public void setSubValue(String key, BigDecimal value) {
        subAccounts.put(key, value);
    }

    @Override
    public String toString() {
        return "Account{" +
                "pesel='" + pesel + '\'' +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", subAccounts=" + subAccounts +
                '}';
    }
}
