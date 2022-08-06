package com.drone.model;

public record LoadItemIntoDroneRequest(Long droneId,
                                       Long itemId,
                                       Integer itemCount) {
}
