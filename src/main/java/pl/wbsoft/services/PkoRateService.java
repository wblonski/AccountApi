package pl.wbsoft.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.wbsoft.entities.ExchangeRatesSeries;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PkoRateService implements RateService {

    private static final String PKO_RATES_URI = "https://api.nbp.pl/api/exchangerates/rates/a/";

    private final RestTemplate restTemplate;

    public PkoRateService() {
        this.restTemplate = new RestTemplate();

    }

    @Override
    public BigDecimal getCurrentExchangeRate(String currencyName) {

        final String endpoint = PKO_RATES_URI + currencyName.toLowerCase() + "/";

        ExchangeRatesSeries responseBody = restTemplate.getForObject(endpoint, ExchangeRatesSeries.class);
        return new BigDecimal(responseBody.getRates().get(0).getMid()).setScale(4, RoundingMode.HALF_UP);
    }
}
