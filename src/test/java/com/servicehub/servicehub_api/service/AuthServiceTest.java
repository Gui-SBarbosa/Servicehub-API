package com.servicehub.servicehub_api.service;

import com.servicehub.servicehub_api.dto.RegisterRequest;
import com.servicehub.servicehub_api.exception.BusinessRuleException;
import com.servicehub.servicehub_api.model.Role;
import com.servicehub.servicehub_api.model.UserAccount;
import com.servicehub.servicehub_api.repository.UserAccountRepository;
import com.servicehub.servicehub_api.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    private final UserAccountRepository users = mock(UserAccountRepository.class);
    private final PasswordEncoder encoder = mock(PasswordEncoder.class);
    private final JwtService jwt = mock(JwtService.class);
    private final AuthService service = new AuthService(users, encoder, jwt);

    @Test void registerNormalizesEmailAndHashesPassword() {
        when(users.existsByEmail("empresa@exemplo.com")).thenReturn(false);
        when(encoder.encode("senha-segura")).thenReturn("bcrypt-hash");
        when(users.save(any(UserAccount.class))).thenAnswer(invocation -> invocation.getArgument(0));
        service.register(new RegisterRequest(" Empresa X ", " EMPRESA@EXEMPLO.COM ", "senha-segura", Role.CLIENT, null, null, null));
        ArgumentCaptor<UserAccount> user = ArgumentCaptor.forClass(UserAccount.class);
        verify(users).save(user.capture());
        assertEquals("empresa@exemplo.com", user.getValue().getEmail());
        assertEquals("bcrypt-hash", user.getValue().getPasswordHash());
    }

    @Test void registerRejectsAdminRole() {
        assertThrows(BusinessRuleException.class, () -> service.register(new RegisterRequest("Admin", "admin@exemplo.com", "senha-segura", Role.ADMIN, null, null, null)));
        verifyNoInteractions(users, encoder);
    }
}
