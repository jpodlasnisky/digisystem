package com.example.digisystem.service;

import com.example.digisystem.api.dto.UsuarioDTO;
import com.example.digisystem.api.dto.UsuarioGitHub;
import com.example.digisystem.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    Usuario autenticar(String email, String senha);

    Usuario salvarUsuario(Usuario usuario);

    void validarEmail(String email);

    List<Usuario> listarUsuarios();

    Optional<Usuario> listarUsuario(long id);

    void deletarUsuario(Usuario usuario);

    Usuario alterarUsuario(Usuario usuario);

    Usuario temCadastroGithub(long id);


}
