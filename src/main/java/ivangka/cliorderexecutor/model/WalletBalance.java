package ivangka.cliorderexecutor.model;

public class WalletBalance {

    private String walletBal;
    private String marginBal;
    private String availableBal;

    public String getWalletBal() {
        return walletBal;
    }

    public void setWalletBal(String walletBal) {
        this.walletBal = walletBal;
    }

    public String getMarginBal() {
        return marginBal;
    }

    public void setMarginBal(String marginBal) {
        this.marginBal = marginBal;
    }

    public String getAvailableBal() {
        return availableBal;
    }

    public void setAvailableBal(String availableBal) {
        this.availableBal = availableBal;
    }

//    private String truncateToFourDecimals(String input) {
//        if (input == null) return null;
//        int dotIndex = input.indexOf('.');
//        if (dotIndex == -1) return input;
//        int endIndex = Math.min(input.length(), dotIndex + 5);
//        return input.substring(0, endIndex);
//    }

    private String abbreviateForDisplay(String s) {
        if (s == null) return "";
        return s.length() > 16 ? s.substring(0, 14) + ".." : s;
    }

    @Override
    public String toString() {
        String displayWalletBal = abbreviateForDisplay(walletBal);
        String displayMarginBal = abbreviateForDisplay(marginBal);
        String displayAvailableBal = abbreviateForDisplay(availableBal);
        return ""
                + "  +---------------+-------------------+" + System.lineSeparator()
                + String.format("  + %-13s + %17s +%n", "walletBal", displayWalletBal)
                + String.format("  + %-13s + %17s +%n", "marginBal", displayMarginBal)
                + String.format("  + %-13s + %17s +%n", "availableBal", displayAvailableBal)
                + "  +---------------+-------------------+";
    }

}
