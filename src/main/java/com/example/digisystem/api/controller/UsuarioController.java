package com.example.digisystem.api.controller;

import com.example.digisystem.api.dto.UsuarioDTO;
import com.example.digisystem.exceptions.ErroAutenticacao;
import com.example.digisystem.exceptions.RegraNegocioException;
import com.example.digisystem.model.entity.Usuario;
import com.example.digisystem.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@Api(value="Desafio Técnico Digisystem")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @PostMapping
    @ApiOperation(value="Persiste um usuário novo na aplicação")
    public ResponseEntity salvarUsuario(@RequestBody UsuarioDTO dto){
        Usuario usuario = Usuario.builder()
                .nome(dto.getNome())
                .senha(dto.getSenha())
                .email(dto.getEmail())
                .build();

        try {
            Usuario usuarioSalvo = service.salvarUsuario(usuario);
            return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
        } catch (RegraNegocioException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/autenticar")
    @ApiOperation(value="Simula autenticação de um usuário da aplicação")
    public ResponseEntity autenticarUsuario(@RequestBody UsuarioDTO dto){
        try{
            Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
            return ResponseEntity.ok(usuarioAutenticado);
        } catch (ErroAutenticacao e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    @ApiOperation(value="Lista todos usuários cadastrados na aplicação")
    public ResponseEntity listarUsuarios(){
        try {
            List<Usuario> usuariosList = service.listarUsuarios();
            return new ResponseEntity(usuariosList, HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @ApiOperation(value="Lista um único usuário com base em seu ID")
    public ResponseEntity listarUsuario(@PathVariable(value="id") long id){
        try {
            Optional<Usuario> usuario = service.listarUsuario(id);
            return new ResponseEntity(usuario, HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping
    @ApiOperation(value="Remove um usuário cadastrado na aplicação")
    public ResponseEntity deletarUsuario(@RequestBody Usuario usuario){
        try{
            service.deletarUsuario(usuario);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping
    @ApiOperation(value="Altera um usuário cadastrado na aplicação")
    public ResponseEntity alterarUsuario(@RequestBody Usuario usuario){
        try {
            Usuario usuarioAlterado = service.alterarUsuario(usuario);
            return new ResponseEntity(usuarioAlterado, HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/valida-git/{id}")
    @ApiOperation(value="Valida se o usuário da aplicação possui uma conta no Github. Tendo a conta, imprime o usuário.")
    public ResponseEntity temCadastroGithub(@PathVariable(value="id") long id){
        try {
            Usuario usuarioComContaGitHub = service.temCadastroGithub(id);
            return new ResponseEntity(usuarioComContaGitHub, HttpStatus.OK);
        } catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


}
