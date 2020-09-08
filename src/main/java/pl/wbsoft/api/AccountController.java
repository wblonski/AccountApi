package pl.wbsoft.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.wbsoft.entities.Account;
import pl.wbsoft.exceptions.*;
import pl.wbsoft.pojos.Order;
import pl.wbsoft.services.AccountService;
import pl.wbsoft.utils.Pesel;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Optional;

@RestController
@RequestMapping(value = "/api/accounts", produces = "application/json")
public class AccountController {

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PutMapping("/create")
    public Optional<Account> createAccount(@NotNull @Valid @RequestBody Account newAccount)
            throws InvalidPeselException, NotAdultClientException, AccountJustExistsException {
        
        return accountService.createAccount(newAccount);
    }
    
    @GetMapping("/{pesel}")
    public Optional<Account> getAccount(@NotNull @PathVariable String pesel) throws InvalidPeselException, AccountNotFoundException {
        
        Pesel.validatePeselStr(pesel);
        return accountService.getAccountByPesel(pesel);
    }
    
    @PostMapping("/exchange")
    public Optional<Account> exchangeInAccount(@NotNull @Valid @RequestBody Order order)
            throws InvalidPeselException, AccountNotFoundException, ExternalExchangeServiceException, InvalidExchangeOrderParameterException {
        
        Pesel.validatePeselStr(order.getPesel());
        return accountService.exchangeInAccount(order.getPesel(), order);
    }
    
    // -----------------------------------------------------------------------------------------------------------------
    
    @ExceptionHandler(AccountJustExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String accountJustExistsHandler(AccountJustExistsException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String accountNotFoundHandler(AccountNotFoundException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(ExternalExchangeServiceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String invalidExchangeHandler(ExternalExchangeServiceException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(InvalidExchangeOrderParameterException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String invalidExchangeOrderParameterHandler(InvalidExchangeOrderParameterException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(InvalidPeselException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String invalidPeselHandler(InvalidPeselException ex) {
        return ex.getMessage();
    }
    
    @ExceptionHandler(NotAdultClientException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String notAdultClientHandler(NotAdultClientException ex) {
        return ex.getMessage();
    }
    
}
