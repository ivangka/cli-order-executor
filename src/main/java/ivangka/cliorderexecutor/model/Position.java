package ivangka.cliorderexecutor.model;

public class Position {

    private String symbol;
    private String side;
    private String size;

    public Position(String symbol, String side, String size) {
        this.symbol = symbol;
        this.side = side;
        this.size = size;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getSide() {
        return side;
    }

    public String getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "Position " + symbol + ": " + size + " (" + side + ")";
    }

}
