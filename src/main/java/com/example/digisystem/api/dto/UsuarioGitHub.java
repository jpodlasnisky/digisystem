package com.example.digisystem.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
public class UsuarioGitHub {

    private String login;
    private BigInteger id;
    private String node_id;
    private String avatar_url;
    private String gravatar_id;
    private String url;
    private String html_url;
    private String followers_url;
    private String subscriptions_url;
    private String organizations_url;
    private String repos_url;
    private String received_events_url;
    private String type;
    private BigInteger score;
    private String following_url;
    private String gists_url;
    private String starred_url;
    private String events_url;
    private boolean site_admin;
    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private boolean hireable;
    private String twitter_username;
    private BigInteger public_repos;
    private BigInteger public_gists;
    private BigInteger followers;
    private BigInteger following;
    private String created_at;
    private String updated_at;

}
