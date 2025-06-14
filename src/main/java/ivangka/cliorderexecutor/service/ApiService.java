package ivangka.cliorderexecutor.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    private final BybitApiMarketRestClient bybitApiMarketRestClient;
    private final BybitApiTradeRestClient bybitApiTradeRestClient;
    private final BybitApiPositionRestClient bybitApiPositionRestClient;

    @Autowired
    public ApiService(BybitApiMarketRestClient bybitApiMarketRestClient,
                      BybitApiTradeRestClient bybitApiTradeRestClient,
                      BybitApiPositionRestClient bybitApiPositionRestClient) {
        this.bybitApiMarketRestClient = bybitApiMarketRestClient;
        this.bybitApiTradeRestClient = bybitApiTradeRestClient;
        this.bybitApiPositionRestClient = bybitApiPositionRestClient;
    }

    // create market order
    public String createMarketOrder(String symbol, String side, String orderSize, String stopLoss, String takeProfit) {
        Map<String, Object> orderParams = Map.of(
                "category", CategoryType.LINEAR,
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

    // create market order without tp
    public String createMarketOrder(String symbol, String side, String orderSize, String stopLoss) {
        Map<String, Object> orderParams = Map.of(
                "category", CategoryType.LINEAR,
                "symbol", symbol,
                "isLeverage", "1",
                "side", side,
                "orderType", "Market",
                "qty", orderSize,
                "stopLoss", stopLoss
        );
        Object response = bybitApiTradeRestClient.createOrder(orderParams);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
    }

    // create limit order
    public String createLimitOrder(String symbol, String side, String orderSize, String price,
                                   String stopLoss, String takeProfit) {
        Map<String, Object> orderParams = Map.of(
                "category", CategoryType.LINEAR,
                "symbol", symbol,
                "isLeverage", "1",
                "side", side,
                "orderType", "Limit",
                "qty", orderSize,
                "price", price,
                "stopLoss", stopLoss,
                "takeProfit", takeProfit
        );
        Object response = bybitApiTradeRestClient.createOrder(orderParams);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
    }

    // create limit order without tp
    public String createLimitOrder(String symbol, String side, String orderSize, String price, String stopLoss) {
        Map<String, Object> orderParams = Map.of(
                "category", CategoryType.LINEAR,
                "symbol", symbol,
                "isLeverage", "1",
                "side", side,
                "orderType", "Limit",
                "qty", orderSize,
                "price", price,
                "stopLoss", stopLoss
        );
        Object response = bybitApiTradeRestClient.createOrder(orderParams);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
    }

    // get last price
    public String lastPrice(String symbol) throws UnknownSymbolException {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiMarketRestClient.getMarketTickers(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        if (!responseMap.get("retCode").toString().equals("0")) {
            throw new UnknownSymbolException("The symbol wasn't found");
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        Map<String, Object> firstItem = list.get(0);
        return (String) firstItem.get("lastPrice");
    }

    // get number of decimal places in order
    public String orderSizeStep(String symbol) throws UnknownSymbolException {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiMarketRestClient.getInstrumentsInfo(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        if (!responseMap.get("retCode").toString().equals("0")) {
            throw new UnknownSymbolException("The symbol wasn't found");
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        Map<String, Object> firstItem = list.get(0);
        Map<String, Object> lotSizeFilter = (Map<String, Object>) firstItem.get("lotSizeFilter");
        return (String) lotSizeFilter.get("qtyStep");
    }

    // get the max leverage of the trading pair
    public String maxLeverage(String symbol) throws UnknownSymbolException {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiMarketRestClient.getRiskLimit(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        if (!responseMap.get("retCode").toString().equals("0")) {
            throw new UnknownSymbolException("The symbol wasn't found");
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        Map<String, Object> firstItem = list.get(0);
        return (String) firstItem.get("maxLeverage");
    }

    // set the leverage for the trading pair
    public String setLeverage(String symbol, String leverage) {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .buyLeverage(leverage)
                .sellLeverage(leverage)
                .build();
        Object response = bybitApiPositionRestClient.setPositionLeverage(request);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
    }

}
