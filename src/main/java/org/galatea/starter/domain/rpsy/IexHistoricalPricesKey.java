package org.galatea.starter.domain.rpsy;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class IexHistoricalPricesKey implements Serializable {
    public String symbol;
    public Date date;

    public IexHistoricalPricesKey(String symbol, Date date){
        this.symbol = symbol;
        this.date = date;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj.getClass().equals(this.getClass()))){
            return false;
        }
        IexHistoricalPricesKey key = (IexHistoricalPricesKey) obj;

        return key.symbol.equals(this.symbol) && key.date.equals(this.date);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(symbol).append(date).toHashCode();
    }
}
