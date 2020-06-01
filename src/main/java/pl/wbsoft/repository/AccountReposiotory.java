package pl.wbsoft.repository;

import org.springframework.data.repository.CrudRepository;


public interface AccountReposiotory<Account, String> extends CrudRepository<Account, String> {

//    Optional<Account> find(Pesel pesel);
//
//    Account create(Account account);
}
