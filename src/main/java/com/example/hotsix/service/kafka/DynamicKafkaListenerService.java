package com.example.hotsix.service.kafka;

import com.example.hotsix.dto.build.TeamProjectInfoDto;
import com.example.hotsix.dto.logs.LogEntryDto;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.repository.team.TeamProjectInfoRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.TopicPartitionOffset;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DynamicKafkaListenerService {
    private final Map<Long, ConcurrentMessageListenerContainer<String, String>> listeners = new ConcurrentHashMap<>();
    private final ConcurrentKafkaListenerContainerFactory<String, String> factory;
    private final SimpMessagingTemplate messagingTemplate;
    private final KafkaProperties kafkaProperties;
    private final TeamProjectInfoRepository teamProjectInfoRepository;

    @Value("${kafka.bootstrap.server.config}")
    private String serverConfig;

    @Autowired
    public DynamicKafkaListenerService(
            @Qualifier("logListenerContainerFactory") ConcurrentKafkaListenerContainerFactory<String, String> factory,
            SimpMessagingTemplate messagingTemplate,
            KafkaProperties kafkaProperties,
            TeamProjectInfoRepository teamProjectInfoRepository) {
        this.factory = factory;
        this.messagingTemplate = messagingTemplate;
        this.kafkaProperties = kafkaProperties;
        this.teamProjectInfoRepository = teamProjectInfoRepository;
    }
    public void createDynamicListener(Long projectInfoId,Long configId, String topic, String groupId){
        TeamProjectInfo teamProjectInfo = teamProjectInfoRepository.findProjectInfoByProjectInfoId(projectInfoId);
        Long teamId = teamProjectInfo.getTeam().getId();
        Map<String, Object> consumerProps = new HashMap<>(kafkaProperties.buildConsumerProperties());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, serverConfig);
//
//
//        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
//
//        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
//        factory.setConsumerFactory(consumerFactory);

        ConcurrentMessageListenerContainer<String, String> container = factory.createContainer(
                new TopicPartitionOffset(topic, 0)
        );
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                .appendLiteral('Z')
                .toFormatter();

        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        container.setupMessageListener((MessageListener<String, String>) record -> {
            LogEntryDto log = null;
            try {
                log = mapper.readValue(record.value(), LogEntryDto.class);
                LocalDateTime dateTime = LocalDateTime.parse(log.getTimestamp(), inputFormatter);
                log.setTimestamp(dateTime.format(outputFormatter));
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            sendLogToTeam(projectInfoId,configId, log);
        });
        container.start();
        listeners.put(configId, container);
    }
    public void removeDynamicListener(Long configId, String topic){
        ConcurrentMessageListenerContainer<String, String> container = listeners.remove(configId);
        if (container != null) {
            container.stop();
        }
    }
    private void sendLogToTeam(Long projectInfoId, Long configId, LogEntryDto log) {
        System.out.println("A new Log Create and Send to Client // /sub/log/" + projectInfoId+"/"+configId);
        messagingTemplate.convertAndSend("/sub/log/" + projectInfoId+"/"+configId, log);
    }
}
