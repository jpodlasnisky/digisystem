package com.example.digisystem.model.repository;

import com.example.digisystem.model.entity.Usuario;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class UsuarioRepositoryTest {

    @Autowired
    UsuarioRepository repository;

    @Autowired
    TestEntityManager entityManager;

    @Test
    void deveValidarSeExisteUmEmailNaBaseDeDados(){
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);
        boolean resultado = repository.existsByEmail("joao@mail.com");
        Assertions.assertThat(resultado).isTrue();
    }

    @Test
    void deveValidarQueNaoExisteUmEmailNaBaseDeDados(){
        boolean resultado = repository.existsByEmail("joao2@mail.com");
        Assertions.assertThat(resultado).isFalse();
    }

    @Test
    void devePersistirUmUsuarioNaBaseDeDados(){
        Usuario usuario = criarUsuario();

        Usuario usuarioSalvo = repository.save(usuario);

        Assertions.assertThat(usuarioSalvo.getId()).isNotNull();
    }

    @Test
    void deveBuscarUmUsuarioPorEmailNaBaseDeDados(){
        Usuario usuario = criarUsuario();
        entityManager.persist(usuario);

        Optional<Usuario> resultado = repository.findByEmail("joao@mail.com");
        Assertions.assertThat(resultado.isPresent()).isTrue();
    }

    @Test
    void deveRetornarVazioAoBuscarUmUsuarioPorEmailNaBaseDeDados(){

        Optional<Usuario> resultado = repository.findByEmail("joao@mail.com");
        Assertions.assertThat(resultado.isPresent()).isFalse();
    }


    public static Usuario criarUsuario(){
        return Usuario
                .builder()
                .nome("Joao")
                .email("joao@mail.com")
                .senha("123456")
                .build();
    }


}
