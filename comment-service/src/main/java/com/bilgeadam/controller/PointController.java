package com.bilgeadam.controller;

import com.bilgeadam.dto.request.GivePointRequestDto;
import com.bilgeadam.dto.request.UpdatePointRequestDto;
import com.bilgeadam.repository.entity.Point;
import com.bilgeadam.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
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
    public ResponseEntity<Boolean> givePoint(@RequestBody GivePointRequestDto dto, @PathVariable String token) {
        return ResponseEntity.ok(pointService.givePoint(dto, token));
    }
    @PutMapping("/update-point/{token}")
    public ResponseEntity<Boolean> updatePoint(@RequestBody UpdatePointRequestDto dto,String token){
        return ResponseEntity.ok(pointService.updatePoint(dto,token));
    }
    @DeleteMapping("/delete-point/{pointId}/{token}")
    public ResponseEntity<Boolean> deletePoint(@PathVariable String pointId, @PathVariable String token) {
        return ResponseEntity.ok(pointService.deletePoint(pointId, token));
    }
    @Operation(summary = "Token ile giriş yapan kullanıcının tüm puanlarını listeler")
    @GetMapping("/find-all-point/{token}")
    public ResponseEntity<List<Point>> findAllPointsFromUser(@PathVariable String token) {
        return ResponseEntity.ok(pointService.findAllPointsFromUser(token));
    }
    @Operation(summary = "Tüm puanları listelemek için")
    @GetMapping("/find-all-point")
    public ResponseEntity<List<Point>> findAllPoints(){
        return ResponseEntity.ok(pointService.findAll());
    }


}
