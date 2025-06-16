package ivangka.cliorderexecutor.model;

public class Instrument {

    private String minOrderQty;
    private String qtyStep;

    public String getMinOrderQty() {
        return minOrderQty;
    }

    public void setMinOrderQty(String minOrderQty) {
        this.minOrderQty = minOrderQty;
    }

    public String getQtyStep() {
        return qtyStep;
    }

    public void setQtyStep(String qtyStep) {
        this.qtyStep = qtyStep;
    }

}
