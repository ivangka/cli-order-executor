package ivangka.cliorderexecutor.model;

public class Ticker {

    private String lastPrice;

    public Ticker(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

}
