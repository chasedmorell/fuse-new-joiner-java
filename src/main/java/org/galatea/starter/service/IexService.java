package org.galatea.starter.service;

import java.math.BigDecimal;
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
  public List<IexHistoricalPrices> getHistoricalPricesForSymbol(final String symbol, final String range) {

      LocalDate sinceDate;
      Integer rangeValue = Integer.parseInt(range.replaceAll("[^0-9]",""));

      switch(range.charAt(range.length()-1)){
          case 'd': sinceDate = LocalDate.now().minusDays(rangeValue); break;
          case 'm': sinceDate = LocalDate.now().minusMonths(rangeValue); break;
          case 'y': sinceDate = LocalDate.now().minusYears(rangeValue); break;
          case 'q': sinceDate = LocalDate.now().minusMonths(rangeValue*3); break;
          default: return new ArrayList<IexHistoricalPrices>();
      }

      //determine the days of historicalPrice data needed based on range query
      List<Date> daysSinceDate = Helpers.getDaysSinceDate(LocalDate.now(), sinceDate);

      //create a list of primary keys, which will be used in cache query.
      List<IexHistoricalPricesKey> keys = new ArrayList<>();
      for(Date date : daysSinceDate){
          keys.add(new IexHistoricalPricesKey(symbol, date));
      }

      //find all the historicalPrice records, if they exist, in cache database.
      //when a record isn't found in cache, immediately stop querying cache.
      List<IexHistoricalPrices> cachedHistoricalPrices = new ArrayList<>();
      Boolean allDatesFoundInCache = true;

      for(IexHistoricalPricesKey key: keys){
          Optional<IexHistoricalPrices> price = iexHistoricalPricesRpsy.findById(key);
          //Price records for days that market is closed (weekends, etc) have null values.
          //Don't include these in the response.
          if(price.isPresent()){
              if((price.get().getClose().isPresent())){
                  cachedHistoricalPrices.add(price.get());
              }
          }else{
              allDatesFoundInCache = false;
              break;
          }
      }

      //if the cache query returns fewer documents than required, we'll need to query the API instead.
      if(!allDatesFoundInCache){

          List<IexHistoricalPrices> historicalPrices = iexClient.getHistoricalPricesForSymbol(symbol, range);

          //Add HistoricalPrices for days when market is closed into this list.
          List<IexHistoricalPrices> historicalPricesToCache = new ArrayList<>(historicalPrices);

          Set<IexHistoricalPricesKey> requiredKeys = new HashSet<>(keys);

          //get keys of all records returned by IEX API query
          Set<IexHistoricalPricesKey> receivedKeys = new HashSet<>();
          for(IexHistoricalPrices price: historicalPrices){
              receivedKeys.add(price.key());
          }

          //Get the difference between the record keys received from IEX API and all the records needed.
          //(Weekend values aren't returned by IEX API, but are needed for cache)
          requiredKeys.removeAll(receivedKeys);

          //add the required historicalPrice records to the list of historicalPrices to save to cache
          for(IexHistoricalPricesKey key: requiredKeys){
              historicalPricesToCache.add(new IexHistoricalPrices(key));
          }

          iexHistoricalPricesRpsy.saveAll(historicalPricesToCache);
          return historicalPrices;
      }

      return cachedHistoricalPrices;

  }


}
