package com.andersentask.bookshop.user.facade;

import com.andersentask.bookshop.user.model.Role;
import com.andersentask.bookshop.user.model.User;
import com.andersentask.bookshop.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    /**
     * This method is only used in console version of the application.
     * Builds a new User object with the given first name, last name, email, and password, and registers it using the
     * UserService registration method.
     *
     * @param firstName - the user's first name as a String
     * @param lastName - the user's last name as a String
     * @param email - the user's email address as a String
     * @param password - the user's password as a String
     * @return - true if the registration was successful, false otherwise
     */
    public boolean buildUserForRegistration(String firstName, String lastName, String email, String password) {
        return userService.registration(User.builder()
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .role(Role.ROLE_USER)
                .password(password)
                .build());
    }

}
