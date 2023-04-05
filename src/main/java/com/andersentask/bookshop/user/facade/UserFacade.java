package com.andersentask.bookshop.user.facade;

import static com.andersentask.bookshop.user.domain.model.Role.ROLE_USER;

import com.andersentask.bookshop.user.domain.dto.UserDto;
import com.andersentask.bookshop.user.domain.model.User;
import com.andersentask.bookshop.user.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;


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
     * @param lastName  - the user's last name as a String
     * @param email     - the user's email address as a String
     * @param password  - the user's password as a String
     * @return - true if the registration was successful, false otherwise
     */
    public boolean buildUserForRegistration(String firstName, String lastName, String email, String password) {
        return userService.registration(User.builder()
                .lastName(lastName)
                .firstName(firstName)
                .email(email)
                .role(ROLE_USER)
                .password(password)
                .build());
    }

    public UserDto addUserIntoEntity(String email) {
        try {
            return userService.findByEmail(email);
        } catch (EntityNotFoundException exception) {
            Scanner scanner = new Scanner(System.in);

            System.out.println("You need to register.");

            System.out.println("Enter your password: ");
            String password = scanner.nextLine();

            System.out.println("Enter your first name: ");
            String firstName = scanner.nextLine();

            System.out.println("Enter your last name: ");
            String lastName = scanner.nextLine();

            buildUserForRegistration(firstName, lastName, email, password);

            return userService.findByEmail(email);
        }
    }

}
