package org.galatea.starter.domain.rpsy;

import lombok.*;
import org.galatea.starter.domain.RangeType;
import org.galatea.starter.utils.Helpers;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class IexHistoricalPricesKey implements Serializable {
    public String symbol;
    public Date date;

    /**
     * Returns a list of IexHistoricalPricesKeys for the given symbol and range.
     * For example, a range of 5d will return keys for that symbol's last 5 days
     *
     * @param symbol stock symbol to get historical prices for.
     * @param range the range of the historical data.
     * @return a list of IexHistoricalPricesKeys
     */
    public static List<IexHistoricalPricesKey> keysForRange(final String symbol, final String range){
        LocalDate sinceDate;
        Integer rangeValue = Integer.parseInt(range.replaceAll("[^0-9]",""));

        Optional<RangeType> type = RangeType.fromString(String.valueOf(range.charAt(range.length()-1)));

        if(type.isPresent()){
            sinceDate = type.get().subtract(rangeValue);
        }else{
            throw new IllegalArgumentException();
        }

        //determine the days of historicalPrice data needed based on range query
        List<Date> daysSinceDate = Helpers.getDaysSinceDate(LocalDate.now(), sinceDate);

        //create a list of primary keys, which will be used in cache query.
        List<IexHistoricalPricesKey> keys = new ArrayList<>();
        for(Date date : daysSinceDate){
            keys.add(new IexHistoricalPricesKey(symbol, date));
        }
        return keys;
    }
}
