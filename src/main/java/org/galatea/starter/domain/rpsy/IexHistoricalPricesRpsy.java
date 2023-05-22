package org.galatea.starter.domain.rpsy;

import org.galatea.starter.domain.IexHistoricalPrices;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IexHistoricalPricesRpsy extends CrudRepository<IexHistoricalPrices, IexHistoricalPricesKey> {

  /**
   * Retrieves all entities with the given symbol.
   */
  List<IexHistoricalPrices> findBySymbol(String symbol);

  @Override
  List<IexHistoricalPrices> findAllById(Iterable<IexHistoricalPricesKey> ids);

  @Override
  Optional<IexHistoricalPrices> findById(IexHistoricalPricesKey id);


  @Override
  <S extends IexHistoricalPrices> S save(S entity);

  @Override
  <S extends IexHistoricalPrices> Iterable<S> saveAll(Iterable<S> prices);
}
