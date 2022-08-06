package com.drone.controller;

import com.drone.dto.TripDto;
import com.drone.model.LoadItemIntoDroneRequest;
import com.drone.response.BaseResponse;
import com.drone.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/trips")
public class TripController {

    @Autowired
    private TripService tripService;

    @PatchMapping
    public ResponseEntity<BaseResponse<Long>> loadItemIntoDrone(@RequestBody LoadItemIntoDroneRequest loadItemIntoDroneRequest) {
        return ResponseEntity.ok(tripService.loadItemIntoDrone(loadItemIntoDroneRequest)
                .fold(left -> new BaseResponse<>(false, left, null),
                        right -> new BaseResponse<>(true, null, right)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<TripDto>>> findAll() {
        return ResponseEntity.ok(new BaseResponse<>(true, null, tripService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<TripDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, null, tripService.findById(id)));
    }

}
