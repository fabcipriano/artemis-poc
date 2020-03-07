package br.com.facio.artemis;

import br.com.facio.artemis.jms.Sender;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ArtemisAppClient  implements ApplicationRunner {

    private static Logger LOG = LogManager.getLogger();
    
    @Autowired
    private Sender sender;

    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        LOG.info("Start sending messages ...");
        LOG.info("Spring Boot Artemis Client Example");

        for (int i = 0; i < 10000; i++) {
            sender.generateMessageToSend();
            TimeUnit.MILLISECONDS.sleep(100);
        }
        LOG.info("Messages sent ... waiting");
        TimeUnit.SECONDS.sleep(600);
        LOG.info("Done.");
    }

    public static void main(String[] args) {
        SpringApplication.run(ArtemisAppClient.class, args);
    }
    
}
