package com.sribalajipg.controller;

import com.sribalajipg.entity.Bed;
import com.sribalajipg.entity.Room;
import com.sribalajipg.repository.BedRepository;
import com.sribalajipg.repository.RoomRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/rooms")
public class RoomController {

    private final RoomRepository roomRepository;
    private final BedRepository bedRepository;

    public RoomController(RoomRepository roomRepository, BedRepository bedRepository) {
        this.roomRepository = roomRepository;
        this.bedRepository = bedRepository;
    }

    @GetMapping
    public List<Room> listRooms() {
        return roomRepository.findAll();
    }

    @PostMapping
    public Room addRoom(@RequestBody Room room) {
        return roomRepository.save(room);
    }

    @PutMapping("/beds/{bedId}/status")
    public Bed updateBedStatus(@PathVariable Long bedId, @RequestBody Map<String, String> body) {
        Bed bed = bedRepository.findById(bedId).orElseThrow();
        bed.setStatus(com.sribalajipg.entity.BedStatus.valueOf(body.get("status")));
        return bedRepository.save(bed);
    }

}
