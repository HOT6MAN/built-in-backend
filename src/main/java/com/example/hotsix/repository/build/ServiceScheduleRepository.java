package com.example.hotsix.repository.build;

import com.example.hotsix.model.ServiceSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceScheduleRepository extends JpaRepository<ServiceSchedule, Long>, ServiceScheduleRepositoryCustom {
}
