package org.galatea.starter.domain;

import java.time.LocalDate;
import java.util.Optional;

public enum RangeType {
    DAYS("d") {
        public LocalDate subtract(Integer rangeValue) {
            return LocalDate.now().minusDays(rangeValue);
        }
    },
    MONTHS("m") {
        public LocalDate subtract(Integer rangeValue) {
            return LocalDate.now().minusMonths(rangeValue);
        }
    },
    YEARS("y") {
        public LocalDate subtract(Integer rangeValue) {
            return LocalDate.now().minusYears(rangeValue);
        }
    },
    QUARTERS("q") {
        public LocalDate subtract(Integer rangeValue) {
            return LocalDate.now().minusMonths(rangeValue * 3L);
        }
    };

    private final String type;

    RangeType(String type) {
        this.type = type;
    }

    public static Optional<RangeType> fromString(String type){
        for(RangeType r: RangeType.values()){
            if(r.type.equals(type)){
                return Optional.of(r);
            }
        }
        return Optional.empty();
    }

    public abstract LocalDate subtract(Integer rangeValue);


}
