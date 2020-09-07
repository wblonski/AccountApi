package pl.wbsoft.services;

import pl.wbsoft.exceptions.ExternalExchangeServiceException;

import java.math.BigDecimal;

public interface RateService {
    
    BigDecimal getCurrentExchangeRate(String currencyName) throws ExternalExchangeServiceException;
}
