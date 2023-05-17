package org.galatea.starter.domain;

import lombok.*;
import org.galatea.starter.domain.rpsy.IexHistoricalPricesKey;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.xml.bind.annotation.XmlRootElement;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC) // For spring and jackson
@Data
@Builder
@Entity
@IdClass(IexHistoricalPricesKey.class)
@XmlRootElement(name = "IexHistoricalPrices")
public class IexHistoricalPrices {

  @Id
  protected String symbol;

  protected BigDecimal close;
  protected BigDecimal high;
  protected BigDecimal low;
  protected BigDecimal open;
  protected Integer volume;
  protected Date date;
}
