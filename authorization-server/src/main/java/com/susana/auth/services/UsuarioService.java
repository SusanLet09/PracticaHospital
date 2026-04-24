package com.susana.auth.services;

import java.util.Set;

import com.susana.auth.dto.UsuarioRequest;
import com.susana.auth.dto.UsuarioResponse;

public interface UsuarioService {

    Set<UsuarioResponse> listar();

    UsuarioResponse registrar(UsuarioRequest request);

    UsuarioResponse eliminar(String username);
}
