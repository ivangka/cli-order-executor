package ivangka.cliorderexecutor.model;

public class RiskLimit {

    private String maxLeverage;

    public RiskLimit(String riskLimit) {
        this.maxLeverage = riskLimit;
    }

    public String getMaxLeverage() {
        return maxLeverage;
    }

}
