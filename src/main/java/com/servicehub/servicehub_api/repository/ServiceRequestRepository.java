package com.servicehub.servicehub_api.repository;
import com.servicehub.servicehub_api.model.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> { List<ServiceRequest> findByClientId(Long clientId); List<ServiceRequest> findByServiceProviderId(Long providerId); }
