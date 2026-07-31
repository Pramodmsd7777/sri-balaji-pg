package com.sribalajipg.controller;

import com.sribalajipg.entity.Tenant;
import com.sribalajipg.repository.TenantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/tenants")
public class TenantController {

    private final TenantRepository tenantRepository;

    public TenantController(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
    }

    @GetMapping
    public List<Tenant> listAll(@RequestParam(required = false) String search) {
        if (search == null || search.isBlank()) return tenantRepository.findAll();
        return tenantRepository.findByFullNameContainingIgnoreCaseOrPhoneNumberContaining(search, search);
    }

    @PostMapping
    public Tenant addTenant(@RequestBody Tenant tenant) {
        return tenantRepository.save(tenant);
    }

    @PutMapping("/{id}")
    public Tenant updateTenant(@PathVariable Long id, @RequestBody Tenant updated) {
        updated.setId(id);
        return tenantRepository.save(updated);
    }

    @DeleteMapping("/{id}")
    public void removeTenant(@PathVariable Long id) {
        tenantRepository.deleteById(id);
    }
}
