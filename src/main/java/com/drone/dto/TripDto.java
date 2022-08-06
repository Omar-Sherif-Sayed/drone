package com.drone.dto;

import java.util.Date;
import java.util.List;

public record TripDto(Long id,
                      DroneDto droneDto,
                      List<TripItemsDto> tripItemsDto,
                      Date startTime,
                      Date endTime) {
}
