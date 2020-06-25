package pl.wbsoft.services;

import pl.wbsoft.errors.InvalidParamException;

import java.math.BigDecimal;

public interface RateService {

    BigDecimal getCurrentExchangeRate(String currencyName) throws InvalidParamException;
}
