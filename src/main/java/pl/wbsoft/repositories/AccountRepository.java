package pl.wbsoft.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.wbsoft.entity.Account;


public interface AccountRepository extends JpaRepository<Account, String> {

}
