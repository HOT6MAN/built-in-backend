package com.example.hotsix.controller.logs;

import com.example.hotsix.dto.logs.LogEntryDto;
import com.example.hotsix.service.kafka.DynamicKafkaListenerService;
import com.example.hotsix.service.kafka.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
public class LogController {
    private final KafkaService kafkaService;
    private final DynamicKafkaListenerService dynamicKafkaListenerService;

    @GetMapping("/{projectId}")
    public List<LogEntryDto> find400LogsByProjectId(@PathVariable("projectId") Long projectId){
        System.out.println("call find 400 log");
        String topic = "vector-test";
        List<LogEntryDto> list = kafkaService.getRecentLogs(topic, 1L,"log-group");
        for(LogEntryDto log : list){
            System.out.println(log);
        }
        return list;
    }

    @GetMapping("/active/{teamId}")
    public void readTimeLogging(@PathVariable("teamId")Long teamId){
        String topic = "vector-test";
        String groupId = "log-group";
        dynamicKafkaListenerService.createDynamicListener(teamId, topic, groupId);
    }

    @GetMapping("/inactive/{teamId}")
    public void removeListener(@PathVariable("teamId")Long teamId){
        String topic = "vector-test";
        dynamicKafkaListenerService.removeDynamicListener(teamId, topic);
    }

}
