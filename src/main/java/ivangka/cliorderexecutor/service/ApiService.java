package ivangka.cliorderexecutor.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
import ivangka.cliorderexecutor.model.Instrument;
import ivangka.cliorderexecutor.model.Position;
import ivangka.cliorderexecutor.model.RiskLimit;
import ivangka.cliorderexecutor.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
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

    // close position by symbol
    public String closePositions(String symbol, String side, String size) {
        Map<String, Object> orderParams = Map.of(
                "category", CategoryType.LINEAR,
                "symbol", symbol,
                "side", side,
                "orderType", "Market",
                "qty", size,
                "reduceOnly", true
        );
        Object response = bybitApiTradeRestClient.createOrder(orderParams);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
    }

    // cancel all limit orders for specified pair
    public String cancelOrders(String symbol) {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiTradeRestClient.cancelAllOrder(request);

        // get and return retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        Object retCode = responseMap.get("retCode");
        return retCode.toString();
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

    // [max leverage]
    public RiskLimit riskLimit(String symbol) throws UnknownSymbolException {
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
        String maxLeverage = (String) firstItem.get("maxLeverage");

        RiskLimit riskLimit = new RiskLimit();
        riskLimit.setMaxLeverage(maxLeverage);
        return riskLimit;
    }

    // [last price]
    public Ticker ticker(String symbol) throws UnknownSymbolException {
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
        String lastPrice = (String) firstItem.get("lastPrice");

        Ticker ticker = new Ticker();
        ticker.setLastPrice(lastPrice);
        return ticker;
    }

    // [min order qty, qty step]
    public Instrument instrumentInfo(String symbol) throws UnknownSymbolException {
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
        String minOrderQty = (String) lotSizeFilter.get("minOrderQty");
        String qtyStep = (String) lotSizeFilter.get("qtyStep");

        Instrument instrument = new Instrument();
        instrument.setMinOrderQty(minOrderQty);
        instrument.setQtyStep(qtyStep);
        return instrument;
    }

    // get positions by symbol
    public List<Position> positions(String symbol) throws UnknownSymbolException {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiPositionRestClient.getPositionInfo(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        if (!responseMap.get("retCode").toString().equals("0")) {
            throw new UnknownSymbolException("The symbol wasn't found");
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");

        List<Position> positions = new LinkedList<>();
        Position position;
        for (Map<String, Object> item : list) {
            position = new Position();
            position.setSymbol((String) item.get("symbol"));
            position.setSide((String) item.get("side"));
            position.setSize((String) item.get("size"));
            position.setAvgPrice((String) item.get("avgPrice"));
            position.setLeverage((String) item.get("leverage"));
            position.setLiqPrice((String) item.get("liqPrice"));
            position.setStopLoss((String) item.get("stopLoss"));
            position.setTakeProfit((String) item.get("takeProfit"));
            positions.add(position);
        }
        return positions;
    }

}
