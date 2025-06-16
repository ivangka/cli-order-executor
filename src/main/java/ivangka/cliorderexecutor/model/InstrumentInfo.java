package ivangka.cliorderexecutor.model;

public class InstrumentInfo {

    private String minOrderQty;
    private String qtyStep;

    public InstrumentInfo(String minOrderQty, String qtyStep) {
        this.minOrderQty = minOrderQty;
        this.qtyStep = qtyStep;
    }

    public String getMinOrderQty() {
        return minOrderQty;
    }

    public String getQtyStep() {
        return qtyStep;
    }

}
