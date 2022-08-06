package com.drone.controller;

import com.drone.dto.ItemDto;
import com.drone.response.BaseResponse;
import com.drone.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public ResponseEntity<BaseResponse<ItemDto>> registerItem(@RequestParam(name = "name") String name,
                                                              @RequestParam(name = "code") String code,
                                                              @RequestParam(name = "weight") Long weight,
                                                              @RequestParam(name = "image") MultipartFile image) {
        return ResponseEntity.ok(itemService.registerItem(name, code, weight, image)
                .fold(left -> new BaseResponse<>(false, left, null),
                        right -> new BaseResponse<>(true, null, right)));
    }

    @GetMapping
    public ResponseEntity<BaseResponse<List<ItemDto>>> findAll() {
        return ResponseEntity.ok(new BaseResponse<>(true, null, itemService.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ItemDto>> findById(@PathVariable Long id) {
        return ResponseEntity.ok(new BaseResponse<>(true, null, itemService.findById(id)));
    }

}
