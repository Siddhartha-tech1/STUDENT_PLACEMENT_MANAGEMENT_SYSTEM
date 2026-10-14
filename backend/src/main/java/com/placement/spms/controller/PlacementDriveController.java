package com.placement.spms.controller;

import com.placement.spms.dto.DriveDtos.*;
import com.placement.spms.entity.DriveStatus;
import com.placement.spms.service.DriveService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/placement-drives")
public class PlacementDriveController {
    private final DriveService driveService;

    public PlacementDriveController(DriveService driveService) {
        this.driveService = driveService;
    }

    @GetMapping
    public Page<DriveResponse> list(@RequestParam(required = false) String q,
                                    @RequestParam(required = false) DriveStatus status,
                                    Pageable pageable) {
        return driveService.search(q, status, pageable);
    }

    @GetMapping("/{id}")
    public DriveResponse get(@PathVariable Long id) {
        return driveService.get(id);
    }

    @PostMapping
    public DriveResponse create(@Valid @RequestBody DriveRequest request) {
        return driveService.create(request);
    }

    @PutMapping("/{id}")
    public DriveResponse update(@PathVariable Long id, @Valid @RequestBody DriveRequest request) {
        return driveService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        driveService.delete(id);
    }

    @PatchMapping("/{id}/status")
    public DriveResponse status(@PathVariable Long id, @RequestParam DriveStatus status) {
        return driveService.updateStatus(id, status);
    }

    @GetMapping("/{id}/eligibility")
    public EligibilityResponse eligibility(@PathVariable Long id) {
        return driveService.eligibility(id);
    }
}
