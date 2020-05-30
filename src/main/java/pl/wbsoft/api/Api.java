package pl.wbsoft.api;

import com.sun.istack.internal.NotNull;
import pl.wbsoft.dao.AccountDao;
import pl.wbsoft.entities.Account;
import pl.wbsoft.exceptions.AccountExists;
import pl.wbsoft.exceptions.InvalidParam;
import pl.wbsoft.exceptions.NotAdultClient;
import pl.wbsoft.utils.Pesel;
import pl.wbsoft.validators.AccountValidator;

import java.util.HashMap;
import java.util.Map;

public class Api {

    private AccountDao dao;
    private AccountValidator validator;

    public Api(AccountDao dao, AccountValidator validator) {
        this.dao = dao;
        this.validator = validator;
    }

    public Account CreateAccount (@NotNull String name, @NotNull String surname, @NotNull String pesel,
                                   @NotNull String amount) throws InvalidParam, NotAdultClient, AccountExists {

        // walidacja parametrów przychodzących
        Map<String, Object> validParams = new HashMap<>();
        try {
            validParams.put("name", validator.validName(name.trim()));
            validParams.put("surname", validator.validSurname(surname.trim()));
            validParams.put("pesel", new Pesel(pesel.trim()));
            validParams.put("amount", validator.validAmount(amount.trim()));
        } catch (Exception e) {
            throw new InvalidParam(e);
        }
        // walidacja biznesowa
        if (isNotAdultClient((Pesel)validParams.get("pesel")))
            throw new NotAdultClient();
        if (accountExists((Pesel)validParams.get("pesel")))
            throw new AccountExists();

        // utworzenie konta
        Account account = new Account(validParams);
        account = dao.create(account);

        // TODO logowanie

        return account;

    }

    private boolean isNotAdultClient(@NotNull Pesel pesel) {
        return !pesel.isAdult();
    }

    private boolean accountExists(@NotNull Pesel pesel) {
        return dao.find(pesel).isPresent();
    }


}
