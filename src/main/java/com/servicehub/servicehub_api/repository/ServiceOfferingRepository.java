package com.servicehub.servicehub_api.repository;
import com.servicehub.servicehub_api.model.ServiceOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ServiceOfferingRepository extends JpaRepository<ServiceOffering, Long> { List<ServiceOffering> findByActiveTrue(); List<ServiceOffering> findByProviderId(Long providerId); }
