package com.servicehub.servicehub_api.service;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.exception.BusinessRuleException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.*;
import com.servicehub.servicehub_api.repository.ServiceOfferingRepository;
import com.servicehub.servicehub_api.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class ServiceOfferingService {
    private final ServiceOfferingRepository services; private final CurrentUserService current;
    public ServiceOfferingService(ServiceOfferingRepository services, CurrentUserService current) { this.services = services; this.current = current; }
    @Transactional(readOnly = true) public List<ServiceOfferingResponse> findPublic() { return services.findByActiveTrue().stream().map(ServiceOfferingResponse::from).toList(); }
    @Transactional(readOnly = true) public ServiceOfferingResponse findById(Long id) { return ServiceOfferingResponse.from(get(id)); }
    public ServiceOfferingResponse create(ServiceOfferingRequest request) { UserAccount user = current.get(); if (user.getRole() != Role.PROVIDER) throw new BusinessRuleException("Somente prestadores podem publicar serviços"); return ServiceOfferingResponse.from(services.save(new ServiceOffering(required(request.title(), "Título"), InputSanitizer.text(request.description()), request.price(), request.category(), user))); }
    @Transactional(readOnly = true) public List<ServiceOfferingResponse> mine() { UserAccount user = current.get(); if (user.getRole() != Role.PROVIDER) throw new BusinessRuleException("Somente prestadores possuem serviços publicados"); return services.findByProviderId(user.getId()).stream().map(ServiceOfferingResponse::from).toList(); }
    public ServiceOfferingResponse update(Long id, ServiceOfferingRequest request) { ServiceOffering service = get(id); authorizeProviderOrAdmin(service); service.update(required(request.title(), "Título"), InputSanitizer.text(request.description()), request.price(), request.category()); return ServiceOfferingResponse.from(service); }
    public void deactivate(Long id) { ServiceOffering service = get(id); authorizeProviderOrAdmin(service); service.deactivate(); }
    private ServiceOffering get(Long id) { return services.findById(id).orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + id)); }
    private void authorizeProviderOrAdmin(ServiceOffering service) { UserAccount user = current.get(); if (user.getRole() != Role.ADMIN && !service.getProvider().getId().equals(user.getId())) throw new BusinessRuleException("Você não pode alterar este serviço"); }
    private String required(String value, String field) { String sanitized = InputSanitizer.text(value); if (sanitized == null) throw new BusinessRuleException(field + " é obrigatório"); return sanitized; }
}
