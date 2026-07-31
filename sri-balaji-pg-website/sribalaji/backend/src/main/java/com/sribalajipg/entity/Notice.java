package com.sribalajipg.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "notices")
@Data
public class Notice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String category;   // Holiday, Water Shutdown, Food Menu, Events, Maintenance
    @Column(length = 2000)
    private String body;
    private LocalDateTime postedAt = LocalDateTime.now();
}
