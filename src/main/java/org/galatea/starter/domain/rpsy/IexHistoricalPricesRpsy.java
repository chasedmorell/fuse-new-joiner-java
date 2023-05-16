package org.galatea.starter.domain.rpsy;

import org.galatea.starter.domain.IexHistoricalPrices;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IexHistoricalPricesRpsy extends CrudRepository<IexHistoricalPrices, IexHistoricalPricesKey> {

  /**
   * Retrieves all entities with the given symbol.
   */
  List<IexHistoricalPrices> findBySymbol(String symbol);

  @Override
  List<IexHistoricalPrices> findAllById(Iterable<IexHistoricalPricesKey> ids);

  /**
   * 'p0' required in key because java does not retain parameter names during compilation unless
   * specified. You must use position parameter bindings otherwise.
   */
  @Override
  <S extends IexHistoricalPrices> S save(S entity);

  @Override
  <S extends IexHistoricalPrices> Iterable<S> saveAll(Iterable<S> prices);
}
