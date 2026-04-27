package com.mydukaan.service;

import com.mydukaan.dto.request.StoreRequest;
import com.mydukaan.dto.common.ApiResponse;
import com.mydukaan.dto.response.StoreResponse;
import com.mydukaan.dto.response.UserResponse;
import com.mydukaan.exception.InvalidCredentialsException;
import com.mydukaan.exception.ResourceNotFoundException;
import com.mydukaan.model.Store;
import com.mydukaan.model.User;
import com.mydukaan.repository.StoreRepository;
import com.mydukaan.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class StoreService {
    private final StoreRepository repo;
    private final UserRepository userRepo;

    @Autowired
    public StoreService(StoreRepository repo, UserRepository userRepo) {
        this.repo = repo;
        this.userRepo = userRepo;
    }

    public ApiResponse<StoreResponse> create(StoreRequest storeRequest, String userEmail) {

        if (storeRequest.getName() == null || storeRequest.getName().isBlank()) {
            throw new IllegalArgumentException("Store name is required");
        }

        String email = userEmail != null ? userEmail : getCurrentUserEmail();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Store store = new Store();
        store.setName(storeRequest.getName());
        store.setAddress(storeRequest.getAddress());
        store.setOwner(user);

        Store saved = repo.save(store);

        StoreResponse res = mapToStoreResDto(saved);

        return new ApiResponse<>(true, "Store created successfully!", res);
    }

    public ApiResponse<List<StoreResponse>> getStores() {
        String email = getCurrentUserEmail();
        Long userId = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!!!")).getId();
        List<Store> allStores = repo.findByOwnerId(userId);

        if (allStores.isEmpty()) throw new ResourceNotFoundException("No store is linked to this user.");

        List<StoreResponse> res = allStores
                .stream()
                .map(this::mapToStoreResDto)
                .toList();

        return new ApiResponse<>(true, "Stores fetched successfully!!!", res);
    }

    public ApiResponse<StoreResponse> getStore(Long storeId) {
        Store store = repo.findById(storeId).orElseThrow(() -> new ResourceNotFoundException("Store not found!!!"));
        return new ApiResponse(true, "Store fetched successfully!!!", mapToStoreResDto(store));
    }

    public ApiResponse<StoreResponse> deleteStore(Long id) {
        String email = getCurrentUserEmail();
        Long userId = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found!!!")).getId();
        Store store = repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Store with " + id + " Not Found!!!"));
        if (!Objects.equals(store.getOwner().getId(), userId)) {
            throw new InvalidCredentialsException("Unauthorized User!!!");
        }
        repo.deleteById(id);
        return new ApiResponse<>(true, "Store Deleted Successfully!!!", mapToStoreResDto(store));
    }


    private StoreResponse mapToStoreResDto(Store s) {
        return StoreResponse.builder()
                .id(s.getId())
                .userName(s.getOwner().getName())
                .name(s.getName())
                .createdAt(s.getCreatedAt())
                .build();
    }

    private String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new RuntimeException("User not authenticated");
        }
        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails) {
            return ((UserDetails) principal).getUsername();
        }
        return principal.toString();
    }
}
