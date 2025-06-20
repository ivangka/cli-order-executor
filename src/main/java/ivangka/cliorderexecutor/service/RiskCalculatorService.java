package ivangka.cliorderexecutor.service;

import ivangka.cliorderexecutor.exception.InvalidCommandException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@PropertySource("classpath:application.properties")
@Service
public class RiskCalculatorService {

    // market order commission in %
    @Value("${order.market.commission}")
    private BigDecimal orderCommissionPercent;

    // get order size in base coin
    public String calculateOrderSize(BigDecimal risk, BigDecimal price, BigDecimal stopLoss, BigDecimal step)
            throws InvalidCommandException {

        BigDecimal commissionDecimal = orderCommissionPercent.divide(BigDecimal.valueOf(100), 10,
                RoundingMode.HALF_UP);

        /*
                      R
        V = ----------------------
            ∣(P - SL)∣ + c(P + SL)

        V  - orderSize ($)
        P  - price
        R  - risk ($)
        SL - stopLoss
        c  - orderCommission (in decimal form)

         */
        BigDecimal priceDiff = price.subtract(stopLoss).abs();
        BigDecimal commissionComponent = commissionDecimal.multiply(price.add(stopLoss));
        BigDecimal denominator = priceDiff.add(commissionComponent);
        BigDecimal orderSize = risk.divide(denominator, 10, RoundingMode.HALF_UP);

        // formatting size by price step
        BigDecimal roundedOrderSize = orderSize.divide(step, 0, RoundingMode.DOWN).multiply(step);
        return roundedOrderSize.stripTrailingZeros().toPlainString();
    }

}
