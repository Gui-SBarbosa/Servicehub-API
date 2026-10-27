package com.servicehub.servicehub_api.controller;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/service-requests")
public class ServiceRequestController {
    private final ServiceRequestService service; public ServiceRequestController(ServiceRequestService service) { this.service = service; }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ServiceRequestResponse create(@Valid @RequestBody ServiceRequestCreateRequest request) { return service.create(request); }
    @GetMapping("/mine") public List<ServiceRequestResponse> mine() { return service.mine(); }
    @GetMapping("/provider") public List<ServiceRequestResponse> providerRequests() { return service.providerRequests(); }
    @PatchMapping("/{id}/status") public ServiceRequestResponse updateStatus(@PathVariable Long id, @Valid @RequestBody ServiceRequestStatusRequest request) { return service.updateStatus(id, request); }
}
