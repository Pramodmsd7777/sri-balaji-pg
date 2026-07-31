package com.sribalajipg.repository;

import com.sribalajipg.entity.Bed;
import com.sribalajipg.entity.BedStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BedRepository extends JpaRepository<Bed, Long> {
    List<Bed> findByStatus(BedStatus status);
}
