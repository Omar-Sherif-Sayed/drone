package com.drone.dto;

public record TripItemsDto(Long id,
                           Long tripId,
                           ItemDto itemDto,
                           Integer itemCount) {
}
