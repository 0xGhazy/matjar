package com.matjar.api.userManagement.service;

import com.matjar.api.userManagement.adapter.UserAdapter;
import com.matjar.api.userManagement.dto.ActivateUserRequest;
import com.matjar.api.userManagement.dto.UserDto;
import com.matjar.api.userManagement.entity.Code;
import com.matjar.api.userManagement.entity.User;
import com.matjar.api.userManagement.enums.UserStatus;
import com.matjar.api.exceptionHandler.GenericError;
import com.matjar.api.exceptionHandler.Ineligible;
import com.matjar.api.exceptionHandler.NotFound;
import com.matjar.api.userManagement.repository.UserRepository;
import com.matjar.api.userManagement.utils.DateTimeHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class UserService {

    private static final Logger log = LogManager.getLogger(UserService.class);
    private final UserAdapter userAdapter = new UserAdapter();
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private UserRepository userRepository;
    @Autowired private CodeService codeService;
    @Value("${user.minimum.age}")
    private int minimumAge;

    public void registerUser(UserDto userDto) {

        // Check if the email already exists
        if (isEmailAlreadyRegistered(userDto.getEmail())) {
            log.info("Email {} is already registered.", userDto.getEmail());
            throw new GenericError("The provided email is already registered.");
        }

        // Validate user’s age against the minimum allowed age
        int userAge = DateTimeHandler.calculateAge(userDto.getDateOfBirth());
        if (userAge < minimumAge) {
            log.warn("User with email {} is below the minimum age limit.", userDto.getEmail());
            throw new Ineligible("User must be at least 18 years old.");
        }

        // Convert UserDto to User entity and set attributes
        User user = userAdapter.toEntity(userDto);
        user.setId(UUID.randomUUID().toString());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setStatus(UserStatus.PENDING_ACTIVATION);
        user.setCreatedAt(LocalDateTime.now());
        log.debug("User system attributes have been set successfully for userId={}", user.getId());

        // Persist user in the database
        persist(user);

        // Issue activation code for the user’s email
        Code activationCode = codeService.issueActivationCode(user.getEmail());

        // TODO: Send an email to the user with the generated activation code
    }



    public void activateUser(ActivateUserRequest activateUserRequest) {

        String email = activateUserRequest.getEmail();
        String plainCode = activateUserRequest.getCode();

        if (!isEmailAlreadyRegistered(email)) {
            log.warn("Email is not registered.");
            throw new NotFound("Email is not registered.");
        }

        // Fetch user by email
        User user = userRepository.findByEmail(email);

        // Validate the activation code
        codeService.validateCode(email, plainCode);

        // Update User to be active
        user.setStatus(UserStatus.ACTIVE);
        persist(user);
        log.info("User status set to ACTIVE.");
    }


    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private User persist(User user) {
        try {
            User createdUser = userRepository.save(user);
            log.debug("User persisted successfully, user={}", createdUser);
            return createdUser;
        } catch (Exception e) {
            log.error("Failed to persist user, error={}", e.getMessage());
            throw e;
        }
    }

}
