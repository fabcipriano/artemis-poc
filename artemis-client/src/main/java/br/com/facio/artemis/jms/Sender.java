package br.com.facio.artemis.jms;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author fabiano
 */
public class Sender {

    private static Logger LOG = LogManager.getLogger();
    private static long SEQUENCEMSG = System.currentTimeMillis() * 100000;
    
    private long min = 100000;
    private long max = 900000000;

    @Autowired
    private JmsTemplate jmsTemplate;

    public void send(String message) {
        LOG.debug("sending message='{}'", message);
        jmsTemplate.convertAndSend("ordernumbers", message);
    }
    
    public void generateMessageToSend() {
        SEQUENCEMSG++;
        double randomnumber = (Math.random() * ( max - min )) + min;
        String message = "{id:" + SEQUENCEMSG + ", random:" + randomnumber + "}";
        send(message);
        LOG.info("message id={} sended.", SEQUENCEMSG);
    }
}
