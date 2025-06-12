package ivangka.cliorderexecutor.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    private final BybitApiMarketRestClient bybitApiMarketRestClient;
    private final BybitApiTradeRestClient bybitApiTradeRestClient;

    @Autowired
    public ApiService(BybitApiMarketRestClient bybitApiMarketRestClient,
                      BybitApiTradeRestClient bybitApiTradeRestClient) {
        this.bybitApiMarketRestClient = bybitApiMarketRestClient;
        this.bybitApiTradeRestClient = bybitApiTradeRestClient;
    }

    // create market order (linear contract)
    public String createOrder(String symbol, String orderSize, String stopLoss, String takeProfit) {
        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal takeProfitBD = new BigDecimal(takeProfit);
        String side = stopLossBD.compareTo(takeProfitBD) < 0 ? "Buy" : "Sell";
        Map<String, Object> orderParams = Map.of(
                "category", "linear",
                "symbol", symbol,
                "isLeverage", "1",
                "side", side,
                "orderType", "Market",
                "qty", orderSize,
                "stopLoss", stopLoss,
                "takeProfit", takeProfit
        );
        Object response = bybitApiTradeRestClient.createOrder(orderParams);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
    }

    // get last price (linear contract)
    public String lastPrice(String symbol) {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object rawResponse = bybitApiMarketRestClient.getMarketTickers(request);

        Map<String, Object> response = (Map<String, Object>) rawResponse;
        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        Map<String, Object> firstItem = list.get(0);
        return (String) firstItem.get("lastPrice");
    }

    // get number of decimal places in order
    public String orderSizeStep(String symbol) {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object rawResponse = bybitApiMarketRestClient.getInstrumentsInfo(request);

        Map<String, Object> response = (Map<String, Object>) rawResponse;
        Map<String, Object> result = (Map<String, Object>) response.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        Map<String, Object> firstItem = list.get(0);
        Map<String, Object> lotSizeFilter = (Map<String, Object>) firstItem.get("lotSizeFilter");
        return (String) lotSizeFilter.get("qtyStep");
    }

}
