package com.drone.scheduler;

import com.drone.entity.Drone;
import com.drone.enums.DroneState;
import com.drone.repository.DroneRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@EnableScheduling
public class DroneBatteryScheduler {

    private static final Logger logger = LogManager.getLogger(DroneBatteryScheduler.class);

    @Autowired
    private DroneRepository droneRepository;

    @Scheduled(cron = "${scheduler.drone-battery.cron-expression}")
    void decreaseDroneBatteryPercentage() {

        logger.info("start decreaseDroneBatteryPercentage cron job");

        List<Drone> drones = droneRepository.findAll();

        drones.forEach(drone -> {
            Long batteryCapacity = drone.getBatteryCapacity();
            if (!drone.getState().equals(DroneState.IDLE) && batteryCapacity >= 1L)
                drone.setBatteryCapacity(batteryCapacity - 1L);
        });

        droneRepository.saveAll(drones);

        logger.info("end decreaseDroneBatteryPercentage cron job");
    }

}
