package ivangka.cliorderexecutor.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final ApiService apiService;
    private final RiskCalculatorService riskCalculatorService;

    @Autowired
    public OrderService(RiskCalculatorService riskCalculatorService, ApiService apiService) {
        this.riskCalculatorService = riskCalculatorService;
        this.apiService = apiService;
    }

    // place a market order
    public String placeOrder(String symbol, String stopLoss, String takeProfit, String risk) {
        String lastPrice = apiService.lastPrice(symbol);
        String orderSizeStep = apiService.orderSizeStep(symbol);
        String orderSize = riskCalculatorService.calculateOrderSize(risk, lastPrice, stopLoss, orderSizeStep);
        return apiService.createOrder(symbol, orderSize, stopLoss, takeProfit);
    }

}
