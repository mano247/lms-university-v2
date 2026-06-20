package com.lmsuniversity.security.jwt;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.util.HashMap;

/**
 * Caps /api/auth/signin attempts per client IP to slow down credential-stuffing
 * and brute-force attacks. 5 attempts per minute, refilled gradually rather than
 * reset all at once, to avoid bursts right after the window rolls over.
 */
@Component
public class LoginRateLimitingFilter extends OncePerRequestFilter {

	private static final String SIGNIN_PATH = "/api/auth/signin";
	private static final int CAPACITY = 5;
	private static final Duration REFILL_PERIOD = Duration.ofMinutes(1);

	private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (!HttpMethod.POST.matches(request.getMethod()) || !SIGNIN_PATH.equals(request.getServletPath())) {
			filterChain.doFilter(request, response);
			return;
		}

		Bucket bucket = buckets.computeIfAbsent(clientIp(request), key -> newBucket());

		if (bucket.tryConsume(1)) {
			filterChain.doFilter(request, response);
			return;
		}

		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(429);

		Map<String, Object> body = new HashMap<>();
		body.put("status", 429);
		body.put("error", "Too Many Requests");
		body.put("message", "Too many login attempts. Please try again later.");
		body.put("path", request.getServletPath());

		new ObjectMapper().writeValue(response.getOutputStream(), body);
	}

	private Bucket newBucket() {
		return Bucket.builder()
				.addLimit(limit -> limit.capacity(CAPACITY).refillGreedy(CAPACITY, REFILL_PERIOD))
				.build();
	}

	private String clientIp(HttpServletRequest request) {
		String forwardedFor = request.getHeader("X-Forwarded-For");
		if (forwardedFor != null && !forwardedFor.isBlank()) {
			return forwardedFor.split(",")[0].trim();
		}
		return request.getRemoteAddr();
	}
}
