package infrastructure.currency;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Load information about exchange rates from the file
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public class CurrencyInfoFromFileLoader implements CurrencyInfoLoader {
    private static final String RATIO = "ratio.";
    private static final String SYMBOL = "symbol.";

    private static final Logger log = LogManager.getLogger(CurrencyInfoFromFileLoader.class);

    private final ResourceBundle properties;


    public CurrencyInfoFromFileLoader() {
        log.debug("");

        properties = ResourceBundle.getBundle("currency");
    }

    @Override
    public Map<String, CurrencyInfo> getCurrencyInfo() {
        Map<String, CurrencyInfo> toReturn = new HashMap<>();
        Enumeration<String> keys = properties.getKeys();
        while (keys.hasMoreElements()) {
            String key = keys.nextElement();
            if (key.startsWith(RATIO)) {
                composeIfRatio(toReturn, key);
            } else if (key.startsWith(SYMBOL)) {
                composeIfSymbol(toReturn, key);
            }
        }
        return toReturn;
    }

    private void composeIfSymbol(Map<String, CurrencyInfo> toReturn, String key) {
        String langKey = key.replaceFirst(SYMBOL, "");
        CurrencyInfo currencyInfo = toReturn.get(langKey);
        if (currencyInfo == null) {
            currencyInfo = new CurrencyInfo();
            currencyInfo.setCurrencySymbol(properties.getString(key));
            toReturn.put(langKey, currencyInfo);
        } else {
            currencyInfo.setCurrencySymbol(properties.getString(key));
            toReturn.put(langKey, currencyInfo);
        }
    }

    private void composeIfRatio(Map<String, CurrencyInfo> toReturn, String key) {
        String langKey = key.replaceFirst(RATIO, "");
        CurrencyInfo currencyInfo = toReturn.get(langKey);
        if (currencyInfo == null) {
            currencyInfo = new CurrencyInfo();
            currencyInfo.setRatioToDollar(Long.parseLong(properties.getString(key)));
            toReturn.put(langKey, currencyInfo);
        } else {
            currencyInfo.setRatioToDollar(Long.parseLong(properties.getString(key)));
            toReturn.put(langKey, currencyInfo);
        }
    }
}
