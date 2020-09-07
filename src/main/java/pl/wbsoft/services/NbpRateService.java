package pl.wbsoft.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wbsoft.entities.ExchangeRatesData;
import pl.wbsoft.entities.Rate;
import pl.wbsoft.exceptions.ExternalExchangeServiceException;

import java.math.BigDecimal;
import java.util.List;

import static java.lang.String.format;
import static pl.wbsoft.services.AccountServiceImpl.ROUND_TYPE;

@Service
public class NbpRateService implements RateService {
    
    private static final String NBP_NO_RATE_FOR_THIS_CURRENCY = "There is not defined currency rate for \"%s\" in NBP.";
    private static final String NBP_RATES_URI = "https://api.nbp.pl/api/exchangerates/rates/a/";
    
    private final RestTemplate restTemplate;
    
    public NbpRateService() {
        this.restTemplate = new RestTemplate();
        
    }
    
    @Override
    public BigDecimal getCurrentExchangeRate(String currencyName) throws ExternalExchangeServiceException {
        
        final String endpointUri = NBP_RATES_URI + currencyName.toLowerCase();
        ExchangeRatesData responseBody;
        try {
            responseBody = restTemplate.getForObject(endpointUri, ExchangeRatesData.class);
        } catch (Exception ex) {
            throw new ExternalExchangeServiceException(ex.getMessage());
        }
        List<Rate> rates = responseBody.getRates();
        if (rates == null || rates.isEmpty())
            throw new ExternalExchangeServiceException(format(NBP_NO_RATE_FOR_THIS_CURRENCY, currencyName));
        
        return new BigDecimal(rates.get(0).getMid()).setScale(4, ROUND_TYPE);
    }
}
