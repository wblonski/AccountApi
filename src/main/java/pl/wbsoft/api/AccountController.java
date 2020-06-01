package pl.wbsoft.api;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.wbsoft.entities.Account;
import pl.wbsoft.error.AccountExists;
import pl.wbsoft.error.InvalidParam;
import pl.wbsoft.error.NotAdultClient;
import pl.wbsoft.repository.AccountReposiotory;
import pl.wbsoft.utils.Pesel;
import pl.wbsoft.validators.AccountValidator;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/")
public class AccountController extends Application {

    @Qualifier("accountRepositoryInMemory")
    @Autowired
    private AccountReposiotory<Account, String> repo;

    @Autowired
    private AccountValidator validator;


    @PutMapping("/accounts/{name}/{surname}/{pesel}/{amount}")
    Account createAccount(@PathVariable String name, @PathVariable String surname, @PathVariable String pesel,
                          @PathVariable String amount) throws InvalidParam, NotAdultClient, AccountExists {

        // walidacja parametrów przychodzących
        Map<String, String> validParams = new HashMap<>();
        try {
            validParams.put("name", validator.validName(name.trim()));
            validParams.put("surname", validator.validSurname(surname.trim()));
            validParams.put("pesel", new Pesel(pesel.trim()).toString());
            validParams.put("amount", validator.validAmount(amount.trim()));
        } catch (Exception e) {
            throw new InvalidParam(e.getMessage());
        }
        // walidacja biznesowa
        if (isNotAdultClient(validParams.get("pesel")))
            throw new NotAdultClient();
        if (accountExists(validParams.get("pesel")))
            throw new AccountExists();

        // utworzenie konta
        Account account = new Account();
        account = repo.save(account);

        // TODO logowanie

        return account;

    }

    private boolean isNotAdultClient(String pesel) {

        final int adultAdgeInYears = 18;
        return true;
//                !repo.findById(pesel).get().getBirthDate().plusYears(adultAdgeInYears).isBefore(LocalDate.now());
    }

    private boolean accountExists(String pesel) {
        return repo.findById(pesel).isPresent();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

    }
}
