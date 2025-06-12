package ivangka.cliorderexecutor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@PropertySource("classpath:application.properties")
@Service
public class RiskCalculatorService {

    // market order commission in %
    @Value("${order.commission}")
    private BigDecimal orderCommissionPercent;

    // get order size in base coin
    public String calculateOrderSize(String risk, String lastPrice, String stopLoss, String priceStep) {
        BigDecimal riskBD = new BigDecimal(risk);
        BigDecimal lastPriceBD = new BigDecimal(lastPrice);
        BigDecimal stopLossBD = new BigDecimal(stopLoss);
        BigDecimal stepBD = new BigDecimal(priceStep);
        BigDecimal commissionDecimal = orderCommissionPercent.divide(BigDecimal.valueOf(100), 10,
                RoundingMode.HALF_UP);

        /*

                      R
        V = ----------------------
            ∣(P - SL)∣ + c(P + SL)

        V  - orderSize ($)
        P  - currentPrice
        R  - risk ($)
        SL - stopLoss
        c  - orderCommission (in decimal form)

         */
        BigDecimal priceDiff = lastPriceBD.subtract(stopLossBD).abs();
        BigDecimal commissionComponent = commissionDecimal.multiply(lastPriceBD.add(stopLossBD));
        BigDecimal denominator = priceDiff.add(commissionComponent);
        BigDecimal orderSize = riskBD.divide(denominator, 10, RoundingMode.HALF_UP);

        // formatting size by price step
        BigDecimal roundedOrderSize = orderSize.divide(stepBD, 0, RoundingMode.DOWN).multiply(stepBD);
        return roundedOrderSize.stripTrailingZeros().toPlainString();
    }

}
