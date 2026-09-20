package com.ecole.dto;

import com.ecole.model.User;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;
    private String address;
    private String city;
    private String postalCode;
    private String country;
    private User.Role role;
    private String profilePicture;
    private Set<String> authorities;

    public static UserResponse fromUser(User user) {
        return UserResponse.builder()
            .id(user.getId())
            .email(user.getEmail())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .phone(user.getPhone())
            .address(user.getAddress())
            .city(user.getCity())
            .postalCode(user.getPostalCode())
            .country(user.getCountry())
            .role(user.getRole())
            .profilePicture(user.getProfilePicture())
            .authorities(user.getAuthorities().stream()
                .map(authority -> authority.getName())
                .collect(Collectors.toSet()))
            .build();
    }
}
