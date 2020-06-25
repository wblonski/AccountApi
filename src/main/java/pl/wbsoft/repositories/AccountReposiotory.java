package pl.wbsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wbsoft.entities.Account;


public interface AccountReposiotory extends JpaRepository<Account, String> {

}
