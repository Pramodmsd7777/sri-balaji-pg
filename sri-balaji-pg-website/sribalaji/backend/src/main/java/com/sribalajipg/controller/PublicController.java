package com.sribalajipg.controller;

import com.sribalajipg.entity.BedStatus;
import com.sribalajipg.repository.BedRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

// Unauthenticated endpoints consumed by the public homepage
@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final BedRepository bedRepository;

    public PublicController(BedRepository bedRepository) {
        this.bedRepository = bedRepository;
    }

    // Powers the "Total Beds / Occupied / Available / Occupancy %" strip on the homepage
    @GetMapping("/capacity")
    public Map<String, Object> capacity() {
        long total = bedRepository.count();
        long occupied = bedRepository.findByStatus(BedStatus.OCCUPIED).size();
        double occupancy = total == 0 ? 0 : (occupied * 100.0) / total;
        return Map.of(
                "totalBeds", total,
                "occupiedBeds", occupied,
                "availableBeds", total - occupied,
                "occupancyPercent", Math.round(occupancy * 10) / 10.0
        );
    }
}
