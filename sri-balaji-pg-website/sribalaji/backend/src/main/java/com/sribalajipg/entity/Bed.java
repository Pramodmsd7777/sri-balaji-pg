package com.sribalajipg.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "beds")
@Data
public class Bed {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bedLabel;   // e.g. "Bed 1"

    @ManyToOne
    @JoinColumn(name = "room_id")
    private Room room;

    @Enumerated(EnumType.STRING)
    private BedStatus status = BedStatus.VACANT;

    @OneToOne
    @JoinColumn(name = "tenant_id")
    private Tenant currentTenant;
}
