package com.servicehub.servicehub_api.service;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.exception.BusinessRuleException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.*;
import com.servicehub.servicehub_api.repository.ServiceOfferingRepository;
import com.servicehub.servicehub_api.repository.ServiceRequestRepository;
import com.servicehub.servicehub_api.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @Transactional
public class ServiceRequestService {
    private final ServiceRequestRepository requests; private final ServiceOfferingRepository services; private final CurrentUserService current;
    public ServiceRequestService(ServiceRequestRepository requests, ServiceOfferingRepository services, CurrentUserService current) { this.requests = requests; this.services = services; this.current = current; }
    public ServiceRequestResponse create(ServiceRequestCreateRequest dto) { UserAccount client = current.get(); if (client.getRole() != Role.CLIENT) throw new BusinessRuleException("Somente empresas clientes podem contratar serviços"); ServiceOffering service = services.findById(dto.serviceId()).orElseThrow(() -> new ResourceNotFoundException("Serviço não encontrado: " + dto.serviceId())); if (!service.isActive()) throw new BusinessRuleException("Este serviço não está disponível"); return ServiceRequestResponse.from(requests.save(new ServiceRequest(service, client, dto.scheduledAt(), InputSanitizer.text(dto.notes())))); }
    @Transactional(readOnly = true) public List<ServiceRequestResponse> mine() { UserAccount user = current.get(); return requests.findByClientId(user.getId()).stream().map(ServiceRequestResponse::from).toList(); }
    @Transactional(readOnly = true) public List<ServiceRequestResponse> providerRequests() { UserAccount user = current.get(); if (user.getRole() != Role.PROVIDER) throw new BusinessRuleException("Somente prestadores acessam esta rota"); return requests.findByServiceProviderId(user.getId()).stream().map(ServiceRequestResponse::from).toList(); }
    public ServiceRequestResponse updateStatus(Long id, ServiceRequestStatusRequest dto) { ServiceRequest request = get(id); UserAccount user = current.get(); boolean providerOwnsService = user.getRole() == Role.PROVIDER && request.getService().getProvider().getId().equals(user.getId()); boolean clientOwnsRequest = user.getRole() == Role.CLIENT && request.getClient().getId().equals(user.getId()); if (user.getRole() != Role.ADMIN && !providerOwnsService && !clientOwnsRequest) throw new BusinessRuleException("Você não pode atualizar esta solicitação"); if (clientOwnsRequest && dto.status() != ServiceRequestStatus.CANCELLED) throw new BusinessRuleException("O cliente só pode cancelar uma solicitação"); validateTransition(request.getStatus(), dto.status()); request.changeStatus(dto.status()); return ServiceRequestResponse.from(request); }
    private void validateTransition(ServiceRequestStatus from, ServiceRequestStatus to) { if (from == to) return; boolean valid = switch (from) { case PENDING -> to == ServiceRequestStatus.ACCEPTED || to == ServiceRequestStatus.CANCELLED; case ACCEPTED -> to == ServiceRequestStatus.IN_PROGRESS || to == ServiceRequestStatus.CANCELLED; case IN_PROGRESS -> to == ServiceRequestStatus.COMPLETED; case COMPLETED, CANCELLED -> false; }; if (!valid) throw new BusinessRuleException("Transição de status inválida: " + from + " para " + to); }
    private ServiceRequest get(Long id) { return requests.findById(id).orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada: " + id)); }
}
