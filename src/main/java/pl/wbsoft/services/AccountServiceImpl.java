package pl.wbsoft.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.wbsoft.dto.Order;
import pl.wbsoft.entity.Account;
import pl.wbsoft.exception.*;
import pl.wbsoft.repositories.AccountRepository;
import pl.wbsoft.utils.Pesel;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@Service
public class AccountServiceImpl implements AccountService {
    
    public static final int ADULT_AGE = 18;
    
    public static final RoundingMode ROUND_TYPE = RoundingMode.FLOOR;
    private static final int PRECISION = 4;
    private static final MathContext mathContext = new MathContext(PRECISION, ROUND_TYPE);
    
    private static final String INVALID_VALUE_IN_ORDER = "Invalid value in order. ";
    private static final String THERE_ARE_NOT_ENOUGH_MONEY = "There are not enough money on account. ";
    private static final String INVALID_CURRENCIES_SHOULD_BE_NOT_THE_SAME = "Currencies in order should not be the same. ";
    private static final String CURRENCY_IN_ORDER_NOT_EXISTS_IN_ACCOUNT = "Some used currency does not exist in this " +
            "account. ";
    
    private final AccountRepository repo;
    private final RateService rateService;
    
    @Autowired
    public AccountServiceImpl(AccountRepository repo, RateService rateService) {
        this.repo = repo;
        this.rateService = rateService;
    }
    
    @Override
    public Optional<Account> createAccount(Account newAccount) throws InvalidPeselException, NotAdultClientException,
            AccountJustExistsException {
        
        Pesel.validatePeselStr(newAccount.getPesel());
        if (!Pesel.isAdultPeselOwner(newAccount.getPesel(), ADULT_AGE)) {
            throw new NotAdultClientException(newAccount.getPesel());
        }
        if (accountExists(newAccount.getPesel())) {
            throw new AccountJustExistsException(newAccount.getPesel());
        }
        return Optional.of(repo.save(newAccount));
    }
    
    @Override
    public Optional<Account> getAccountByPesel(String pesel) throws AccountNotFoundException {
        
        return Optional.of(repo.findById(pesel).orElseThrow(() -> new AccountNotFoundException(pesel)));
    }
    
    @Override
    public Optional<Account> exchangeInAccount(@NotNull String pesel, @NotNull Order order)
            throws AccountNotFoundException, InvalidExchangeOrderParameterException, ExternalExchangeServiceException {
        
        Optional<Account> myAccountOpt = repo.findById(pesel);
        if (!myAccountOpt.isPresent())
            throw new AccountNotFoundException(pesel);
        Account account = myAccountOpt.get();
        
        validateOrder(order, account);
        return executeOrder(order, account);
    }
    
    @Transactional
    Optional<Account> executeOrder(Order order, Account account) throws ExternalExchangeServiceException {
        
        BigDecimal currentRate = order.getCurrencyFrom().equals("PLN")
                ? new BigDecimal(1).divide(rateService.getCurrentExchangeRate(order.getCurrencyTo()), mathContext)
                : rateService.getCurrentExchangeRate(order.getCurrencyFrom());
        
        account.setSubValue(order.getCurrencyFrom(), account.getSubValue(order.getCurrencyFrom()).subtract(order.getValue()));
        
        BigDecimal calculatedValue = order.getValue().multiply(currentRate, mathContext);
        account.setSubValue(order.getCurrencyTo(), account.getSubValue(order.getCurrencyTo()).add(calculatedValue, mathContext));
        return Optional.of(repo.save(account));
    }
    
    private boolean accountExists(String pesel) {
        return repo.findById(pesel).isPresent();
    }
    
    private void validateOrder(Order order, Account account) throws InvalidExchangeOrderParameterException {
        if (order.getValue().compareTo(ZERO) < 0) {
            throw new InvalidExchangeOrderParameterException(INVALID_VALUE_IN_ORDER, order);
        }
        if (!account.getCurrencies().containsKey(order.getCurrencyFrom()) ||
                !account.getCurrencies().containsKey(order.getCurrencyTo())) {
            throw new InvalidExchangeOrderParameterException(CURRENCY_IN_ORDER_NOT_EXISTS_IN_ACCOUNT, order);
        }
        if (order.getCurrencyFrom().equals(order.getCurrencyTo())) {
            throw new InvalidExchangeOrderParameterException(INVALID_CURRENCIES_SHOULD_BE_NOT_THE_SAME, order);
        }
        if ((account.getSubValue(order.getCurrencyFrom()).compareTo(order.getValue()) < 0)) {
            throw new InvalidExchangeOrderParameterException(THERE_ARE_NOT_ENOUGH_MONEY, order);
        }
    }
}
