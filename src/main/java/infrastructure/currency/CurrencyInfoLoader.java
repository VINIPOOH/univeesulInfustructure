package infrastructure.currency;

import java.util.Map;

/**
 * Decelerate interface for load information about exchange rates
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface CurrencyInfoLoader {
    Map<String, CurrencyInfo> getCurrencyInfo();
}
