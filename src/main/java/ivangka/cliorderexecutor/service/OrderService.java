package ivangka.cliorderexecutor.service;

import ivangka.cliorderexecutor.exception.InvalidCommandException;
import ivangka.cliorderexecutor.exception.OrderNotFoundException;
import ivangka.cliorderexecutor.exception.TooSmallOrderSizeException;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
import ivangka.cliorderexecutor.model.InstrumentInfo;
import ivangka.cliorderexecutor.model.Position;
import ivangka.cliorderexecutor.model.RiskLimit;
import ivangka.cliorderexecutor.model.Ticker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class OrderService {

    private final ApiService apiService;
    private final RiskCalculatorService riskCalculatorService;

    @Autowired
    public OrderService(RiskCalculatorService riskCalculatorService, ApiService apiService) {
        this.riskCalculatorService = riskCalculatorService;
        this.apiService = apiService;
    }

    // open a market order
    public String openMarketOrder(String symbol, String stopLoss, String takeProfit, String risk)
            throws UnknownSymbolException, InvalidCommandException {

        Ticker ticker = apiService.ticker(symbol);
        InstrumentInfo instrumentInfo = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, ticker.getLastPrice(), stopLoss, instrumentInfo.getQtyStep());

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(ticker.getLastPrice());
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createMarketOrder(symbol, side, orderSize, stopLoss, takeProfit);
    }

    // open a market order without tp
    public String openMarketOrder(String symbol, String stopLoss, String risk)
            throws UnknownSymbolException, InvalidCommandException {

        Ticker ticker = apiService.ticker(symbol);
        InstrumentInfo instrumentInfo = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, ticker.getLastPrice(), stopLoss, instrumentInfo.getQtyStep());

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(ticker.getLastPrice());
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createMarketOrder(symbol, side, orderSize, stopLoss);
    }

    // place a limit order
    public String placeLimitOrder(String symbol, String stopLoss, String takeProfit, String risk, String price)
            throws UnknownSymbolException, InvalidCommandException {

        InstrumentInfo instrumentInfo = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, price, stopLoss, instrumentInfo.getQtyStep());

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(price);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createLimitOrder(symbol, side, orderSize, price, stopLoss, takeProfit);
    }

    // place limit order without tp
    public String placeLimitOrder(String symbol, String stopLoss, String risk, String price)
            throws UnknownSymbolException, InvalidCommandException {

        InstrumentInfo instrumentInfo = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, price, stopLoss, instrumentInfo.getQtyStep());

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(price);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createLimitOrder(symbol, side, orderSize, price, stopLoss);
    }

    // close position for specified pair
    public String closePositions(String symbol, String percent)
            throws UnknownSymbolException, InvalidCommandException, OrderNotFoundException, TooSmallOrderSizeException {
        List<Position> positions = apiService.positions(symbol);
        if (positions.get(0).getSize().equals("0")) {
            throw new OrderNotFoundException("The order not found");
        }

        // checking user's percent
        BigDecimal percentBD;
        try {
            percentBD = new BigDecimal(percent);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Incorrect command format");
        }
        // 0 < percent <= 100
        if (percentBD.compareTo(BigDecimal.ZERO) <= 0 || percentBD.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidCommandException("Value of percent is incorrect");
        }

        // get min order size and step of the symbol
        InstrumentInfo instrumentInfo = apiService.instrumentInfo(symbol);
        BigDecimal minOrderSizeBD = new BigDecimal(instrumentInfo.getMinOrderQty());
        BigDecimal stepBD = new BigDecimal(instrumentInfo.getQtyStep());

        for (Position position : positions) {
            String side = position.getSide().equals("Buy") ? "Sell" : "Buy";

            // calculating final size
            BigDecimal orderSizeBD = new BigDecimal(position.getSize());
            BigDecimal percentFactor = percentBD.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
            BigDecimal sizeBD = orderSizeBD.multiply(percentFactor);
            // formatting size by price step
            BigDecimal roundedSizeBD = sizeBD.divide(stepBD, 0, RoundingMode.DOWN).multiply(stepBD)
                    .stripTrailingZeros();

            // size >= minOrderSize
            if (percentBD.compareTo(BigDecimal.valueOf(100)) != 0 && roundedSizeBD.compareTo(minOrderSizeBD) < 0) {
                throw new TooSmallOrderSizeException("Position size to close is too small");
            }

            String retCode = apiService.closePositions(position.getSymbol(), side, roundedSizeBD.toString());
            if (!retCode.equals("0")) {
                return retCode;
            }
        }
        return "0";
    }

    // cancel all limit orders for specified pair
    public String cancelOrders(String symbol) {
        return apiService.cancelOrders(symbol);
    }

    // set the leverage for the trading pair
    public String setLeverage(String symbol, String leverage) throws UnknownSymbolException {
        if (leverage.equals("Max")) {
            RiskLimit riskLimit = apiService.riskLimit(symbol);
            leverage = riskLimit.getMaxLeverage();
        }
        return apiService.setLeverage(symbol, leverage);
    }

    // get order size
    private String orderSize(String risk, String price, String stopLoss, String step) throws InvalidCommandException {
        BigDecimal riskBD, priceBD, stopLossBD, stepBD;
        try {
            riskBD = new BigDecimal(risk);
            priceBD = new BigDecimal(price);
            stopLossBD = new BigDecimal(stopLoss);
            stepBD = new BigDecimal(step);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Incorrect command format");
        }
        return riskCalculatorService.calculateOrderSize(riskBD, priceBD, stopLossBD, stepBD);
    }

}
