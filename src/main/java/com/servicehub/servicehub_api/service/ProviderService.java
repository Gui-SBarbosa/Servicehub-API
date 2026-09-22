package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.dto.ProviderRequest;
import com.servicehub.servicehub_api.dto.ProviderResponse;
import com.servicehub.servicehub_api.exception.DuplicateResourceException;
import com.servicehub.servicehub_api.exception.ResourceNotFoundException;
import com.servicehub.servicehub_api.model.Provider;
import com.servicehub.servicehub_api.repository.ProviderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProviderService {

    private final ProviderRepository repository;

    public ProviderService(ProviderRepository repository) {
        this.repository = repository;
    }

    public ProviderResponse create(ProviderRequest request) {
        if (repository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Já existe um provider com este e-mail");
        }
        Provider provider = new Provider();
        copy(request, provider, true);
        return ProviderResponse.from(repository.save(provider));
    }

    @Transactional(readOnly = true)
    public List<ProviderResponse> findAll() {
        return repository.findAll().stream().map(ProviderResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public ProviderResponse findById(Long id) {
        return ProviderResponse.from(getProvider(id));
    }

    public ProviderResponse update(Long id, ProviderRequest request) {
        Provider provider = getProvider(id);
        if (repository.existsByEmailAndIdNot(request.email(), id)) {
            throw new DuplicateResourceException("Já existe um provider com este e-mail");
        }
        copy(request, provider, false);
        return ProviderResponse.from(repository.save(provider));
    }

    public void delete(Long id) {
        Provider provider = getProvider(id);
        repository.delete(provider);
    }

    private Provider getProvider(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Provider não encontrado: " + id));
    }

    private void copy(ProviderRequest request, Provider provider, boolean creating) {
        provider.setName(request.name());
        provider.setEmail(request.email());
        provider.setPhone(request.phone());
        provider.setBio(request.bio());
        if (creating) {
            provider.setActive(request.active() == null || request.active());
        } else if (request.active() != null) {
            provider.setActive(request.active());
        }
    }
}
