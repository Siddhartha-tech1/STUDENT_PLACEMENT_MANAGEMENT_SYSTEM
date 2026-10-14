package com.placement.spms.controller;

import com.placement.spms.dto.ApplicationDtos.InterviewRequest;
import com.placement.spms.dto.ApplicationDtos.InterviewResponse;
import com.placement.spms.service.InterviewService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/interviews")
public class InterviewController {
    private final InterviewService interviewService;

    public InterviewController(InterviewService interviewService) {
        this.interviewService = interviewService;
    }

    @PostMapping
    public InterviewResponse create(@Valid @RequestBody InterviewRequest request) {
        return interviewService.save(request);
    }

    @PutMapping("/{id}")
    public InterviewResponse update(@PathVariable Long id, @Valid @RequestBody InterviewRequest request) {
        return interviewService.save(new InterviewRequest(request.applicationId(), request.interviewRound(),
                request.interviewDateTime(), request.mode(), request.locationOrMeetingDetails(), request.instructions()));
    }

    @GetMapping("/my")
    public List<InterviewResponse> my() {
        return interviewService.my();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        interviewService.delete(id);
    }
}
