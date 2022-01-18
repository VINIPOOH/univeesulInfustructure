package infrastructure;

import infrastructure.controller.MultipleMethodController;
import infrastructure.currency.CurrencyInfo;
import infrastructure.factory.ObjectFactory;
import infrastructure.сonfig.Config;

/**
 * Represents info about context in which application run.
 * Contains info about currency rates.
 * Controls beans and commands.
 *
 * @author Vendelovskyi Ivan
 * @version 1.0
 */
public interface ApplicationContext {
    void init();

    CurrencyInfo getCurrencyInfo(String langKey);

    <T> T getObject(Class<T> typeKey);

    MultipleMethodController getCommand(String linkKey);

    void setFactory(ObjectFactory factory);

    Config getConfig();
}
