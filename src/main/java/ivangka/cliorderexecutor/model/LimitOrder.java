package ivangka.cliorderexecutor.model;

public class LimitOrder {

    private String symbol;
    private String price;
    private String quantity;
    private String side;
    private String orderType;
    private String stopLoss;
    private String takeProfit;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
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
        return ""
                + "  +---------------+-------------------+" + System.lineSeparator()
                + String.format("  + %-13s + %17s +%n", "symbol", displaySymbol)
                + String.format("  + %-13s + %17s +%n", "price", price)
                + String.format("  + %-13s + %17s +%n", "quantity", quantity)
                + String.format("  + %-13s + %17s +%n", "side", side)
                + String.format("  + %-13s + %17s +%n", "orderType", orderType)
                + String.format("  + %-13s + %17s +%n", "stopLoss", stopLoss)
                + String.format("  + %-13s + %17s +%n", "takeProfit", takeProfit)
                + "  +---------------+-------------------+";
    }

}
