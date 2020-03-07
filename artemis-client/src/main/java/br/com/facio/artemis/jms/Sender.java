package br.com.facio.artemis.jms;

import static br.com.facio.artemis.config.Config.ORDER_QUEUE;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**
 *
 * @author fabiano
 */
@Component
public class Sender {

    private static Logger LOG = LogManager.getLogger();
    private static long SEQUENCEMSG = System.currentTimeMillis() * 100000;
    
    private long min = 100000;
    private long max = 900000000;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendAndRetry(String message, int times) {
        int tryit = 0;
        boolean result = false;
        while (!(result = send(message)) && (tryit <= times) ) {
            tryit++;
            LOG.warn("Failed to send result={} message='{}' ... try for the {} time", result, message, tryit);
        }
        
        if (!result) {
            LOG.error(" =======> FATAL MESSAGE NOT SENDED '{}'", message);
        }
    }

    private boolean send(String message) {
        boolean sended = false;
        try {
            LOG.debug("sending message='{}' ...", message);
            jmsTemplate.convertAndSend(ORDER_QUEUE, message);
            sended = true;
            LOG.debug("sended message='{}'", message);
        } catch (Exception jmsException) {
            LOG.error("FAILED to send message='{}'", message, jmsException);
        }
        return sended;
    }
    
    public void generateMessageToSend() {
        SEQUENCEMSG++;
        double randomnumber = (Math.random() * (max - min)) + min;
        String message = "{id:" + SEQUENCEMSG + ", random:" + randomnumber + "}";
        sendAndRetry(message, 3);
        LOG.info("message id={} sended.", SEQUENCEMSG);
    }
}
