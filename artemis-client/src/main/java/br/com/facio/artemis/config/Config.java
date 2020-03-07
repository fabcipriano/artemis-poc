package br.com.facio.artemis.config;

import br.com.facio.artemis.jms.Sender;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

/**
 *
 * @author fabiano
 */
@Configuration
@EnableJms
public class Config {
    private static Logger LOG = LogManager.getLogger();
    public static final String ORDER_QUEUE = "ordernumbers";

    @Value("${artemis.broker-url}")
    private String brokerUrl;

    @Value("${artemis.user}")
    private String user;

    @Value("${artemis.password}")
    private String password;

    @Bean
    public ActiveMQConnectionFactory connectionFactory() {
        LOG.debug("brokerUrl.:{}", brokerUrl);
        LOG.debug("user.:{}, password", user, password);

        return new ActiveMQConnectionFactory(brokerUrl, user, password);
    }
    public CachingConnectionFactory cachingConnectionFactoryForProducer() {
        CachingConnectionFactory cache = new CachingConnectionFactory(connectionFactory());
        cache.setCacheConsumers(false);
        cache.setCacheProducers(true);
        cache.setSessionCacheSize(4);
        return cache;
    }
    
    @Bean
    public JmsTemplate jmsTemplate() {
        LOG.info("Creating jmsTemplate ...");
        JmsTemplate jmsTemplate = new JmsTemplate(cachingConnectionFactoryForProducer());
        return jmsTemplate;
    }
    @Bean
    public DefaultJmsListenerContainerFactory queueListenerFactory( DefaultJmsListenerContainerFactoryConfigurer configurer ) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        configurer.configure(factory, connectionFactory());
        factory.setSessionTransacted(true);
        factory.setConcurrency("10-20");
        factory.setReceiveTimeout(7500l);
        factory.setCacheLevel(DefaultMessageListenerContainer.CACHE_CONSUMER);
        factory.setTaskExecutor(new SimpleAsyncTaskExecutor("Consumer-"));
        
        return factory;
    }
}
