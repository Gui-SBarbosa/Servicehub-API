package com.servicehub.servicehub_api.controller;

import com.servicehub.servicehub_api.dto.ProviderRequest;
import com.servicehub.servicehub_api.dto.ProviderResponse;
import com.servicehub.servicehub_api.service.ProviderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/providers")
@Tag(name = "Providers", description = "Prestadores de serviços cadastrados no ServiceHub")
public class ProviderController {

    private final ProviderService service;

    public ProviderController(ProviderService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar provider", description = "Cadastra um novo prestador de serviço.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Provider criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    public ProviderResponse create(@Valid @RequestBody ProviderRequest request) {
        return service.create(request);
    }

    @GetMapping
    @Operation(summary = "Listar providers", description = "Retorna todos os prestadores cadastrados.")
    @ApiResponse(responseCode = "200", description = "Lista retornada com sucesso")
    public List<ProviderResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar provider", description = "Busca um prestador pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provider encontrado"),
            @ApiResponse(responseCode = "404", description = "Provider não encontrado")
    })
    public ProviderResponse findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar provider", description = "Substitui os dados de um prestador existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Provider atualizado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Provider não encontrado"),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado")
    })
    public ProviderResponse update(@PathVariable Long id, @Valid @RequestBody ProviderRequest request) {
        return service.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Excluir provider", description = "Remove um prestador pelo identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Provider excluído"),
            @ApiResponse(responseCode = "404", description = "Provider não encontrado")
    })
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
