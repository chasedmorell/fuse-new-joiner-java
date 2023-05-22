package org.galatea.starter.domain;

import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Function;

public enum RangeType {
    DAYS("d", rangeValue -> LocalDate.now().minusDays(rangeValue)) {
    },
    MONTHS("m", rangeValue -> LocalDate.now().minusMonths(rangeValue)) {
    },
    YEARS("y", rangeValue -> LocalDate.now().minusYears(rangeValue)) {
    },
    QUARTERS("q", rangeValue -> LocalDate.now().minusMonths(rangeValue*3)) {
    };

    private final String type;
    public final Function<Integer, LocalDate> subtract;


    RangeType(String type, Function<Integer, LocalDate> subtract) {
        this.type = type;
        this.subtract = subtract;
    }

    /**
     * Returns the RangeType based on string value, if one exists.
     *
     * @param type String representation of range type. (d, m, y, q)
     * @return An optional RangeType
     */
    public static Optional<RangeType> fromString(String type){
        for(RangeType r: RangeType.values()){
            if(r.type.equals(type)){
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }


}
