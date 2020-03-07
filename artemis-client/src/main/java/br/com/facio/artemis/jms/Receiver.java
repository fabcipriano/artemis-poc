package br.com.facio.artemis.jms;

import static br.com.facio.artemis.config.Config.ORDER_QUEUE;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabiano
 */
@Component
public class Receiver {
    private static Logger LOG = LogManager.getLogger();

    @JmsListener(destination = ORDER_QUEUE, containerFactory = "queueListenerFactory")
    public void receiveMessage(String message) {
        waitFor();
        LOG.info("after wait, JMS Received message.:" + message);
    }

    private void waitFor() throws RuntimeException {
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException ex) {
            throw new RuntimeException("unexpected exception", ex);
        }
    }
    
}
