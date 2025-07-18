package ivangka.cliorderexecutor.service;

import ivangka.cliorderexecutor.exception.*;
import ivangka.cliorderexecutor.model.*;
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
    public void openMarketOrder(String symbol, String stopLoss, String takeProfit, String risk)
            throws BadRetCodeException, InvalidCommandException {

        Ticker ticker = apiService.ticker(symbol);
        Instrument instrument = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, ticker.getLastPrice(), stopLoss, instrument.getQtyStep(), "Market");

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(ticker.getLastPrice());
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        apiService.createMarketOrder(symbol, side, orderSize, stopLoss, takeProfit);
    }

    // open a market order without tp
    public void openMarketOrder(String symbol, String stopLoss, String risk)
            throws BadRetCodeException, InvalidCommandException {

        Ticker ticker = apiService.ticker(symbol);
        Instrument instrument = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, ticker.getLastPrice(), stopLoss, instrument.getQtyStep(), "Market");

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(ticker.getLastPrice());
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        apiService.createMarketOrder(symbol, side, orderSize, stopLoss);
    }

    // place a limit order
    public void placeLimitOrder(String symbol, String stopLoss, String takeProfit, String risk, String price)
            throws BadRetCodeException, InvalidCommandException {

        Instrument instrument = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, price, stopLoss, instrument.getQtyStep(), "Limit");

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(price);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        apiService.createLimitOrder(symbol, side, orderSize, price, stopLoss, takeProfit);
    }

    // place limit order without tp
    public void placeLimitOrder(String symbol, String stopLoss, String risk, String price)
            throws BadRetCodeException, InvalidCommandException {

        Instrument instrument = apiService.instrumentInfo(symbol);
        String orderSize = orderSize(risk, price, stopLoss, instrument.getQtyStep(), "Limit");

        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal priceBD = new BigDecimal(price);
        String side = stopLossBD.compareTo(priceBD) < 0 ? "Buy" : "Sell";

        apiService.createLimitOrder(symbol, side, orderSize, price, stopLoss);
    }

    // close positions
    public void closePositions(String symbol, String percent)
            throws BadRetCodeException, InvalidCommandException, TooSmallOrderSizeException, InterruptedException {
        List<Position> positions;
        if (symbol.equals("-all")) {
            positions = apiService.positions(); // 2 req (Position)
        } else {
            positions = apiService.positions(symbol); // 1 req (Position)
        }
        // checking user's percent
        BigDecimal percentBD;
        try {
            percentBD = new BigDecimal(percent);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Incorrect command format, try again");
        }
        // 0 < percent <= 100
        if (percentBD.compareTo(BigDecimal.ZERO) <= 0 || percentBD.compareTo(new BigDecimal("100")) > 0) {
            throw new InvalidCommandException("Percent value is incorrect");
        }

        for (Position position : positions) {
            String side = position.getSide().equals("Buy") ? "Sell" : "Buy";

            // if percent == 100 (full position)
            if (percentBD.compareTo(new BigDecimal("100")) == 0) {
                apiService.closePosition(position.getSymbol(), side, position.getSize()); // 1 req (Trade)
                Thread.sleep(150);
                continue;
            }

            // get min order size and step of the symbol
            Instrument instrument = apiService.instrumentInfo(position.getSymbol()); // 1 req (Market)
            BigDecimal minOrderSizeBD = new BigDecimal(instrument.getMinOrderQty());
            BigDecimal stepBD = new BigDecimal(instrument.getQtyStep());

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

            apiService.closePosition(position.getSymbol(), side, roundedSizeBD.toPlainString()); // 1 req (Trade)
            Thread.sleep(150);
        }
    }

    // cancel orders
    public void cancelOrders(String symbol) throws BadRetCodeException {
        if (symbol.equals("-all")) {
            apiService.cancelOrders();
        } else {
            apiService.cancelOrders(symbol);
        }
    }

    // manage stop-loss
    public void manageStopLoss(String symbol, String price) throws BadRetCodeException {
        apiService.manageStopLoss(symbol, price);
    }

    // manage take-profit
    public void manageTakeProfit(String symbol, String price) throws BadRetCodeException {
        apiService.manageTakeProfit(symbol, price);
    }

    // set the leverage for the trading pair
    public void setLeverage(String symbol, String leverage) throws BadRetCodeException {
        if (leverage.equals("-max")) {
            RiskLimit riskLimit = apiService.riskLimit(symbol);
            leverage = riskLimit.getMaxLeverage();
        }
        apiService.setLeverage(symbol, leverage);
    }

    // get positions info
    public List<Position> positions(String symbol) throws BadRetCodeException, OrderNotFoundException {
        List<Position> positions;
        if (symbol.equals("-all")) {
            positions = apiService.positions();
        } else {
            positions = apiService.positions(symbol);
        }
        if (positions.isEmpty() || positions.get(0).getSize().equals("0")) {
            throw new OrderNotFoundException("Positions were not found");
        }
        return positions;
    }

    // get open orders
    public List<Order> orders(String symbol) throws BadRetCodeException, OrderNotFoundException {
        List<Order> orders;
        if (symbol.equals("-all")) {
            orders = apiService.orders();
        } else {
            orders = apiService.orders(symbol);
        }
        if (orders.isEmpty()) {
            throw new OrderNotFoundException("Orders were not found");
        }
        return orders;
    }

    // test API request
    public void testRequest() throws BadRetCodeException {
        apiService.testRequest();
    }

    // get order size
    private String orderSize(String risk, String price, String stopLoss, String step, String orderType)
            throws InvalidCommandException {
        BigDecimal riskBD, priceBD, stopLossBD, stepBD;
        try {
            riskBD = new BigDecimal(risk);
            priceBD = new BigDecimal(price);
            stopLossBD = new BigDecimal(stopLoss);
            stepBD = new BigDecimal(step);
        } catch (NumberFormatException e) {
            throw new InvalidCommandException("Incorrect command format, try again");
        }
        return riskCalculatorService.calculateOrderSize(riskBD, priceBD, stopLossBD, stepBD, orderType);
    }

}
