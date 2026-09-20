package com.ecole.service;

import com.ecole.dto.UserResponse;
import com.ecole.model.Authority;
import com.ecole.model.User;
import com.ecole.repository.AuthorityRepository;
import com.ecole.repository.UserRepository;
import com.ecole.security.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GoogleAuthService {

    @Value("${app.google.client-id}")
    private String clientId;

    @Value("${app.google.client-secret}")
    private String clientSecret;

    @Value("${app.google.redirect-uri}")
    private String redirectUri;

    private final UserRepository userRepository;
    private final AuthorityRepository authorityRepository;
    private final JwtTokenProvider tokenProvider;

    private static final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private static final String GOOGLE_USER_INFO_URL = "https://www.googleapis.com/oauth2/v3/userinfo";

    public String getGoogleAuthUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth" +
            "?client_id=" + clientId +
            "&redirect_uri=" + redirectUri +
            "&response_type=code" +
            "&scope=openid%20email%20profile" +
            "&access_type=offline";
    }

    public UserResponse authenticateWithGoogle(String code) {
        String accessToken = getGoogleAccessToken(code);
        Map<String, Object> userInfo = getGoogleUserInfo(accessToken);

        String email = (String) userInfo.get("email");
        String googleId = (String) userInfo.get("sub");
        String firstName = (String) userInfo.get("given_name");
        String lastName = (String) userInfo.get("family_name");
        String picture = (String) userInfo.get("picture");

        User user = userRepository.findByGoogleId(googleId)
            .orElseGet(() -> registerGoogleUser(email, googleId, firstName, lastName, picture));

        return UserResponse.fromUser(user);
    }

    private String getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(GOOGLE_TOKEN_URL, request, Map.class);

        return (String) response.getBody().get("access_token");
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> getGoogleUserInfo(String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);
        ResponseEntity<Map> response = restTemplate.exchange(
            GOOGLE_USER_INFO_URL, HttpMethod.GET, request, Map.class);

        return response.getBody();
    }

    private User registerGoogleUser(String email, String googleId, String firstName, String lastName, String picture) {
        Set<Authority> authorities = new HashSet<>();
        Authority userAuthority = authorityRepository.findByName("ROLE_USER")
            .orElseGet(() -> authorityRepository.save(
                Authority.builder().name("ROLE_USER").description("User role").build()
            ));
        authorities.add(userAuthority);

        User user = User.builder()
            .email(email)
            .password("") // No password for Google users
            .firstName(firstName)
            .lastName(lastName)
            .phone("")
            .address("")
            .city("")
            .postalCode("")
            .country("")
            .role(User.Role.USER)
            .googleId(googleId)
            .profilePicture(picture)
            .authorities(authorities)
            .build();

        return userRepository.save(user);
    }
}
