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

import java.util.ArrayList;
import java.util.List;

@Component
@EnableScheduling
public class DroneScheduler {

    private static final Logger logger = LogManager.getLogger(DroneScheduler.class);

    @Autowired
    private DroneRepository droneRepository;

    @Scheduled(cron = "${scheduler.cron-expression.decrease-drone-battery}")
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

    @Scheduled(cron = "${scheduler.cron-expression.change-drone-state}")
    void changeDroneState() {

        logger.info("start changeDroneState cron job");

        List<Drone> drones = droneRepository.findAll();

        List<Drone> deliveringDrones = drones.stream().filter(drone -> drone.getState().equals(DroneState.DELIVERING)).toList();
        List<Drone> deliveredDrones = drones.stream().filter(drone -> drone.getState().equals(DroneState.DELIVERED)).toList();
        List<Drone> returningDrones = drones.stream().filter(drone -> drone.getState().equals(DroneState.RETURNING)).toList();

        deliveringDrones.forEach(drone -> drone.setState(DroneState.DELIVERED));
        deliveredDrones.forEach(drone -> drone.setState(DroneState.RETURNING));
        returningDrones.forEach(drone -> drone.setState(DroneState.IDLE));

        List<Drone> newDroneStates = new ArrayList<>();
        newDroneStates.addAll(deliveringDrones);
        newDroneStates.addAll(deliveredDrones);
        newDroneStates.addAll(returningDrones);

        droneRepository.saveAll(newDroneStates);

        logger.info("end changeDroneState cron job");
    }

}
