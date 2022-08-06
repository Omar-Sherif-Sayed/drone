package com.drone.service;

import com.drone.dto.DroneDto;
import com.drone.entity.Drone;
import com.drone.entity.DroneModel;
import com.drone.entity.Trip;
import com.drone.enums.DroneState;
import com.drone.error.ApplicationFailed;
import com.drone.model.CreateDroneRequest;
import com.drone.repository.DroneModelRepository;
import com.drone.repository.DroneRepository;
import com.drone.repository.TripRepository;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@Transactional
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private DroneModelRepository droneModelRepository;

    @Autowired
    private TripRepository tripRepository;

    public Either<ApplicationFailed, DroneDto> registerDrone(CreateDroneRequest createDroneRequest) {

        Either<ApplicationFailed, Boolean> validate = createDroneRequest.validate();
        if (validate.isLeft())
            return Either.left(validate.getLeft());

        if (droneRepository.findBySerial(createDroneRequest.serial()) != null)
            return Either.left(new ApplicationFailed.AlreadyExist("serial"));

        DroneModel droneModel = droneModelRepository.findByName(createDroneRequest.model());

        Drone drone = new Drone();
        drone.setSerial(createDroneRequest.serial());
        drone.setModel(droneModel);
        drone.setState(DroneState.IDLE);
        drone.setBatteryCapacity(100L);
        return Either.right(droneRepository.save(drone).toDroneDto());
    }

    public List<DroneDto> findAll() {
        return droneRepository.findAll().stream().map(Drone::toDroneDto).toList();
    }

    public DroneDto findById(Long id) {
        Drone drone = droneRepository.findById(id).orElse(null);
        if (drone != null) return drone.toDroneDto();
        else return null;
    }

    public List<DroneDto> availableDronesForLoading() {
        return droneRepository.findAllByState(DroneState.IDLE).stream().map(Drone::toDroneDto).toList();
    }

    public Either<ApplicationFailed, DroneDto> nextDroneState(Long id) {
        Drone drone = droneRepository.findById(id).orElse(null);
        if (drone == null) return Either.left(new ApplicationFailed.NotExist("drone"));

        switch (drone.getState()) {
            case IDLE:
                return Either.left(new ApplicationFailed.AssignItemToChangeIdealDroneState());
            case LOADING:
                drone.setState(DroneState.LOADED);
                break;
            case LOADED:
                if (drone.getBatteryCapacity() < 25L)
                    return Either.left(new ApplicationFailed.BadDroneBattery(drone.getBatteryCapacity(), 25L));

                drone.setState(DroneState.DELIVERING);
                Trip trip = tripRepository.findByDroneIdAndDroneStateAndEndTimeIsNull(id, DroneState.LOADED);
                trip.setStartTime(new Date());
                tripRepository.save(trip);
                break;
            case DELIVERING:
                drone.setState(DroneState.DELIVERED);
                Trip trip1 = tripRepository.findByDroneIdAndDroneStateAndEndTimeIsNull(id, DroneState.DELIVERING);
                trip1.setEndTime(new Date());
                tripRepository.save(trip1);
                break;
            case DELIVERED:
                drone.setState(DroneState.RETURNING);
                break;
            case RETURNING:
                drone.setState(DroneState.IDLE);
                break;
        }

        return Either.right(droneRepository.save(drone).toDroneDto());
    }

    public Either<ApplicationFailed, DroneDto> rechargeBatteryCapacity(Long id) {
        Drone drone = droneRepository.findById(id).orElse(null);
        if (drone == null) return Either.left(new ApplicationFailed.NotExist("drone"));

        drone.setBatteryCapacity(100L);
        return Either.right(droneRepository.save(drone).toDroneDto());
    }

}
