package ivangka.cliorderexecutor.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@PropertySource("classpath:application.properties")
@Service
public class RiskCalculatorService {

    // taker fee in % (market order)
    @Value("${fee.taker.commission}")
    private BigDecimal feeTakerPercent;

    // maker order fee in % (limit order)
    @Value("${fee.maker.commission}")
    private BigDecimal feeMakerPercent;

    // get order size in base coin
    public String calculateOrderSize(BigDecimal risk, BigDecimal price, BigDecimal stopLoss, BigDecimal step,
                                     String orderType) {
        BigDecimal feeOpenDecimal;
        if (orderType.equals("Limit")) {
            feeOpenDecimal = feeMakerPercent.divide(BigDecimal.valueOf(100), 10,
                    RoundingMode.HALF_UP);
        } else {
            feeOpenDecimal = feeTakerPercent.divide(BigDecimal.valueOf(100), 10,
                    RoundingMode.HALF_UP);
        }
        BigDecimal feeCloseDecimal = feeTakerPercent.divide(BigDecimal.valueOf(100), 10,
                RoundingMode.HALF_UP);

        /*
                         R
        V = -----------------------------
            ∣(P - SL)∣ + Fo * P + Fc * SL

        V  - order size (orderSize, base coin)
        P  - price (price)
        R  - risk (risk, $)
        SL - stop-loss (stopLoss)
        Fo  - trading fee for open (feeOpenDecimal, in decimal form)
        Fc  - trading fee for close (feeCloseDecimal, in decimal form)

         */
        BigDecimal priceDiff = price.subtract(stopLoss).abs();
        BigDecimal feeOpen = feeOpenDecimal.multiply(price);
        BigDecimal feeClose = feeCloseDecimal.multiply(stopLoss);
        BigDecimal denominator = priceDiff.add(feeOpen).add(feeClose);
        BigDecimal orderSize = risk.divide(denominator, 10, RoundingMode.HALF_UP);

        // formatting size by price step
        BigDecimal roundedOrderSize = orderSize.divide(step, 0, RoundingMode.DOWN).multiply(step);
        return roundedOrderSize.stripTrailingZeros().toPlainString();
    }

}
