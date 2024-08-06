package com.example.hotsix.service.build;

import com.example.hotsix.model.ServiceSchedule;
import com.example.hotsix.model.project.TeamProjectInfo;
import com.example.hotsix.repository.build.ServiceScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServiceScheduleServiceImpl {
    private final ServiceScheduleRepository serviceScheduleRepository;

    public ServiceSchedule findEmptyServiceId(){
        return serviceScheduleRepository.findEmptyService();
    }

    public List<TeamProjectInfo> findUsedProjectInfoIdByTeamId(Long teamId){
        return serviceScheduleRepository.findUsedProjectInfoIdByTeamId(teamId);
    }
}
