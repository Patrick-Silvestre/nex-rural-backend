package com.agromach.security;

import com.agromach.entity.Role;
import com.agromach.entity.Usuario;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * Utilitario de autorizacao. Centraliza a checagem de posse (ownership) usada pelos services
 * para impedir que um usuario autenticado altere recursos de outro usuario (IDOR).
 */
public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static Usuario currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof Usuario usuario)) {
            throw new AccessDeniedException("Usuario nao autenticado");
        }
        return usuario;
    }

    /**
     * Garante que o usuario autenticado seja o dono do recurso (ownerId) ou ADMIN.
     */
    public static void requireOwnerOrAdmin(Long ownerId) {
        requireAnyOwnerOrAdmin(ownerId);
    }

    /**
     * Garante que o usuario autenticado seja dono de pelo menos um dos IDs informados, ou ADMIN.
     * Util para recursos com mais de uma parte envolvida (ex: comprador/vendedor de um pedido).
     */
    public static void requireAnyOwnerOrAdmin(Long... ownerIds) {
        Usuario usuario = currentUser();
        if (usuario.getRole() == Role.ADMIN) {
            return;
        }

        for (Long ownerId : ownerIds) {
            if (ownerId != null && usuario.getId().equals(ownerId)) {
                return;
            }
        }

        throw new AccessDeniedException("Voce nao tem permissao para acessar este recurso");
    }
}
