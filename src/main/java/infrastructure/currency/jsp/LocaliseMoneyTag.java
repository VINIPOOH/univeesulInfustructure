package infrastructure.currency.jsp;

import infrastructure.ApplicationContext;
import infrastructure.currency.CurrencyInfo;

import javax.servlet.ServletContext;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

import static infrastructure.constant.AttributeConstants.CONTEXT;

/**
 * Jsp tag which localise money representation
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class LocaliseMoneyTag extends SimpleTagSupport {
    private long moneyInCents;
    private String lang;

    public long getMoneyInCents() {
        return moneyInCents;
    }

    public void setMoneyInCents(long moneyInCents) {
        this.moneyInCents = moneyInCents;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    /**
     * Converts money to the currency of the current locale. And adds a symbol of this currency at the end.
     * If there is an overflow after converting dollars to local currency,then the return occurs in the lobar equivalent.
     */
    @Override
    public void doTag() throws IOException {
        ServletContext servletContext = ((PageContext) getJspContext()).getServletContext();
        ApplicationContext context = ((ApplicationContext) servletContext.getAttribute(CONTEXT));
        CurrencyInfo currencyInfo = context.getCurrencyInfo(lang);
        if (currencyInfo == null || moneyInCents * currencyInfo.getRatioToDollar() <= 0) {
            getJspContext().getOut().print(moneyInCents + ".00 $");
            return;
        }
        getJspContext().getOut().print(moneyInCents * currencyInfo.getRatioToDollar() + ".00 " + currencyInfo.getCurrencySymbol());
    }
}
