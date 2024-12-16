package com.example.JobBoard.controller;

import com.example.JobBoard.model.User;
import com.example.JobBoard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/add")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.createUser(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/allUsers")
    public Page<User> getAllUsers(
            @RequestParam(defaultValue = "0") int page,  // Default page = 0
            @RequestParam(defaultValue = "10") int size // Default size = 10
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return userService.getAllUsers(pageable);
    }
    @PutMapping("/update/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        Optional<User> existingUserOpt = userService.getUserById(id);

        if (existingUserOpt.isPresent()) {
            // Get the existing user
            User existingUser = existingUserOpt.get();

            // Update the user's fields with the new data
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPassword(updatedUser.getPassword());
            existingUser.setName(updatedUser.getName());
            existingUser.setRole(updatedUser.getRole());

            // Save the updated user back to the database
            User savedUser = userService.updateUser(existingUser);

            return ResponseEntity.ok(savedUser);  // Return the updated user
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);  // User not found
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userService.login(email, password);

        if (user.isPresent()) {
            User loggedInUser = user.get();

            // Trigger 2FA
            userService.sendTwoFactorToken(loggedInUser);

            // Prepare response data with role information
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "2FA code sent to your email.");
            responseData.put("needs2FA", true);
            responseData.put("role", loggedInUser.getRole().getName()); // Add role to response
            responseData.put("email", loggedInUser.getEmail());
            responseData.put("userId", loggedInUser.getId());

            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid email or password"));
        }
    }

    @PostMapping("/validate-2fa")
    public ResponseEntity<?> validateTwoFactorToken(@RequestBody Map<String, String> request, HttpServletRequest httpRequest) {
        String token = request.get("token");
        Optional<User> user = userService.validateTwoFactorToken(token);

        if (user.isPresent()) {
            User loggedInUser = user.get();

            // Prepare a comprehensive response
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("message", "Login successful");
            responseData.put("userId", loggedInUser.getId());
            responseData.put("email", loggedInUser.getEmail());
            responseData.put("name", loggedInUser.getName());
            responseData.put("role", loggedInUser.getRole().getName());

            return ResponseEntity.ok(responseData);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired 2FA token"));
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        String token = userService.createPasswordResetToken(email);

        if (token != null) {
            String resetLink = "http://localhost:3000/resetpassword?token=" + token;

            try {
                userService.sendResetEmail(email, resetLink);
                return ResponseEntity.ok("Reset email sent.");
            } catch (Exception e) {
                System.err.println("Error sending reset email: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send reset email. Please try again later.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Email not found.");
    }

    @PostMapping("/resetpasswords")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");

        if (token == null || newPassword == null) {
            return ResponseEntity.badRequest().body("Token and new password are required.");
        }

        boolean success = userService.updatePassword(token, newPassword);

        if (success) {
            return ResponseEntity.ok("Password reset successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
        }
    }
}
