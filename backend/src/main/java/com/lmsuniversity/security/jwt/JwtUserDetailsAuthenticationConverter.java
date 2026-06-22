package com.lmsuniversity.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import com.lmsuniversity.security.services.UserDetailsServiceImpl;

@Component
public class JwtUserDetailsAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Override
	public AbstractAuthenticationToken convert(Jwt jwt) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(jwt.getSubject());
		return new UsernamePasswordAuthenticationToken(userDetails, jwt, userDetails.getAuthorities());
	}
}
