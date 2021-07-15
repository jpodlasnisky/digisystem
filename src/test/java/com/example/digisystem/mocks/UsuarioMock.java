package com.example.digisystem.mocks;

import com.example.digisystem.model.entity.Usuario;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UsuarioMock {
    public static Usuario usuarioMock(){
        Usuario usuario = new Usuario();
        usuario.setEmail("zeca@mail.com");
        usuario.setId(1L);
        usuario.setNome("zeca123");
        usuario.setSenha("244466666");
        return usuario;
    }

    public static List<Usuario> usuarioListMock(){
        List<Usuario> usuarioList = new ArrayList<>();
        usuarioList.add(usuarioMock());
        usuarioList.add(usuarioMock());
        usuarioList.add(usuarioMock());

        return usuarioList;
    }
}
