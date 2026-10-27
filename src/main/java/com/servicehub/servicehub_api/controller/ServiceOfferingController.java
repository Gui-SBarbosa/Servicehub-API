package com.servicehub.servicehub_api.controller;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.service.ServiceOfferingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/services")
public class ServiceOfferingController {
    private final ServiceOfferingService service; public ServiceOfferingController(ServiceOfferingService service) { this.service = service; }
    @GetMapping public List<ServiceOfferingResponse> findAll() { return service.findPublic(); }
    @GetMapping("/{id}") public ServiceOfferingResponse findById(@PathVariable Long id) { return service.findById(id); }
    @GetMapping("/mine") public List<ServiceOfferingResponse> mine() { return service.mine(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ServiceOfferingResponse create(@Valid @RequestBody ServiceOfferingRequest request) { return service.create(request); }
    @PutMapping("/{id}") public ServiceOfferingResponse update(@PathVariable Long id, @Valid @RequestBody ServiceOfferingRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void deactivate(@PathVariable Long id) { service.deactivate(id); }
}
