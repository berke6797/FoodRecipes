package com.bilgeadam.service;

import com.bilgeadam.repository.IPointRepository;
import com.bilgeadam.repository.entity.Point;
import com.bilgeadam.utility.ServiceManager;
import org.springframework.stereotype.Service;

@Service
public class PointService extends ServiceManager<Point,String> {
    private final IPointRepository pointRepository;

    public PointService(IPointRepository pointRepository) {
        super(pointRepository);
        this.pointRepository = pointRepository;
    }
}
