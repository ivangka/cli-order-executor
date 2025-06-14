package ivangka.cliorderexecutor.service;

import ivangka.cliorderexecutor.exception.InvalidCommandException;
import ivangka.cliorderexecutor.exception.UnknownSymbolException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

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
