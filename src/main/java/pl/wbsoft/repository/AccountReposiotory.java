package pl.wbsoft.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wbsoft.entities.Account;


public interface AccountReposiotory extends JpaRepository<Account, String> {

//    Optional<Account> find(Pesel pesel);
//
//    Account create(Account account);
}
