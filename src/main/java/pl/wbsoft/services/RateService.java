package pl.wbsoft.services;

import pl.wbsoft.exception.ExternalExchangeServiceException;

import java.math.BigDecimal;

public interface RateService {
    
    BigDecimal getCurrentExchangeRate(String currencyName) throws ExternalExchangeServiceException;
}
