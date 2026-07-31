package com.sribalajipg.controller;

import com.sribalajipg.entity.Complaint;
import com.sribalajipg.entity.ComplaintStatus;
import com.sribalajipg.repository.ComplaintRepository;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
public class ComplaintController {

    private final ComplaintRepository complaintRepository;

    public ComplaintController(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    @GetMapping
    public List<Complaint> all() {
        return complaintRepository.findAll();
    }

    @PostMapping
    public Complaint raise(@RequestBody Complaint complaint) {
        return complaintRepository.save(complaint);
    }

    @PutMapping("/{id}/status")
    public Complaint updateStatus(@PathVariable Long id, @RequestBody Map<String, String> body) {
        Complaint c = complaintRepository.findById(id).orElseThrow();
        c.setStatus(ComplaintStatus.valueOf(body.get("status")));
        if (body.get("assignedTo") != null) c.setAssignedTo(body.get("assignedTo"));
        if (c.getStatus() == ComplaintStatus.RESOLVED) c.setResolvedAt(LocalDateTime.now());
        return complaintRepository.save(c);
    }
}
