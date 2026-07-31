package com.sribalajipg.controller;

import com.sribalajipg.entity.Notice;
import com.sribalajipg.repository.NoticeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notices")
public class NoticeController {

    private final NoticeRepository noticeRepository;

    public NoticeController(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    @GetMapping
    public List<Notice> all() {
        return noticeRepository.findAll();
    }

    @PostMapping
    public Notice post(@RequestBody Notice notice) {
        return noticeRepository.save(notice);
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        noticeRepository.deleteById(id);
    }
}
