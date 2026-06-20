package com.lmsuniversity.security.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
  private String accessToken;

  @Builder.Default
  private String tokenType = "Bearer";

  private Long id;
  private String email;
  private List<String> permissions;
  private String userType;
}
