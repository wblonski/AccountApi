package pl.wbsoft.services;

import org.springframework.stereotype.Service;
import pl.wbsoft.entities.Account;
import pl.wbsoft.enums.AllowedCurrencies;
import pl.wbsoft.repositories.AccountReposiotory;
import pl.wbsoft.utils.Pesel;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.ZERO;

@Service
public class AccountServiceImpl implements AccountService {

    private static final int PRECISION = 4;

    private final AccountReposiotory repo;
    private final RateService rateService;

    public AccountServiceImpl(AccountReposiotory repo, RateService rateService) {
        this.repo = repo;
        this.rateService = rateService;
    }

    @Override
    public Account createAccount(Account newAccount) {

        if (Pesel.isValidPeselStr(newAccount.getPesel()) &&
                isAdultClient(newAccount.getPesel()) &&
                !accountExists(newAccount.getPesel()))
            return repo.save(newAccount);
        else
            return null;
    }

    @Override
    public Account getAccountByPesel(String pesel) {

        return repo
                .findById(pesel)
                .orElse(null);
    }

    @Override
    public Optional<Account> exchangeInAccount(@NotNull String pesel, @NotNull Map<String, String> update) {

        String currencyFrom = update.get("currencyFrom");
        String currencyTo = update.get("currencyTo");
        BigDecimal value;
        BigDecimal calculatedValue;

        value = new BigDecimal(update.get("value"));

        if (value.compareTo(ZERO) < 0) {
            return Optional.empty();
        }
        MathContext mc = new MathContext(PRECISION, RoundingMode.FLOOR);
        boolean areAllowedCurrencies = AllowedCurrencies.areAllowed(currencyFrom, currencyTo);
        if (currencyFrom.equals(currencyTo) || !areAllowedCurrencies) {
            return Optional.empty();
        }
        Optional<Account> opt = repo.findById(pesel);
        if (opt.isPresent()) {
            Account account = opt.get();
            if ((account.getSubValue(currencyFrom).compareTo(value) < 0)) {
                return Optional.empty();
            }
            account.setSubValue(currencyFrom, account.getSubValue(currencyFrom).subtract(value));
            BigDecimal currentRate;
            try {
                currentRate = currencyFrom.equals("PLN")
                        ? new BigDecimal(1).divide(rateService.getCurrentExchangeRate(currencyTo), mc)
                        : rateService.getCurrentExchangeRate(currencyFrom);
            } catch (Exception ex) {
                return Optional.empty();
            }
            calculatedValue = value.multiply(currentRate, mc);
            account.setSubValue(currencyTo, account.getSubValue(currencyTo).add(calculatedValue, mc));
            return Optional.of(repo.save(account));
        }
        return Optional.empty();
    }

    private boolean isAdultClient(String pesel) {

        final int adultAdgeInYears = 18;
        Pesel peselObj;
        try {
            peselObj = new Pesel(pesel);
            return !peselObj.getBirthDate().plusYears(adultAdgeInYears).isAfter(LocalDate.now());
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean accountExists(String pesel) {
        return repo.findById(pesel).isPresent();
    }


}
