package infrastructure.currency;

/**
 * Contains a pair of currency symbol and coefficient for the conversion of dollars to this currency
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class CurrencyInfo {
    private long ratioToDollar;
    private String currencySymbol;

    public long getRatioToDollar() {
        return ratioToDollar;
    }

    public void setRatioToDollar(long ratioToDollar) {
        this.ratioToDollar = ratioToDollar;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }
}
