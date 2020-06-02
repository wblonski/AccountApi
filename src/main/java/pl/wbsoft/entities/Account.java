package pl.wbsoft.entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

@Entity
public class Account  implements Serializable {
    @Id
    private String pesel;
    private String name;
    private String surname;
    private String amountPLN;
    private String amountUSD;

    public Account() {
    }

    public Account(Map<String, String> params) {
        pesel = params.get("pesel");
        name = params.get("name");
        surname =  params.get("surname");
        amountPLN = params.get("amountPLN");
        amountUSD = params.get("amountUSD");
    }

    public String getPesel() {
        return pesel;
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

    public String getAmountPLN() {
        return amountPLN;
    }

    public void setAmountPLN(String amountPLN) {
        this.amountPLN = amountPLN;
    }

    public String getAmountUSD() {
        return amountUSD;
    }

    public void setAmountUSD(String amountUSD) {
        this.amountUSD = amountUSD;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Account)) return false;
        Account account = (Account) o;
        return pesel.equals(account.pesel) &&
                name.equals(account.name) &&
                surname.equals(account.surname) &&
                amountPLN.equals(account.amountPLN) &&
                amountUSD.equals(account.amountUSD);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pesel, name, surname, amountPLN, amountUSD);
    }
}
