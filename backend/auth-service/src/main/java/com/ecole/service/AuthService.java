package com.ecole.service;

import com.ecole.dto.LoginRequest;
import com.ecole.dto.RegisterRequest;
import com.ecole.dto.TokenResponse;
import com.ecole.dto.UserResponse;
import com.ecole.model.Authority;
import com.ecole.model.RefreshToken;
import com.ecole.model.User;
import com.ecole.repository.AuthorityRepository;
import com.ecole.repository.RefreshTokenRepository;
import com.ecole.repository.UserRepository;
import com.ecole.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;

    public TokenResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String jwt = tokenProvider.generateToken(userDetails);

        User user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        RefreshToken refreshToken = createRefreshToken(user);

        return TokenResponse.builder()
            .accessToken(jwt)
            .refreshToken(refreshToken.getToken())
            .expiresIn(tokenProvider.getJwtExpiration())
            .build();
    }

    public UserResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        Set<Authority> authorities = new HashSet<>();
        Authority userAuthority = authorityRepository.findByName("ROLE_USER")
            .orElseGet(() -> authorityRepository.save(
                Authority.builder().name("ROLE_USER").description("User role").build()
            ));
        authorities.add(userAuthority);

        User user = User.builder()
            .email(request.getEmail())
            .password(passwordEncoder.encode(request.getPassword()))
            .firstName(request.getFirstName())
            .lastName(request.getLastName())
            .phone(request.getPhone())
            .address(request.getAddress())
            .city(request.getCity())
            .postalCode(request.getPostalCode())
            .country(request.getCountry())
            .role(User.Role.USER)
            .authorities(authorities)
            .build();

        user = userRepository.save(user);
        return UserResponse.fromUser(user);
    }

    public TokenResponse refreshToken(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
            .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        if (token.getExpiryDate().isBefore(Instant.now())) {
            throw new RuntimeException("Refresh token expired");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(token.getUser().getEmail());
        String newJwt = tokenProvider.generateToken(userDetails);

        return TokenResponse.builder()
            .accessToken(newJwt)
            .refreshToken(refreshToken)
            .expiresIn(tokenProvider.getJwtExpiration())
            .build();
    }

    private RefreshToken createRefreshToken(User user) {
        refreshTokenRepository.deleteByUserId(user.getId());

        Instant expiryDate = Instant.now().plusMillis(7 * 24 * 60 * 60 * 1000); // 7 days

        RefreshToken refreshToken = RefreshToken.builder()
            .user(user)
            .token(UUID.randomUUID().toString())
            .expiryDate(expiryDate)
            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public void logout(String refreshToken) {
        RefreshToken token = refreshTokenRepository.findByToken(refreshToken)
            .orElse(null);
        if (token != null) {
            refreshTokenRepository.delete(token);
        }
    }
}
