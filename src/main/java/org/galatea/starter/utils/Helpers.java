package org.galatea.starter.utils;

import static org.springframework.util.ReflectionUtils.doWithMethods;
import static org.springframework.util.ReflectionUtils.invokeMethod;

import java.lang.reflect.Modifier;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.builder.DiffBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.springframework.util.ReflectionUtils.MethodFilter;


@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Helpers {

  /**
   * Compares the values of all methods between the left-hand-side and right-hand-side. Checks for
   * methods that begin with 'get' and 'is'.
   */
  public static DiffResult diff(final Object lhs, final Object rhs) {
    return diff(lhs, rhs,
        method -> (method.getName().startsWith("get") || method.getName().startsWith("is"))
            && Modifier.isPublic(method.getModifiers()));
  }

  /**
   * Compares the values of all methods between the left-hand-side and right-hand-side. Uses the
   * provided MethodFilter to determine which methods to check.
   */
  public static DiffResult diff(final Object lhs, final Object rhs, final MethodFilter filter) {
    final DiffBuilder builder = new DiffBuilder(lhs, rhs, ToStringStyle.SHORT_PREFIX_STYLE);

    if (!lhs.getClass().isAssignableFrom(rhs.getClass())) {
      throw new IllegalArgumentException("lhs is not assignable from rhs");
    }

    doWithMethods(lhs.getClass(), method -> builder.append(method.getName(),
        invokeMethod(method, lhs), invokeMethod(method, rhs)), filter);

    return builder.build();
  }

  public static List<Date> getBusinessDaysSinceDate(final LocalDate startDate) {

    final LocalDate endDate = LocalDate.now();

    // Predicate 2: Is a given date is a weekday
    Predicate<LocalDate> isWeekend = date -> date.getDayOfWeek() == DayOfWeek.SATURDAY
            || date.getDayOfWeek() == DayOfWeek.SUNDAY;

    // Iterate over stream of all dates and check each day against any weekday or holiday
    List<LocalDate> businessDays = startDate.datesUntil(endDate)
            .filter(isWeekend.negate())
            .collect(Collectors.toList());

    List<Date> businessDaysDate = new ArrayList<Date>();

    for (int i = 0; i < businessDays.size(); i++) {
      Date date = Date.from(businessDays.get(i).atStartOfDay(ZoneId.of("Etc/UTC")).toInstant());
      System.out.println(businessDays.get(i));
      businessDaysDate.add(date);
    }

    return businessDaysDate;
  }
}
