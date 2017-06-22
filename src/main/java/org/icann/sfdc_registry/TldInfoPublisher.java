package org.icann.sfdc_registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.icann.secure_data.api.EventEnvelope;
import org.icann.secure_data.api.TldUpdateEvent;
import org.icann.sfdc_registry.util.EventEnvelopBuilder;
import org.icann.sfdc_registry.util.EventEnvelopBuilderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Properties;
import java.util.Random;

@Component
public class TldInfoPublisher {

    private KafkaProducer<String, String> producer;

    @Value(("${spring.kafka.bootstrap-servers}"))
    private String kafkaBrokers;

    @Value(("${kafka.outgoing.topic}"))
    private String outgoingTopic;

    private static final Logger logger = LoggerFactory.getLogger(TldInfoPublisher.class);

    private Random random = new Random();

    private ObjectMapper mapper = new ObjectMapper();

    private EventEnvelopBuilder envelopBuilder = new EventEnvelopBuilderImpl();

    @PostConstruct
    public void post() {
        Properties props = new Properties();
        props.put("bootstrap.servers", kafkaBrokers);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("acks", "all");
        props.put("client.id", "Tld Info");
        producer = new KafkaProducer<>(props);
    }

    boolean first = true;

    @Scheduled(fixedDelay = 5000)
    public void publishChanges() throws JsonProcessingException {
        String[] tlds = new String[]{"gmail", "host", "xyz", "com"};
        String tld;
        if (first) {
            tld = tlds[tlds.length-1];
            first = false;
        } else {
            tld = tlds[random.nextInt(tlds.length - 1)];
        }
        TldUpdateEvent tldInfo = new TldUpdateEvent().withTldName(tld).withStatus("active").withDownloadMethod(TldUpdateEvent.DownloadMethod.HTTP);
        producer.send(new ProducerRecord<>(outgoingTopic, tld, mapper.writeValueAsString(envelopBuilder.build(tldInfo))));
        producer.flush();
    }

}
