package com.example.digisystem.exceptions;

public class ErroAutenticacao extends RuntimeException{

    public ErroAutenticacao(String msgErro){
        super(msgErro);
    }
}
