package com.nearbuy.backend.controller;

import com.nearbuy.backend.dto.address.AddressRequest;
import com.nearbuy.backend.dto.address.AddressResponse;
import com.nearbuy.backend.service.AddressService;
import com.nearbuy.backend.service.CurrentUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/address")
@RequiredArgsConstructor
@Tag(name = "Addresses", description = "Logged-in user's delivery address APIs")
@SecurityRequirement(name = "bearerAuth")
public class AddressController {

    private final AddressService addressService;
    private final CurrentUserService currentUserService;

    @PostMapping
    @Operation(summary = "Add address for current user")
    public AddressResponse addAddress(
            @Valid @RequestBody AddressRequest request) {

        Long userId = currentUserService.getCurrentUserId();
        return addressService.addAddress(userId, request);
    }

    @GetMapping
    @Operation(summary = "Get current user's addresses")
    public List<AddressResponse> getAddresses() {
        Long userId = currentUserService.getCurrentUserId();
        return addressService.getUserAddresses(userId);
    }
}
