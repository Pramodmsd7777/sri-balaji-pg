package com.sribalajipg.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "tenants")
@Data
public class Tenant {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;              // links to login credentials

    private String fullName;
    private String phoneNumber;
    private String email;

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @OneToOne
    @JoinColumn(name = "bed_id")
    private Bed bed;

    private LocalDate joiningDate;
    private Double monthlyRent;
    private Double depositAmount;
    private boolean active = true;

    // Document references (Cloudinary URLs)
    private String aadhaarUrl;
    private String panUrl;
    private String photoUrl;
    private String agreementUrl;
}
