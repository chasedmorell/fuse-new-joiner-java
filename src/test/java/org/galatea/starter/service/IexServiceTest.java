package org.galatea.starter.service;

import org.galatea.starter.ASpringTest;
import org.galatea.starter.domain.IexHistoricalPrices;
import org.galatea.starter.domain.rpsy.IexHistoricalPricesKey;
import org.galatea.starter.domain.rpsy.IexHistoricalPricesRpsy;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Verify.verify;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;

public class IexServiceTest extends ASpringTest {
    @Mock
    private IexClient mockIexClient;

    @Mock
    IexHistoricalPricesRpsy mockIexHistoricalPricesRpsy;

    private IexService service;

    @Before
    public void setup() {
        service = new IexService(mockIexClient, mockIexHistoricalPricesRpsy);
    }

    @Test
    public void testGetHistoricalPricesForSymbol(){

        Mockito.verify(mockIexClient, times(0)).getHistoricalPricesForSymbol("IBM","2d");
        service.getHistoricalPricesForSymbol("IBM", "2d");
        Mockito.verify(mockIexClient, times(1)).getHistoricalPricesForSymbol("IBM","2d");

        IexHistoricalPrices price = new IexHistoricalPrices(new IexHistoricalPricesKey("IBM",new Date()));
        price.setClose(BigDecimal.valueOf(1.00));

        Mockito.when(mockIexHistoricalPricesRpsy.findById(any())).thenReturn(Optional.of(price));

        service.getHistoricalPricesForSymbol("IBM", "2d");

        //IexClient.getHistoricalPricesForSymbol() should only be invoked 1 time.
        //When getting prices the second time, the service should use cached data,
        //so no need to query the external API
        Mockito.verify(mockIexClient, times(1)).getHistoricalPricesForSymbol("IBM","2d");

    }



}
