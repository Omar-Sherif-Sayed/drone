package com.drone.dto;

public record ItemDto(Long id,
                      String name,
                      String code,
                      Long weight,
                      String imageUrl) {
}
