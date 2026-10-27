package com.servicehub.servicehub_api.service;
import com.servicehub.servicehub_api.dto.*;
import com.servicehub.servicehub_api.exception.BusinessRuleException;
import com.servicehub.servicehub_api.exception.DuplicateResourceException;
import com.servicehub.servicehub_api.model.*;
import com.servicehub.servicehub_api.repository.UserAccountRepository;
import com.servicehub.servicehub_api.security.JwtService;
import com.servicehub.servicehub_api.util.InputSanitizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service @Transactional
public class AuthService {
    private final UserAccountRepository users; private final PasswordEncoder encoder; private final JwtService jwt;
    public AuthService(UserAccountRepository users, PasswordEncoder encoder, JwtService jwt) { this.users = users; this.encoder = encoder; this.jwt = jwt; }
    public UserResponse register(RegisterRequest request) {
        if (request.role() == Role.ADMIN) throw new BusinessRuleException("O cadastro público não permite papel ADMIN");
        String email = InputSanitizer.email(request.email()); if (users.existsByEmail(email)) throw new DuplicateResourceException("Já existe usuário com este e-mail");
        UserAccount user = new UserAccount(required(request.fullName(), "Nome"), email, encoder.encode(request.password()), request.role(), InputSanitizer.text(request.phone()), InputSanitizer.text(request.bio()), InputSanitizer.text(request.avatarUrl()));
        return UserResponse.from(users.save(user));
    }
    @Transactional(readOnly = true) public AuthResponse login(LoginRequest request) {
        UserAccount user = users.findByEmail(InputSanitizer.email(request.email())).orElseThrow(() -> new BusinessRuleException("E-mail ou senha inválidos"));
        if (!user.isActive() || !encoder.matches(request.password(), user.getPasswordHash())) throw new BusinessRuleException("E-mail ou senha inválidos");
        return new AuthResponse(jwt.generate(user), "Bearer", jwt.expirationSeconds(), UserResponse.from(user));
    }
    private String required(String value, String field) { String sanitized = InputSanitizer.text(value); if (sanitized == null) throw new BusinessRuleException(field + " é obrigatório"); return sanitized; }
}
