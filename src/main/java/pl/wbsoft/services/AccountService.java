package pl.wbsoft.services;

import pl.wbsoft.entities.Account;
import pl.wbsoft.errors.InvalidParamException;

import java.util.Map;
import java.util.Optional;

public interface AccountService {

    Account createAccount(Account newAccount) throws InvalidParamException;

    Account getAccountByPesel(String pesel);

    Optional<Account> exchangeInAccount(String pesel, Map<String, String> update);

}
