package com.Java24GroupProject.AirBnBPlatform.controllers;


import com.Java24GroupProject.AirBnBPlatform.DTOs.UserRequest;
import com.Java24GroupProject.AirBnBPlatform.DTOs.UserResponse;
import com.Java24GroupProject.AirBnBPlatform.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*registration of new users is handled by AuthenticationController*/
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        return new ResponseEntity<>(userService.getUserById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping
    public ResponseEntity<UserResponse> updateCurrentUser(@Valid @RequestBody UserRequest userRequest) {
        return new ResponseEntity<>(userService.updateUser(userRequest), HttpStatus.OK);
    }

    @PatchMapping("/addFavorite")
    public ResponseEntity<String> addFavorite(@RequestBody String listingId) {
        return new ResponseEntity<>(userService.addFavorite(listingId), HttpStatus.OK);
    }

    @PatchMapping("/removeFavorite")
    public ResponseEntity<String> removeFavorite(@RequestBody String listingId) {
        return new ResponseEntity<>(userService.removeFavorite(listingId), HttpStatus.OK);
    }

}
