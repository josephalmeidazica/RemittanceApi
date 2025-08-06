package com.api.remittance.Services;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.api.remittance.Responses.PriceCurrencyResponse;
@Service
public class PriceService {
    private final RestTemplate restTemplate;
    private final DateTimeFormatter formatter;

    public PriceService() {
        this.restTemplate = new RestTemplate();
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    }

    @Cacheable(value = "priceCache", key = "#today", condition = "!#this.isWeekEnd(#today)")
    public Double getPrice() {
        LocalDate today = LocalDate.now();
        if (isWeekEnd(today)) {
            
            return 0.0;
        }
        String url = "https://olinda.bcb.gov.br/olinda/servico/PTAX/versao/v1/odata/CotacaoDolarDia(dataCotacao=@dataCotacao)?@dataCotacao='"
        + today.format(formatter) +"'&$top=100&$format=json&$select=cotacaoCompra";
        try {
            PriceCurrencyResponse price = restTemplate.getForObject(url, PriceCurrencyResponse.class);
            return price != null ? price.value.getFirst().cotacaoCompra : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }

    public boolean isWeekEnd(LocalDate localDate)
    {
        String dayOfWeek = localDate.getDayOfWeek().toString();
        if("SATURDAY".equalsIgnoreCase(dayOfWeek)||
        "SUNDAY".equalsIgnoreCase(dayOfWeek))
        {
            return true;
        }
        return false;
    }
}
