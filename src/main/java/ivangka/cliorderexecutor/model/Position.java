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

    @Override
    public String toString() {
        String displaySymbol = symbol.length() > 16 ? symbol.substring(0, 14) + ".." : symbol;
        String displayLiqPrice = liqPrice.length() > 16 ? liqPrice.substring(0, 14) + ".." : liqPrice;
        String displayAvgPrice = avgPrice.length() > 16 ? avgPrice.substring(0, 14) + ".." : avgPrice;
        return ""
                + "  +-------------+-------------------+" + System.lineSeparator()
                + String.format("  + %-11s + %17s +%n", "symbol", displaySymbol)
                + String.format("  + %-11s + %17s +%n", "side", side)
                + String.format("  + %-11s + %17s +%n", "size", size)
                + String.format("  + %-11s + %17s +%n", "avgPrice", displayAvgPrice)
                + String.format("  + %-11s + %17s +%n", "leverage", leverage)
                + String.format("  + %-11s + %17s +%n", "liqPrice", displayLiqPrice)
                + String.format("  + %-11s + %17s +%n", "stopLoss", stopLoss)
                + String.format("  + %-11s + %17s +%n", "takeProfit", takeProfit)
                + "  +-------------+-------------------+";
    }

}
