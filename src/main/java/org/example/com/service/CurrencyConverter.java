package org.example.com.service;

import org.example.com.entity.enums.CurrencyCode;

import java.util.HashMap;
import java.util.Map;

/**
 * A utility class for converting between different currencies using predefined exchange rates.
 *
 * @author Natalie Werch
 */
public class CurrencyConverter {

    private static final Map<CurrencyCode, Map<CurrencyCode, Double>> currencyRateMap = new HashMap<>();

    static {
        currencyRateMap.put(CurrencyCode.USD, Map.of(CurrencyCode.EUR, 0.92, CurrencyCode.GBP, 0.78));
        currencyRateMap.put(CurrencyCode.EUR, Map.of(CurrencyCode.USD, 1.09, CurrencyCode.GBP, 0.85));
        currencyRateMap.put(CurrencyCode.GBP, Map.of(CurrencyCode.USD, 1.28, CurrencyCode.EUR, 1.17));
    }

    /**
     * Get the exchange rate from one currency to another.
     *
     * @param from - The source currency code.
     * @param to   - The target currency code.
     * @return The exchange rate from 'from' currency to 'to' currency.
     * @throws IllegalArgumentException If the exchange rate is not found for the specified currencies.
     */
    public static double getExchangeRate(CurrencyCode from, CurrencyCode to) {
        if (from == to) {
            return 1.0;
        } else if (currencyRateMap.containsKey(from) && currencyRateMap.get(from).containsKey(to)) {
            return currencyRateMap.get(from).get(to);
        } else {
            throw new IllegalArgumentException("Exchange rate not found for the specified currencies");
        }
    }
}
