package pl.wbsoft.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import pl.wbsoft.entities.Account;
import pl.wbsoft.entities.ExchangeRatesSeries;
import pl.wbsoft.entities.Rate;
import pl.wbsoft.enums.AllowedCurrencies;
import pl.wbsoft.error.AccountExists;
import pl.wbsoft.error.InvalidParam;
import pl.wbsoft.error.InvalidPesel;
import pl.wbsoft.error.NotAdultClient;
import pl.wbsoft.repository.AccountReposiotory;
import pl.wbsoft.utils.Pesel;
import pl.wbsoft.validators.AccountValidator;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;

import static java.math.BigDecimal.ROUND_HALF_UP;

@RestController
@RequestMapping("/api")
public class AccountController {

    @Autowired
    private AccountReposiotory repo;

    @Autowired
    private AccountValidator validator;


    @PutMapping("/account/{pesel}")
    Account createAccount(@RequestBody Account newAccount, @PathVariable String pesel) throws InvalidParam, NotAdultClient, AccountExists {

        // walidacja parametrów przychodzących
        try {
            validator.validName(newAccount.getName().trim());
            validator.validSurname(newAccount.getSurname().trim());
            new Pesel(pesel.trim());
            validator.validAmount(newAccount.getAmountPLN().trim());
        } catch (Exception e) {
            throw new InvalidParam(e.getMessage());
        }
        // walidacja biznesowa
        if (isNotAdultClient(newAccount.getPesel()))
            throw new NotAdultClient();
        if (accountExists(newAccount.getPesel()))
            throw new AccountExists();
        Account account = repo.save(newAccount);
        return account;
    }

    @GetMapping("/account/{pesel}")
    Account findOne(@PathVariable String pesel) throws Exception {
        return repo.findById(pesel)
                .orElseThrow(() -> new Exception(pesel));
    }

    // Map contains keys: currencyFrom, currencyTo, value
    @PatchMapping("/account/{pesel}")
    @Transactional
    Optional<Account> patch(@RequestBody Map<String, String> update, @PathVariable String pesel) throws InvalidParam {

        String currencyFrom = update.get("currencyFrom");
        String currencyTo = update.get("currencyTo");
        BigDecimal value;
        BigDecimal calculatedValue;
        try {
            value = new BigDecimal(update.get("value"));
        } catch (Exception e) {
            throw new InvalidParam();
        }
        Account account = null;
        if (!currencyFrom.equals(currencyTo)) {
            Optional<Account> opt = repo.findById(pesel);
            if (opt.isPresent()) {
                account = opt.get();
                if (AllowedCurrencies.areAllowed(currencyFrom, currencyTo)) {
                    BigDecimal valuePLN = new BigDecimal(account.getAmountPLN());
                    BigDecimal valueUSD = new BigDecimal(account.getAmountUSD());

                    if (("PLN".equals(currencyFrom) && (valuePLN.compareTo(value) >= 0))) {
                        valuePLN = valuePLN.subtract(value);
                        account.setAmountPLN(valuePLN.toString());
                        if (("USD".equals(currencyFrom))) {
                            calculatedValue =
                                    new BigDecimal(1).divide(getCurrentExchangeRate("USD"))
                                            .setScale(4, ROUND_HALF_UP)
                                            .multiply(value);
                            valueUSD = valueUSD.add(calculatedValue);
                            account.setAmountUSD(valueUSD.toString());
                        }
                    } else {
                        if (("USD".equals(currencyFrom) && (valueUSD.compareTo(value) >= 0))) {
                            valueUSD = valueUSD.subtract(value);
                            account.setAmountUSD(valueUSD.toString());
                            if (("PLN".equals(currencyFrom))) {
                                calculatedValue = getCurrentExchangeRate("USD").multiply(value);
                                valuePLN = valuePLN.add(calculatedValue);
                                account.setAmountPLN(valuePLN.toString());
                            }
                        }
                    }
                }
            }
        }
        if (account != null)
            return Optional.of(account);
        else
            return Optional.empty();
    }

    private boolean isNotAdultClient(String pesel) throws InvalidParam {

        final int adultAdgeInYears = 18;

        try {
            Pesel peselObj = new Pesel(pesel);
            return !peselObj.getBirthDate().plusYears(adultAdgeInYears).isBefore(LocalDate.now());
        } catch (InvalidPesel e) {
            throw new InvalidParam(e.getMessage());
        }
    }

    private boolean accountExists(String pesel) {
        return repo.findById(pesel).isPresent();
    }

    private BigDecimal getCurrentExchangeRate(String currencyName) throws InvalidParam {

        final String endpoint = "https://api.nbp.pl/api/exchangerates/rates/a/" + currencyName.toLowerCase() + "/";

        RestTemplate restTemplate = new RestTemplate();

        final ExchangeRatesSeries responseBody = restTemplate.getForObject(
                endpoint, ExchangeRatesSeries.class);

        for (Rate rate : responseBody.getRates()) {
            if (rate.getEffectiveDate().equals(LocalDate.now().format(DateTimeFormatter.ofPattern("RRRR-MM-dd"))))
                return new BigDecimal(rate.getMid());
        }
        throw new InvalidParam();
    }

}
