package org.galatea.starter.service;

import java.time.LocalDate;
import java.util.*;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.galatea.starter.domain.IexHistoricalPrices;
import org.galatea.starter.domain.IexLastTradedPrice;
import org.galatea.starter.domain.IexSymbol;
import org.galatea.starter.domain.rpsy.IexHistoricalPricesKey;
import org.galatea.starter.domain.rpsy.IexHistoricalPricesRpsy;
import org.galatea.starter.utils.Helpers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

/**
 * A layer for transformation, aggregation, and business required when retrieving data from IEX.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class IexService {

  @NonNull
  private IexClient iexClient;

  @NonNull
  @Autowired
  private IexHistoricalPricesRpsy iexHistoricalPricesRpsy;

  /**
   * Get all stock symbols from IEX.
   *
   * @return a list of all Stock Symbols from IEX.
   */
  public List<IexSymbol> getAllSymbols() {
    return iexClient.getAllSymbols();
  }

  /**
   * Get the last traded price for each Symbol that is passed in.
   *
   * @param symbols the list of symbols to get a last traded price for.
   * @return a list of last traded price objects for each Symbol that is passed in.
   */
  public List<IexLastTradedPrice> getLastTradedPriceForSymbols(final List<String> symbols) {
    if (CollectionUtils.isEmpty(symbols)) {
      return Collections.emptyList();
    } else {
      return iexClient.getLastTradedPriceForSymbols(symbols.toArray(new String[0]));
    }
  }

  /**
   * Get the historical prices for a symbol
   *
   * @param symbol stock symbol to get historical prices for.
   * @param range the range of the historical data.
   * @return a list of IexHistoricalPrices objects
   */
  public List<IexHistoricalPrices> getHistoricalPricesForSymbol(final String symbol, final Integer range) {

      //determine the days of historicalPrice data needed based on range query
      LocalDate sinceDate = LocalDate.now().minusDays(range);
      List<Date> businessDaysSinceDate = Helpers.getBusinessDaysSinceDate(LocalDate.now(), sinceDate);

      //create a list of primary keys, which will be used in cache query.
      List<IexHistoricalPricesKey> keys = new ArrayList<>();
      for(Date date : businessDaysSinceDate){
          keys.add(new IexHistoricalPricesKey(symbol, date));
      }

      //find all the historicalPrice records, if they exist, in cache database.
      List<IexHistoricalPrices> cachedHistoricalPrices = iexHistoricalPricesRpsy.findAllById(keys);

      //if the cache query returns fewer documents than required, we'll need to query the API instead.
      if(cachedHistoricalPrices.size() < businessDaysSinceDate.size()){
          List<IexHistoricalPrices> historicalPrices = iexClient.getHistoricalPricesForSymbol(symbol, range.toString().concat("d"));
          iexHistoricalPricesRpsy.saveAll(historicalPrices);
          return historicalPrices;
      }

      return cachedHistoricalPrices;

  }


}
