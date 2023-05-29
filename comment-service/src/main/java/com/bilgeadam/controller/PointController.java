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
import static com.bilgeadam.constant.ApiUrls.*;

@RestController
@RequestMapping(POINT)
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    @PostMapping(GIVE_POINT+"/{token}")
    public ResponseEntity<Boolean> givePoint(@RequestBody GivePointRequestDto dto, @PathVariable String token) {
        return ResponseEntity.ok(pointService.givePoint(dto, token));
    }
    @PutMapping(UPDATE_POINT+"/{token}")
    public ResponseEntity<Boolean> updatePoint(@RequestBody UpdatePointRequestDto dto,String token){
        return ResponseEntity.ok(pointService.updatePoint(dto,token));
    }
    @DeleteMapping(DELETE_POINT+"/{pointId}/{token}")
    public ResponseEntity<Boolean> deletePoint(@PathVariable String pointId, @PathVariable String token) {
        return ResponseEntity.ok(pointService.deletePoint(pointId, token));
    }
    @Operation(summary = "Token ile giriş yapan kullanıcının tüm puanlarını listeler")
    @GetMapping(FIND_ALL_POINT+"/{token}")
    public ResponseEntity<List<Point>> findAllPointsFromUser(@PathVariable String token) {
        return ResponseEntity.ok(pointService.findAllPointsFromUser(token));
    }
    @Operation(summary = "Tüm puanları listelemek için")
    @GetMapping(FIND_ALL_POINT)
    public ResponseEntity<List<Point>> findAllPoints(){
        return ResponseEntity.ok(pointService.findAll());
    }


}
