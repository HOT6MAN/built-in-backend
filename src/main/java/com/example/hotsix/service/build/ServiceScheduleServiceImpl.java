package com.example.hotsix.service.build;

import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.repository.build.ServiceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServiceScheduleServiceImpl {
    private final ServiceScheduleRepository serviceScheduleRepository;

    public Long findEmptyServiceId(){
        return serviceScheduleRepository.findEmptyService();
    }
}
