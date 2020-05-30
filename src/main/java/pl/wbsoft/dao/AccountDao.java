package pl.wbsoft.dao;

import pl.wbsoft.entities.Account;
import pl.wbsoft.utils.Pesel;

import java.util.Optional;

public interface AccountDao {

    Optional<Account> find(Pesel pesel);

    Account create(Account account);
}
