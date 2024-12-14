package com.example.JobBoard.controller;
import com.example.JobBoard.model.Role;
import com.example.JobBoard.model.User;
import com.example.JobBoard.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginData, HttpServletRequest request) {
        String email = loginData.get("email");
        String password = loginData.get("password");

        Optional<User> user = userService.login(email, password);

        if (user.isPresent()) {
            HttpSession session = request.getSession();
            Role role = user.get().getRole();

            // Set session attributes
            session.setAttribute("email", user.get().getEmail());
            session.setAttribute("role", user.get().getRole());
            session.setAttribute("fullname", user.get().getName());

            // Prepare response data
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("role", role);
            responseData.put("email", user.get().getEmail());
            responseData.put("fullname", user.get().getName());

            // Return a response with status 200 (OK)
            return ResponseEntity.ok(responseData);
        } else {
            // Return error response if login failed
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("errorMessage", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse); // 401 Unauthorized
        }
    }
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        if (email == null || email.isEmpty()) {
            return ResponseEntity.badRequest().body("Email is required.");
        }

        // Generate reset token for the provided email
        String token = userService.createPasswordResetToken(email);

        if (token != null) {
            // Create a reset link (replace with your React frontend's URL)
            String resetLink = "http://localhost:3000/resetpassword?token=" + token;

            try {
                // Send the reset email
                userService.sendResetEmail(email, resetLink);
                return ResponseEntity.ok("Reset email sent.");
            } catch (Exception e) {
                // Log the exception (you might want to use a logging framework here)
                System.err.println("Error sending reset email: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to send reset email. Please try again later.");
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Email not found.");
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
