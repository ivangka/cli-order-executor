package ivangka.cliorderexecutor.model;

public class Position {

    private String symbol;
    private String side;
    private String size;
    private String avgPrice;
    private String leverage;
    private String liqPrice;
    private String stopLoss;
    private String takeProfit;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAvgPrice() {
        return avgPrice;
    }

    public void setAvgPrice(String avgPrice) {
        this.avgPrice = avgPrice;
    }

    public String getLeverage() {
        return leverage;
    }

    public void setLeverage(String leverage) {
        this.leverage = leverage;
    }

    public String getLiqPrice() {
        return liqPrice;
    }

    public void setLiqPrice(String liqPrice) {
        this.liqPrice = liqPrice;
    }

    public String getStopLoss() {
        return stopLoss;
    }

    public void setStopLoss(String stopLoss) {
        this.stopLoss = stopLoss;
    }

    public String getTakeProfit() {
        return takeProfit;
    }

    public void setTakeProfit(String takeProfit) {
        this.takeProfit = takeProfit;
    }

    private String abbreviateForDisplay(String s) {
        if (s == null) return "";
        return s.length() > 16 ? s.substring(0, 14) + ".." : s;
    }

    @Override
    public String toString() {
        String displaySymbol = abbreviateForDisplay(symbol);
        String displayLiqPrice = abbreviateForDisplay(liqPrice);
        String displayAvgPrice = abbreviateForDisplay(avgPrice);
        return ""
                + "  +---------------+-------------------+" + System.lineSeparator()
                + String.format("  + %-13s + %17s +%n", "symbol", displaySymbol)
                + String.format("  + %-13s + %17s +%n", "side", side)
                + String.format("  + %-13s + %17s +%n", "size", size)
                + String.format("  + %-13s + %17s +%n", "avgPrice", displayAvgPrice)
                + String.format("  + %-13s + %17s +%n", "leverage", leverage)
                + String.format("  + %-13s + %17s +%n", "liqPrice", displayLiqPrice)
                + String.format("  + %-13s + %17s +%n", "stopLoss", stopLoss)
                + String.format("  + %-13s + %17s +%n", "takeProfit", takeProfit)
                + "  +---------------+-------------------+";
    }

}
