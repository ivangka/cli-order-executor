package ivangka.cliorderexecutor.service;

import ivangka.cliorderexecutor.exception.InvalidCommandException;
import ivangka.cliorderexecutor.exception.OrderNotFoundException;
import ivangka.cliorderexecutor.exception.TooSmallOrderSizeException;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
import ivangka.cliorderexecutor.model.Position;
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

        String lastPrice = apiService.lastPrice(symbol);
        String orderSizeStep = apiService.orderSizeStep(symbol);
        String orderSize = orderSize(risk, lastPrice, stopLoss, orderSizeStep);

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(lastPrice);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createMarketOrder(symbol, side, orderSize, stopLoss, takeProfit);
    }

    // open a market order without tp
    public String openMarketOrder(String symbol, String stopLoss, String risk)
            throws UnknownSymbolException, InvalidCommandException {

        String lastPrice = apiService.lastPrice(symbol);
        String orderSizeStep = apiService.orderSizeStep(symbol);
        String orderSize = orderSize(risk, lastPrice, stopLoss, orderSizeStep);

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(lastPrice);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createMarketOrder(symbol, side, orderSize, stopLoss);
    }

    // place a limit order
    public String placeLimitOrder(String symbol, String stopLoss, String takeProfit, String risk, String price)
            throws UnknownSymbolException, InvalidCommandException {

        String orderSizeStep = apiService.orderSizeStep(symbol);
        String orderSize = orderSize(risk, price, stopLoss, orderSizeStep);

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(price);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createLimitOrder(symbol, side, orderSize, price, stopLoss, takeProfit);
    }

    // place limit order without tp
    public String placeLimitOrder(String symbol, String stopLoss, String risk, String price)
            throws UnknownSymbolException, InvalidCommandException {

        String orderSizeStep = apiService.orderSizeStep(symbol);
        String orderSize = orderSize(risk, price, stopLoss, orderSizeStep);

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(price);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        return apiService.createLimitOrder(symbol, side, orderSize, price, stopLoss);
    }

    // close position by symbol
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

        // get min order size of the symbol
        String minOrderSize = apiService.minOrderSize(symbol);
        BigDecimal minOrderSizeBD = new BigDecimal(minOrderSize);

        // get order size step of the symbol
        String orderSizeStep = apiService.orderSizeStep(symbol);
        BigDecimal stepBD = new BigDecimal(orderSizeStep);

        for (Position position : positions) {
            String side = position.getSide().equals("Buy") ? "Sell" : "Buy";

            // calculating and rounding final size
            BigDecimal orderSizeBD = new BigDecimal(position.getSize());
            BigDecimal percentFactor = percentBD.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
            BigDecimal sizeBD = orderSizeBD.multiply(percentFactor);
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

    // set the leverage for the trading pair
    public String setLeverage(String symbol, String leverage) throws UnknownSymbolException {
        return apiService.setLeverage(symbol, leverage);
    }

    // set the leverage to maximum for the trading pair
    public String setMaxLeverage(String symbol) throws UnknownSymbolException {
        String maxLeverage = apiService.maxLeverage(symbol);
        return apiService.setLeverage(symbol, maxLeverage);
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
