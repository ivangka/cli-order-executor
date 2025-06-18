package ivangka.cliorderexecutor.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import ivangka.cliorderexecutor.exception.BadRetCodeException;
import ivangka.cliorderexecutor.model.*;
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
    public void createMarketOrder(String symbol, String side, String orderSize, String stopLoss, String takeProfit)
            throws BadRetCodeException {
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

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // create market order without tp
    public void createMarketOrder(String symbol, String side, String orderSize, String stopLoss)
            throws BadRetCodeException {
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

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // create limit order
    public void createLimitOrder(String symbol, String side, String orderSize, String price,
                                   String stopLoss, String takeProfit) throws BadRetCodeException {
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

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // create limit order without tp
    public void createLimitOrder(String symbol, String side, String orderSize, String price, String stopLoss)
            throws BadRetCodeException {
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

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // close position by symbol
    public void closePosition(String symbol, String side, String size) throws BadRetCodeException {
        Map<String, Object> orderParams = Map.of(
                "category", CategoryType.LINEAR,
                "symbol", symbol,
                "side", side,
                "orderType", "Market",
                "qty", size,
                "reduceOnly", true
        );
        Object response = bybitApiTradeRestClient.createOrder(orderParams);

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // cancel all orders for specified pair
    public void cancelOrders(String symbol) throws BadRetCodeException {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiTradeRestClient.cancelAllOrder(request);

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // cancel all orders
    public void cancelOrders() throws BadRetCodeException {
        // USDT
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .build();
        Object response = bybitApiTradeRestClient.cancelAllOrder(request);

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }

        // USDC (PERP)
        request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDC")
                .build();
        response = bybitApiTradeRestClient.cancelAllOrder(request);

        // checking retCode from the response
        responseMap = (Map<?, ?>) response;
        retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // set the leverage for the trading pair
    public void setLeverage(String symbol, String leverage) throws BadRetCodeException {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .buyLeverage(leverage)
                .sellLeverage(leverage)
                .build();
        Object response = bybitApiPositionRestClient.setPositionLeverage(request);

        // checking retCode from the response
        Map<?, ?> responseMap = (Map<?, ?>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
    }

    // [max leverage]
    public RiskLimit riskLimit(String symbol) throws BadRetCodeException {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiMarketRestClient.getRiskLimit(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
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
    public Ticker ticker(String symbol) throws BadRetCodeException {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiMarketRestClient.getMarketTickers(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
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
    public Instrument instrumentInfo(String symbol) throws BadRetCodeException {
        var request = MarketDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiMarketRestClient.getInstrumentsInfo(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
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
    public List<Position> positions(String symbol) throws BadRetCodeException {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiPositionRestClient.getPositionInfo(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        List<Position> positions = new LinkedList<>();
        positions = fillPositions(positions, list);
        return positions;
    }

    // get all positions
    public List<Position> positions() throws BadRetCodeException {
        // USDT
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .build();
        Object response = bybitApiPositionRestClient.getPositionInfo(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        List<Position> positions = new LinkedList<>();
        positions = fillPositions(positions, list);

        // USDC (PERP)
        request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDC")
                .build();
        response = bybitApiPositionRestClient.getPositionInfo(request);

        responseMap = (Map<String, Object>) response;
        retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
        result = (Map<String, Object>) responseMap.get("result");
        list = (List<Map<String, Object>>) result.get("list");
        positions = fillPositions(positions, list);
        return positions;
    }

    // get open limit orders by symbol
    public List<Order> orders(String symbol) throws BadRetCodeException {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .build();
        Object response = bybitApiTradeRestClient.getOpenOrders(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        List<Order> orders = new LinkedList<>();
        orders = fillOrders(orders, list);
        return orders;
    }

    // get all limit orders
    public List<Order> orders() throws BadRetCodeException {
        // USDT
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .build();
        Object response = bybitApiTradeRestClient.getOpenOrders(request);

        Map<String, Object> responseMap = (Map<String, Object>) response;
        String retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
        Map<String, Object> result = (Map<String, Object>) responseMap.get("result");
        List<Map<String, Object>> list = (List<Map<String, Object>>) result.get("list");
        List<Order> orders = new LinkedList<>();
        orders = fillOrders(orders, list);

        // USDC (PERP)
        request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDC")
                .build();
        response = bybitApiTradeRestClient.getOpenOrders(request);

        responseMap = (Map<String, Object>) response;
        retCode = responseMap.get("retCode").toString();
        if (!retCode.equals("0")) {
            String retCodeMessage = BadRetCodeException.RETCODES.get(retCode);
            if (retCodeMessage != null) {
                throw new BadRetCodeException(retCodeMessage + " (retCode: " + retCode + ")");
            } else {
                throw new BadRetCodeException("Error (retCode: " + retCode + ")");
            }
        }
        result = (Map<String, Object>) responseMap.get("result");
        list = (List<Map<String, Object>>) result.get("list");
        orders = fillOrders(orders, list);
        return orders;
    }

    // fill positions
    private List<Position> fillPositions(List<Position> positions, List<Map<String, Object>> listResponse) {
        Position position;
        for (Map<String, Object> item : listResponse) {
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

    // fill limit orders
    private List<Order> fillOrders(List<Order> orders, List<Map<String, Object>> listResponse) {
        Order order;
        for (Map<String, Object> item : listResponse) {
            if (item.get("orderType").equals("Market")) {
                continue;
            }
            order = new Order();
            order.setSymbol((String) item.get("symbol"));
            order.setPrice((String) item.get("price"));
            order.setQuantity((String) item.get("qty"));
            order.setSide((String) item.get("side"));
            order.setOrderType((String) item.get("orderType"));
            order.setStopLoss((String) item.get("stopLoss"));
            order.setTakeProfit((String) item.get("takeProfit"));
            orders.add(order);
        }
        return orders;
    }

}
