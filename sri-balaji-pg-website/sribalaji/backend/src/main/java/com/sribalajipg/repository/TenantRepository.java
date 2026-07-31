package com.sribalajipg.repository;

import com.sribalajipg.entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TenantRepository extends JpaRepository<Tenant, Long> {
    List<Tenant> findByFullNameContainingIgnoreCaseOrPhoneNumberContaining(String name, String phone);
}
