package com.example.digisystem.service;

import com.example.digisystem.exceptions.ErroAutenticacao;
import com.example.digisystem.exceptions.RegraNegocioException;
import com.example.digisystem.mocks.UsuarioMock;
import com.example.digisystem.model.entity.Usuario;
import com.example.digisystem.model.repository.UsuarioRepository;
import com.example.digisystem.service.impl.UsuarioServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.verify;

@DataJpaTest
@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class UsuarioServiceTest {

    @SpyBean
    UsuarioServiceImpl service;

    @MockBean
    UsuarioRepository repository;

    @Test
    public void deveSalvarUmUsuario() {
        Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
        Usuario usuario = Usuario.builder()
                .id(1l)
                .nome("nome")
                .email("email@email.com")
                .senha("senha").build();

        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioSalvo = service.salvarUsuario(new Usuario());

        Assertions.assertThat(usuarioSalvo).isNotNull();
        Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
        Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
        Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
        Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");

    }

    @Test
    public void naoDeveSalvarUmUsuarioComEmailJaCadastrado() {

        String email = "email@email.com";
        Usuario usuario = Usuario.builder().email(email).build();
        Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);

        org.junit.jupiter.api.Assertions
                .assertThrows(RegraNegocioException.class, () -> service.salvarUsuario(usuario) ) ;

        verify( repository, Mockito.never() ).save(usuario);
    }

    @Test
    public void deveAutenticarUmUsuarioComSucesso() {
        String email = "email@email.com";
        String senha = "senha";

        Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
        Mockito.when( repository.findByEmail(email) ).thenReturn(Optional.of(usuario));

        Usuario result = service.autenticar(email, senha);

        Assertions.assertThat(result).isNotNull();

    }

    @Test
    public void deveLancarErroQUandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {

        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());

        Throwable exception = Assertions.catchThrowable( () -> service.autenticar("email@email.com", "senha") );

        Assertions.assertThat(exception)
                .isInstanceOf(ErroAutenticacao.class)
                .hasMessage("Usuário não encontrado no sistema.");
    }

    @Test
    public void deveLancarErroQuandoSenhaNaoBater() {
        String senha = "senha";
        Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
        Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));

        Throwable exception = Assertions.catchThrowable( () ->  service.autenticar("email@email.com", "123") );
        Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida. Tente novamente.");

    }

    @Test
    public void deveValidarEmail() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
        service.validarEmail("email@email.com");
    }

    @Test
    public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
        Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
        org.junit.jupiter.api.Assertions
                .assertThrows(RegraNegocioException.class, () -> service.validarEmail("email@email.com"));
    }

    @Test
    void deveRetornarUmaListaDeUsuarios(){
        Mockito.when(repository.findAll()).thenReturn(UsuarioMock.usuarioListMock());
        List<Usuario> resposta = service.listarUsuarios();
        Assertions.assertThat(resposta).isNotNull();
    }

    @Test
    void deveRetornarUmUsuario(){
        Mockito.when(repository.findById(Mockito.anyLong())).thenReturn(Optional.of(UsuarioMock.usuarioMock()));
        Optional<Usuario> resposta = service.listarUsuario(1L);
        Assertions.assertThat(resposta.isPresent()).isTrue();
    }

    @Test
    void deveDeletarUmUsuario(){
        Usuario usuario = UsuarioMock.usuarioMock();
        repository.delete(usuario);
        verify(repository, Mockito.times(1)).delete(Mockito.eq(usuario));
    }

    @Test
    void deveRetornarUmUsuarioAlterado(){
        Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(UsuarioMock.usuarioMock());
        Usuario usuarioAlterado = service.alterarUsuario(Usuario.builder()
                .id(1L)
                .nome("marcelo")
                .email("marcelo@mail.com")
                .senha("qwerty123").build());
        Assertions.assertThat(usuarioAlterado).isNotNull();
    }
}