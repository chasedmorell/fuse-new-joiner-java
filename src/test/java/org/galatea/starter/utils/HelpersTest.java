package org.galatea.starter.utils;

import static org.junit.Assert.assertEquals;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.builder.DiffResult;
import org.junit.Test;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@ToString
@EqualsAndHashCode
public class HelpersTest {

  public static class SimpleObject {

    private String x = "X";

    void setX(String x) {
      this.x = x;
    }

    public String getX() {
      return x;
    }

    // Starts with "get" but is private, so it shouldn't be included in the diff
    private long getNanoTime() {
      return System.nanoTime();
    }
  }

  @Test
  public void testNoDiff() {

    SimpleObject lhs = new SimpleObject();
    SimpleObject rhs = new SimpleObject();

    DiffResult diffResult = Helpers.diff(lhs, rhs);
    assertEquals("", diffResult.toString());
  }

  @Test
  public void testDiff() {

    SimpleObject lhs = new SimpleObject();
    SimpleObject rhs = new SimpleObject();
    rhs.setX("Y");

    DiffResult diffResult = Helpers.diff(lhs, rhs);
    assertEquals("HelpersTest.SimpleObject[getX=X] differs from HelpersTest.SimpleObject[getX=Y]",
        diffResult.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testExceptionDiff() {
    String lhs = "hi";
    SimpleObject rhs = new SimpleObject();
    Helpers.diff(lhs, rhs);
  }

  @Test
  public void testGetDaysSinceDate(){
    LocalDate endDate = LocalDate.of(2023,5,17);
    LocalDate startDate = endDate.minusDays(5);
    List<Date> businessDaysSinceDate = Helpers.getDaysSinceDate(endDate,startDate);
    assertEquals(businessDaysSinceDate.size(),5);
  }
}
