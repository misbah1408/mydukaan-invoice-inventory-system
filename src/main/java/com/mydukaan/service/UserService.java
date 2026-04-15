package com.mydukaan.service;

import com.mydukaan.dto.request.UserRequest;
import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.response.UserResponse;
import com.mydukaan.model.User;
import com.mydukaan.repository.StoreRepository;
import com.mydukaan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public UserService(UserRepository userRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.storeRepository = storeRepository;
    }

    public ApiResponse<UserResponse> getUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new ApiResponse<>(true, "User fetched successfully", mapToUserResponse(user));
    }


    public ApiResponse<UserResponse> updateUser(Long userId, UserRequest userRequest) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setEmail(userRequest.getEmail());
        user.setRole(userRequest.getRole());
        user.setName(userRequest.getName());

        userRepository.save(user);

        return new ApiResponse<>(true, "User updated successfully", mapToUserResponse(user));
    }

    public ApiResponse<List<UserResponse>> getUsersByStore(Long storeId){
        List<User> users = storeRepository.findAllUsers(storeId);
        List<UserResponse> userResponses = users.stream().map(this::mapToUserResponse).toList();
        return new ApiResponse<>(true, "", userResponses);
    }

    private UserResponse mapToUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole().name())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
