package com.example.hotsix.controller.logs;

import com.example.hotsix.dto.logs.LogEntryDto;
import com.example.hotsix.service.kafka.DynamicKafkaListenerService;
import com.example.hotsix.service.kafka.KafkaService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/log")
@CrossOrigin(origins = "http://localhost:5173")
public class LogController {
    private final KafkaService kafkaService;
    private final DynamicKafkaListenerService dynamicKafkaListenerService;

    @GetMapping("{serviceScheduleId}/{projectInfoId}/{configId}")
    public List<LogEntryDto> find400LogsByProjectId(@PathVariable("serviceScheduleId") Long serviceScheduleId,
                                                    @PathVariable("projectInfoId")Long projectInfoId,
                                                    @PathVariable("configId")Long configId,
                                                    @RequestParam("type") String type){
        System.out.println("call find 400 log");
        String topic = "vector-container-"+serviceScheduleId+"-"+projectInfoId+"-"+configId;
        System.out.println("topic = "+topic);
        List<LogEntryDto> list = kafkaService.getRecentLogs(topic, 1L,"log-group");
        return list;
    }

    @GetMapping("/active/{serviceScheduleId}/{projectInfoId}/{configId}")
    public void readTimeLogging(@PathVariable("serviceScheduleId")Long serviceScheduleId,
                                @PathVariable("projectInfoId")Long projectInfoId,
                                @PathVariable("configId")Long configId,
                                @RequestParam String type){
        String topic = "vector-container-"+serviceScheduleId+"-"+projectInfoId+"-"+configId;
        String groupId = "log-group";
        System.out.println("topic = "+topic);
        System.out.println("type= "+type);
        dynamicKafkaListenerService.createDynamicListener(projectInfoId, configId, topic, groupId);
    }

    @GetMapping("/inactive/{teamId}")
    public void removeListener(@PathVariable("teamId")Long teamId){
        String topic = "vector-test";
        dynamicKafkaListenerService.removeDynamicListener(teamId, topic);
    }

}
