package com.bilgeadam.controller;

import com.bilgeadam.dto.request.GivePointRequestDto;
import com.bilgeadam.repository.entity.Point;
import com.bilgeadam.service.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/point")
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping("/give-point/{token}")
    public ResponseEntity<Boolean> givePoint(@RequestBody GivePointRequestDto dto,@PathVariable String token){
       return ResponseEntity.ok(pointService.givePoint(dto,token));
    }
    @GetMapping("/find-all-point/{token}")
    public ResponseEntity<List<Point>> findAllPointsFromUser(@PathVariable String token){
        return ResponseEntity.ok(pointService.findAllPointsFromUser(token));
    }

}
