package com.servicehub.servicehub_api.service;
import com.servicehub.servicehub_api.exception.BusinessRuleException;
import com.servicehub.servicehub_api.model.UserAccount;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
@Service
public class CurrentUserService { public UserAccount get() { Object principal = SecurityContextHolder.getContext().getAuthentication() == null ? null : SecurityContextHolder.getContext().getAuthentication().getPrincipal(); if (principal instanceof UserAccount user) return user; throw new BusinessRuleException("Usuário não autenticado"); } }
