package com.andersentask.bookshop.user.domain.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;

    private Role role;
}
