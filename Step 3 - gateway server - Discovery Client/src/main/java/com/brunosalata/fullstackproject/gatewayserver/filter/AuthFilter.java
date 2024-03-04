package com.brunosalata.fullstackproject.gatewayserver.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import com.brunosalata.fullstackproject.gatewayserver.service.JwtService;
import com.google.common.base.Predicate;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

	private JwtService jwtService;
	@Value("${jwt.token.prefix}")
	private String tokenPrefix;

	public AuthFilter(JwtService jwtService) {
		super(Config.class);
		this.jwtService = jwtService;
	}

	private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
		ServerHttpResponse response = exchange.getResponse();
		response.setStatusCode(httpStatus);
		return response.setComplete();
	}

	@Override
	public GatewayFilter apply(Config config) {
		return (exchange, chain) -> {

			ServerHttpRequest request = (ServerHttpRequest) exchange.getRequest();

			final List<String> apiEndpoints = List.of("/auth/login");

			Predicate<ServerHttpRequest> isApiSecured = r -> apiEndpoints.stream()
					.noneMatch(uri -> r.getURI().getPath().contains(uri));

			if (isApiSecured.apply(request)) {

				if (!request.getHeaders().containsKey("Authorization")) {
					return this.onError(exchange, "No Authorization header", HttpStatus.UNAUTHORIZED);
				}

				String token = request.getHeaders().getOrEmpty("Authorization").get(0);
				token = token.replace(tokenPrefix, "");

				try {
					jwtService.validateToken(token);
				} catch (Exception e) {
					ServerHttpResponse response = exchange.getResponse();
					response.setStatusCode(HttpStatus.BAD_REQUEST);
					return response.setComplete();
				}

				Claims claims = jwtService.getClaims(token);
				exchange.getRequest().mutate().header("x-user", String.valueOf(claims.get("sub"))).build();

				return chain.filter(exchange.mutate().request(request).build());

			}

			return chain.filter(exchange);
		};
	}

	public static class Config {
		// Put the configuration properties
	}

}
