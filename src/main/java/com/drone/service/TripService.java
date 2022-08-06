package com.drone.service;

import com.drone.dto.TripDto;
import com.drone.entity.Drone;
import com.drone.entity.Item;
import com.drone.entity.Trip;
import com.drone.entity.TripItems;
import com.drone.enums.DroneState;
import com.drone.error.ApplicationFailed;
import com.drone.model.LoadItemIntoDroneRequest;
import com.drone.repository.DroneRepository;
import com.drone.repository.ItemRepository;
import com.drone.repository.TripItemsRepository;
import com.drone.repository.TripRepository;
import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@Transactional
public class TripService {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripItemsRepository tripItemsRepository;

    public Either<ApplicationFailed, Long> loadItemIntoDrone(LoadItemIntoDroneRequest loadItemIntoDroneRequest) {

        Either<ApplicationFailed, Boolean> validateLoadItemIntoDroneRequest = validateLoadItemIntoDroneRequest(loadItemIntoDroneRequest);
        if (validateLoadItemIntoDroneRequest.isLeft())
            return Either.left(validateLoadItemIntoDroneRequest.getLeft());

        Trip trip = tripRepository.findByDroneIdAndDroneStateAndEndTimeIsNull(loadItemIntoDroneRequest.droneId(), DroneState.LOADING);

        if (trip == null) {
            return Either.right(saveNewTrip(loadItemIntoDroneRequest));
        } else {
            TripItems tripItems = trip.getTripItems().stream()
                    .filter(tripItems1 -> tripItems1.getItem().getId().equals(loadItemIntoDroneRequest.itemId()))
                    .findFirst().orElse(null);
            if (tripItems != null) {
                tripItems.setItemCount(tripItems.getItemCount() + loadItemIntoDroneRequest.itemCount());
                return Either.right(tripItemsRepository.save(tripItems).getId());
            } else {
                Item item = itemRepository.findById(loadItemIntoDroneRequest.itemId()).orElse(null);
                assert item != null;
                TripItems tripItems1 = new TripItems();
                tripItems1.setTrip(trip);
                tripItems1.setItem(item);
                tripItems1.setItemCount(loadItemIntoDroneRequest.itemCount());
                return Either.right(tripItemsRepository.save(tripItems1).getId());
            }
        }
    }

    private Long saveNewTrip(LoadItemIntoDroneRequest loadItemIntoDroneRequest) {
        Drone drone = droneRepository.findById(loadItemIntoDroneRequest.droneId()).orElse(null);
        Item item = itemRepository.findById(loadItemIntoDroneRequest.itemId()).orElse(null);
        assert drone != null && item != null;

        if (!drone.getState().equals(DroneState.LOADING)) {
            drone.setState(DroneState.LOADING);
            droneRepository.save(drone);
        }

        Trip trip = new Trip();
        trip.setDrone(drone);
        Trip trip1 = tripRepository.save(trip);

        TripItems tripItems = new TripItems();
        tripItems.setTrip(trip1);
        tripItems.setItem(item);
        tripItems.setItemCount(loadItemIntoDroneRequest.itemCount());
        tripItemsRepository.save(tripItems);
        return trip1.getId();
    }

    private Either<ApplicationFailed, Boolean> validateLoadItemIntoDroneRequest(LoadItemIntoDroneRequest loadItemIntoDroneRequest) {

        Long droneId = loadItemIntoDroneRequest.droneId();
        Long itemId = loadItemIntoDroneRequest.itemId();
        Integer itemCount = loadItemIntoDroneRequest.itemCount();

        if (droneId == null) return Either.left(new ApplicationFailed.Required("droneId"));
        if (itemId == null) return Either.left(new ApplicationFailed.Required("itemId"));
        if (itemCount == null)
            return Either.left(new ApplicationFailed.Required("itemCount"));

        Drone drone = droneRepository.findById(droneId).orElse(null);
        List<DroneState> supportedStates = List.of(DroneState.IDLE, DroneState.LOADING);

        if (drone == null) return Either.left(new ApplicationFailed.NotExist("drone"));

        if (!supportedStates.contains(drone.getState()))
            return Either.left(new ApplicationFailed.DroneWrongState(supportedStates));

        if (itemCount < 1) return Either.left(new ApplicationFailed.MinCount("itemCount", 1));

        Item item = itemRepository.findById(itemId).orElse(null);

        if (item == null) return Either.left(new ApplicationFailed.NotExist("item"));

        AtomicReference<Long> currentWeight = new AtomicReference<>(item.getWeight() * Long.valueOf(itemCount));

        Long maxWeightLimit = drone.getModel().getMaxWeightLimit();

        Trip trip = tripRepository.findByDroneIdAndDroneStateAndEndTimeIsNull(droneId, DroneState.LOADING);

        if (trip != null)
            trip.getTripItems().forEach(tripItems -> currentWeight.updateAndGet(v -> v + (Long.valueOf(tripItems.getItemCount()) * tripItems.getItem().getWeight())));

        if (currentWeight.get() > maxWeightLimit)
            return Either.left(new ApplicationFailed.MaxWeight(currentWeight.get(), maxWeightLimit));

        return Either.right(true);
    }

    public List<TripDto> findAll() {
        return tripRepository.findAll().stream().map(Trip::toTripDto).toList();
    }

    public TripDto findById(Long id) {
        Trip trip = tripRepository.findById(id).orElse(null);
        if (trip != null) return trip.toTripDto();
        else return null;
    }

}
