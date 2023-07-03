package testingFunctional.tcp.deamon;

import infrastructure.anotation.DemonThread;
import infrastructure.currency.CurrencyInfoFromFileLoader;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

@DemonThread()
public class DemonTimeoutLogThread implements Runnable{

    private static final Logger log = LogManager.getLogger(DemonTimeoutLogThread.class);
    @Override
    public void run() {
        while (true){
            try {
                Thread.sleep(50000);
                log.error("DemonTimeoutLogThread");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }
}
