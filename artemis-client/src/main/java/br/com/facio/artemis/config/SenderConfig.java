package br.com.facio.artemis.config;

import br.com.facio.artemis.jms.Sender;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;

/**
 *
 * @author fabiano
 */
@Configuration
public class SenderConfig {
    private static Logger LOG = LogManager.getLogger();

    @Value("${artemis.broker-url}")
    private String brokerUrl;

    @Value("${artemis.user}")
    private String user;

    @Value("${artemis.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory senderActiveMQConnectionFactory() {
        LOG.debug("brokerUrl.:{}", brokerUrl);
        LOG.debug("user.:{}, password", user, password);

        return new ActiveMQConnectionFactory(brokerUrl, user, password);
    }

    @Bean
    public CachingConnectionFactory cachingConnectionFactory() {
        return new CachingConnectionFactory(
                senderActiveMQConnectionFactory());
    }

    @Bean
    public JmsTemplate jmsTemplate() {
        return new JmsTemplate(cachingConnectionFactory());
    }

    @Bean
    public Sender sender() {
        return new Sender();
    }
}
