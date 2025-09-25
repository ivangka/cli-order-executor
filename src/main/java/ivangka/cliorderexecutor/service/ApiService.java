package ivangka.cliorderexecutor.service;

import com.bybit.api.client.domain.CategoryType;
import com.bybit.api.client.domain.account.AccountType;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.position.TpslMode;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.OrderFilter;
import com.bybit.api.client.domain.trade.PositionIdx;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.restApi.BybitApiAccountRestClient;
import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiPositionRestClient;
import com.bybit.api.client.restApi.BybitApiTradeRestClient;
import ivangka.cliorderexecutor.exception.BadRetCodeException;
import ivangka.cliorderexecutor.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ApiService {

    private final BybitApiMarketRestClient bybitApiMarketRestClient;
    private final BybitApiTradeRestClient bybitApiTradeRestClient;
    private final BybitApiPositionRestClient bybitApiPositionRestClient;
    private final BybitApiAccountRestClient bybitApiAccountRestClient;

    @Autowired
    public ApiService(BybitApiMarketRestClient bybitApiMarketRestClient,
                      BybitApiTradeRestClient bybitApiTradeRestClient,
                      BybitApiPositionRestClient bybitApiPositionRestClient,
                      BybitApiAccountRestClient bybitApiAccountRestClient) {
        this.bybitApiMarketRestClient = bybitApiMarketRestClient;
        this.bybitApiTradeRestClient = bybitApiTradeRestClient;
        this.bybitApiPositionRestClient = bybitApiPositionRestClient;
        this.bybitApiAccountRestClient = bybitApiAccountRestClient;
    }

    // create market order
    public void createMarketOrder(String symbol, String side, String orderSize, String stopLoss, String takeProfit)
            throws BadRetCodeException {

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Market");
        orderParams.put("qty", orderSize);
        orderParams.put("stopLoss", stopLoss);
        orderParams.put("takeProfit", takeProfit);

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

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Market");
        orderParams.put("qty", orderSize);
        orderParams.put("stopLoss", stopLoss);

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

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Limit");
        orderParams.put("qty", orderSize);
        orderParams.put("price", price);
        orderParams.put("stopLoss", stopLoss);
        orderParams.put("takeProfit", takeProfit);

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

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Limit");
        orderParams.put("qty", orderSize);
        orderParams.put("price", price);
        orderParams.put("stopLoss", stopLoss);

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

    // create conditional market order
    public void createConditionalMarketOrder(String symbol, String side, String orderSize, String stopLoss,
                                             String takeProfit, int triggerDirection, String triggerPrice)
            throws BadRetCodeException {

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Market");
        orderParams.put("qty", orderSize);
        orderParams.put("triggerDirection", triggerDirection);
        orderParams.put("triggerPrice", triggerPrice);
        orderParams.put("stopLoss", stopLoss);
        orderParams.put("takeProfit", takeProfit);

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

    // create conditional market order without tp
    public void createConditionalMarketOrder(String symbol, String side, String orderSize, String stopLoss,
                                      int triggerDirection, String triggerPrice) throws BadRetCodeException {

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Market");
        orderParams.put("qty", orderSize);
        orderParams.put("triggerDirection", triggerDirection);
        orderParams.put("triggerPrice", triggerPrice);
        orderParams.put("stopLoss", stopLoss);

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

    // create conditional limit order
    public void createConditionalLimitOrder(String symbol, String side, String orderSize, String price,
                                     String stopLoss, String takeProfit, int triggerDirection, String triggerPrice)
            throws BadRetCodeException {

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Limit");
        orderParams.put("qty", orderSize);
        orderParams.put("price", price);
        orderParams.put("triggerDirection", triggerDirection);
        orderParams.put("triggerPrice", triggerPrice);
        orderParams.put("stopLoss", stopLoss);
        orderParams.put("takeProfit", takeProfit);

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

    // create conditional limit order without tp
    public void createConditionalLimitOrder(String symbol, String side, String orderSize, String price,
                                     String stopLoss, int triggerDirection, String triggerPrice)
            throws BadRetCodeException {

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Limit");
        orderParams.put("qty", orderSize);
        orderParams.put("price", price);
        orderParams.put("triggerDirection", triggerDirection);
        orderParams.put("triggerPrice", triggerPrice);
        orderParams.put("stopLoss", stopLoss);

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

    // create market order by quantity
    public void createMarketOrderByQuantity(String symbol, String side, String orderSize) throws BadRetCodeException {
        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Market");
        orderParams.put("qty", orderSize);

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

    // create limit order by quantity
    public void createLimitOrderByQuantity(String symbol, String side, String orderSize, String price)
            throws BadRetCodeException {

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("isLeverage", "1");
        orderParams.put("side", side);
        orderParams.put("orderType", "Limit");
        orderParams.put("qty", orderSize);
        orderParams.put("price", price);

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

        Map<String, Object> orderParams = new HashMap<>();
        orderParams.put("category", CategoryType.LINEAR);
        orderParams.put("symbol", symbol);
        orderParams.put("side", side);
        orderParams.put("orderType", "Market");
        orderParams.put("qty", size);
        orderParams.put("reduceOnly", true);

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

    // cancel limit orders for specified pair
    public void cancelLimitOrders(String symbol) throws BadRetCodeException {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .orderFilter(OrderFilter.ORDER)
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

    // cancel all limit orders
    public void cancelLimitOrders() throws BadRetCodeException {
        // USDT
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .orderFilter(OrderFilter.ORDER)
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
                .orderFilter(OrderFilter.ORDER)
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

    // cancel conditional orders for specified pair
    public void cancelConditionalOrders(String symbol) throws BadRetCodeException {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .orderFilter(OrderFilter.STOP_ORDER)
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

    // cancel all conditional orders
    public void cancelConditionalOrders() throws BadRetCodeException {
        // USDT
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .orderFilter(OrderFilter.STOP_ORDER)
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
                .orderFilter(OrderFilter.STOP_ORDER)
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

    // manage stop-loss
    public void manageStopLoss(String symbol, String price) throws BadRetCodeException {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .tpslMode(TpslMode.FULL)
                .positionIdx(PositionIdx.ONE_WAY_MODE)
                .stopLoss(price)
                .build();
        Object response = bybitApiPositionRestClient.setTradingStop(request);

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

    // manage take-profit
    public void manageTakeProfit(String symbol, String price) throws BadRetCodeException {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .tpslMode(TpslMode.FULL)
                .positionIdx(PositionIdx.ONE_WAY_MODE)
                .takeProfit(price)
                .build();
        Object response = bybitApiPositionRestClient.setTradingStop(request);

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

    // [totalWalletBalance, totalMarginBalance, totalAvailableBalance]
    public WalletBalance walletBalance() throws BadRetCodeException {
        var request = AccountDataRequest.builder()
                .accountType(AccountType.UNIFIED)
                .build();
        Object response = bybitApiAccountRestClient.getWalletBalance(request);

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
        String walletBal = (String) firstItem.get("totalWalletBalance");
        String marginBal = (String) firstItem.get("totalMarginBalance");
        String availableBal = (String) firstItem.get("totalAvailableBalance");

        WalletBalance walletBalance = new WalletBalance();
        walletBalance.setWalletBal(walletBal);
        walletBalance.setMarginBal(marginBal);
        walletBalance.setAvailableBal(availableBal);
        return walletBalance;
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

    // get placed limit orders by symbol
    public List<LimitOrder> limitOrders(String symbol) throws BadRetCodeException {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .orderFilter(OrderFilter.ORDER)
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
        List<LimitOrder> limitOrders = new LinkedList<>();
        limitOrders = fillLimitOrders(limitOrders, list);
        return limitOrders;
    }

    // get all placed limit orders
    public List<LimitOrder> limitOrders() throws BadRetCodeException {
        // USDT
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .orderFilter(OrderFilter.ORDER)
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
        List<LimitOrder> limitOrders = new LinkedList<>();
        limitOrders = fillLimitOrders(limitOrders, list);

        // USDC (PERP)
        request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDC")
                .orderFilter(OrderFilter.ORDER)
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
        limitOrders = fillLimitOrders(limitOrders, list);
        return limitOrders;
    }

    // get placed conditional orders by symbol
    public List<ConditionalOrder> conditionalOrders(String symbol) throws BadRetCodeException {
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol(symbol)
                .orderFilter(OrderFilter.STOP_ORDER)
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
        List<ConditionalOrder> conditionalOrders = new LinkedList<>();
        conditionalOrders = fillConditionalOrders(conditionalOrders, list);
        return conditionalOrders;
    }

    // get all placed conditional orders
    public List<ConditionalOrder> conditionalOrders() throws BadRetCodeException {
        // USDT
        var request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDT")
                .orderFilter(OrderFilter.STOP_ORDER)
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
        List<ConditionalOrder> conditionalOrders = new LinkedList<>();
        conditionalOrders = fillConditionalOrders(conditionalOrders, list);

        // USDC (PERP)
        request = TradeOrderRequest.builder()
                .category(CategoryType.LINEAR)
                .settleCoin("USDC")
                .orderFilter(OrderFilter.STOP_ORDER)
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
        conditionalOrders = fillConditionalOrders(conditionalOrders, list);
        return conditionalOrders;
    }

    // send test request
    public void testRequest() throws BadRetCodeException {
        var request = PositionDataRequest.builder()
                .category(CategoryType.LINEAR)
                .symbol("BTCUSDT")
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
    }

    // fill positions
    private List<Position> fillPositions(List<Position> positions, List<Map<String, Object>> listResponse) {
        Position position;
        for (Map<String, Object> item : listResponse) {
            position = new Position();

            String symbol = (String) item.get("symbol");
            if (symbol != null && symbol.endsWith("PERP")) {
                symbol = symbol.substring(0, symbol.length() - 4) + "USDC";
            }
            position.setSymbol(symbol);
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
    private List<LimitOrder> fillLimitOrders(List<LimitOrder> limitOrders, List<Map<String, Object>> listResponse) {
        LimitOrder order;
        for (Map<String, Object> item : listResponse) {
            order = new LimitOrder();

            String symbol = (String) item.get("symbol");
            if (symbol != null && symbol.endsWith("PERP")) {
                symbol = symbol.substring(0, symbol.length() - 4) + "USDC";
            }
            order.setSymbol(symbol);
            order.setPrice((String) item.get("price"));
            order.setQuantity((String) item.get("qty"));
            order.setSide((String) item.get("side"));
            order.setOrderType((String) item.get("orderType"));
            order.setStopLoss((String) item.get("stopLoss"));
            order.setTakeProfit((String) item.get("takeProfit"));
            limitOrders.add(order);
        }
        return limitOrders;
    }

    // fill conditional orders
    private List<ConditionalOrder> fillConditionalOrders(List<ConditionalOrder> conditionalOrders, List<Map<String,
            Object>> listResponse) {

        ConditionalOrder order;
        for (Map<String, Object> item : listResponse) {
            order = new ConditionalOrder();

            String symbol = (String) item.get("symbol");
            if (symbol != null && symbol.endsWith("PERP")) {
                symbol = symbol.substring(0, symbol.length() - 4) + "USDC";
            }
            order.setSymbol(symbol);
            order.setTriggerPrice((String) item.get("triggerPrice"));
            order.setPrice((String) item.get("price"));
            order.setQuantity((String) item.get("qty"));
            order.setSide((String) item.get("side"));
            order.setOrderType((String) item.get("orderType"));
            order.setStopLoss((String) item.get("stopLoss"));
            order.setTakeProfit((String) item.get("takeProfit"));
            conditionalOrders.add(order);
        }
        return conditionalOrders;
    }

}
