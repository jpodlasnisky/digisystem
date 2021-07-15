package com.example.digisystem.service.impl;

import com.example.digisystem.api.dto.UsuarioGitHub;
import com.example.digisystem.exceptions.ErroAutenticacao;
import com.example.digisystem.exceptions.RegraNegocioException;
import com.example.digisystem.model.entity.Usuario;
import com.example.digisystem.model.repository.UsuarioRepository;
import com.example.digisystem.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Override
    public Usuario autenticar(String email, String senha) {
        Optional<Usuario> usuario = repository.findByEmail(email);
        if(!usuario.isPresent()){
            throw new ErroAutenticacao("Usuário não encontrado no sistema.");
        }

        if(!usuario.get().getSenha().equals(senha)){
            throw new ErroAutenticacao("Senha inválida. Tente novamente.");
        }
        return usuario.get();
    }

    @Override
    @Transactional
    public Usuario salvarUsuario(Usuario usuario) {
        validarEmail(usuario.getEmail());
        return repository.save(usuario);
    }

    @Override
    public void validarEmail(String email) {
        boolean emailExisteNaBase = repository.existsByEmail(email);
        if (emailExisteNaBase){
            throw new RegraNegocioException("Já existe um usuário cadastrado com este e-mail.");
        }
    }
    @Override
    public List<Usuario> listarUsuarios(){
        return repository.findAll();
    }

    @Override
    public Optional<Usuario> listarUsuario(long id){
        return repository.findById(id);
    }

    @Override
    public void deletarUsuario(Usuario usuario) {
        repository.delete(usuario);
    }

    @Override
    public Usuario alterarUsuario(Usuario usuario) {
        // TODO Criar regra para validar se troca de email será permitida
        return repository.save(usuario);
    }

    @Override
    public Usuario temCadastroGithub(long id) {
        RestTemplate restTemplate = new RestTemplate();
        Optional<Usuario> usuario = listarUsuario(id);
        String username = usuario.get().getNome();

        UsuarioGitHub usuarioGitHub = restTemplate.getForObject("https://api.github.com/users/"+username, UsuarioGitHub.class);
        if (Objects.nonNull(usuarioGitHub)){
            return usuario.get();
        }
        return null;
    }
}
