package pl.wbsoft.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.wbsoft.entities.Account;
import pl.wbsoft.services.AccountService;
import pl.wbsoft.utils.Pesel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/account", produces = "application/json")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping
    public Account createAccount(@NotNull @Valid @RequestBody Account newAccount) {
        try {
            return accountService.createAccount(newAccount);
        } catch (Exception ex) {
            return null;
        }
    }

    @GetMapping("/{pesel}")
    public Account getAccount(@NotNull @PathVariable String pesel) {
        if (Pesel.isValidPeselStr(pesel)) {
            return accountService.getAccountByPesel(pesel);
        } else
            return null;
    }

    // Map contains keys: currencyFrom, currencyTo, value
    @PatchMapping("/{pesel}")
    public Optional<Account> exchangeInAccount(@NotNull @PathVariable String pesel,
                                               @NotNull @Valid @RequestBody Map<String, String> update) {

        return accountService.exchangeInAccount(pesel, update);
    }


}
