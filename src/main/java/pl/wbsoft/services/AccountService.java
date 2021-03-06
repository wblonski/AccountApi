package pl.wbsoft.services;

import pl.wbsoft.dto.Order;
import pl.wbsoft.entity.Account;
import pl.wbsoft.exception.*;

import java.util.Optional;

public interface AccountService {
    
    Optional<Account> createAccount(Account newAccount)
            throws InvalidPeselException, NotAdultClientException, AccountJustExistsException;
    
    Optional<Account> getAccountByPesel(String pesel)
            throws AccountNotFoundException;
    
    Optional<Account> exchangeInAccount(String pesel, Order order)
            throws AccountNotFoundException, ExternalExchangeServiceException, InvalidExchangeOrderParameterException;
    
}
