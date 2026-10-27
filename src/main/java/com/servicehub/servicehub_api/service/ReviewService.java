package com.servicehub.servicehub_api.service;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.exception.BusinessRuleException;
import com.servicehub.servicehub_api.exception.DuplicateResourceException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.*;
import com.servicehub.servicehub_api.repository.ReviewRepository;
import com.servicehub.servicehub_api.repository.ServiceRequestRepository;
import com.servicehub.servicehub_api.util.InputSanitizer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @Transactional
public class ReviewService {
    private final ReviewRepository reviews; private final ServiceRequestRepository requests; private final CurrentUserService current;
    public ReviewService(ReviewRepository reviews, ServiceRequestRepository requests, CurrentUserService current) { this.reviews = reviews; this.requests = requests; this.current = current; }
    public ReviewResponse create(ReviewRequest dto) { UserAccount user = current.get(); ServiceRequest request = requests.findById(dto.requestId()).orElseThrow(() -> new ResourceNotFoundException("Solicitação não encontrada: " + dto.requestId())); if (!request.getClient().getId().equals(user.getId())) throw new BusinessRuleException("Apenas o cliente da solicitação pode avaliar"); if (request.getStatus() != ServiceRequestStatus.COMPLETED) throw new BusinessRuleException("A solicitação precisa estar concluída para receber avaliação"); if (reviews.existsByRequestId(request.getId())) throw new DuplicateResourceException("Esta solicitação já foi avaliada"); return ReviewResponse.from(reviews.save(new Review(request, user, dto.rating(), InputSanitizer.text(dto.comment())))); }
}
