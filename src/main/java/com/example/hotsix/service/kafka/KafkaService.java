package com.example.hotsix.service.kafka;

import com.example.hotsix.dto.logs.LogEntryDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;

@Service
@RequiredArgsConstructor
public class KafkaService {
    private final KafkaProperties kafkaProperties;

    @Value("${kafka.bootstrap.server.config}")
    private String bootstrapServerConfig;

    public KafkaConsumer<String, String> createKafkaConsumer(String topic, Long memberId, String groupId) {
        Properties props = new Properties();
        props.put("bootstrap.servers", bootstrapServerConfig);
        props.put("group.id", groupId);
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "member-" + memberId);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        return new KafkaConsumer<>(props);
    }

    public List<LogEntryDto> getRecentLogs(String topic, Long memberId, String groupId) {
        KafkaConsumer<String, String> consumer = createKafkaConsumer(topic, memberId, groupId);

        try {
            TopicPartition partition = new TopicPartition(topic, 0);
            consumer.assign(Collections.singletonList(partition));

            long endOffset = consumer.endOffsets(Collections.singletonList(partition)).get(partition);
            long startOffset = Math.max(0, endOffset - 400);

            consumer.seek(partition, startOffset);
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

            List<LogEntryDto> recentLogs = new ArrayList<>();
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JavaTimeModule());
            mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
                    .appendPattern("yyyy-MM-dd'T'HH:mm:ss")
                    .appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true)
                    .appendLiteral('Z')
                    .toFormatter();

            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            for (ConsumerRecord<String, String> record : records) {
                LogEntryDto log = mapper.readValue(record.value(), LogEntryDto.class);
                LocalDateTime dateTime = LocalDateTime.parse(log.getTimestamp(), inputFormatter);
                log.setTimestamp(dateTime.format(outputFormatter));
                recentLogs.add(log);
                if (recentLogs.size() >= 400) {
                    break;
                }
            }

            return recentLogs;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } finally {
            consumer.close();
        }
    }
}
