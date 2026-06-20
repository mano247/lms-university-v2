package com.lmsuniversity.security.response;

import java.util.List;

public class JwtResponse {
  private String token;
  private String type = "Bearer";
  private Long id;
  private String email;
  private List<String> permissions;

  private String userType;

  public JwtResponse(String accessToken, Long id, String email, List<String> permissions, String userType) {
    this.token = accessToken;
    this.id = id;
    this.email = email;
    this.permissions = permissions;
    this.userType = userType;
  }

  public String getAccessToken() {
    return token;
  }

  public void setAccessToken(String accessToken) {
    this.token = accessToken;
  }

  public String getTokenType() {
    return type;
  }

  public void setTokenType(String tokenType) {
    this.type = tokenType;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public List<String> getPermissions() {
    return permissions;
  }

  public String getUserType() {
    return userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }
}
