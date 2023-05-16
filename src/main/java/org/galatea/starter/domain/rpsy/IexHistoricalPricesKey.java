package org.galatea.starter.domain.rpsy;

import java.io.Serializable;
import java.util.Date;

public class IexHistoricalPricesKey implements Serializable {
    private String symbol;
    private Date date;

    public IexHistoricalPricesKey(String symbol, Date date){
        this.symbol = symbol;
        this.date = date;
    }

    public IexHistoricalPricesKey(){

    }
}
