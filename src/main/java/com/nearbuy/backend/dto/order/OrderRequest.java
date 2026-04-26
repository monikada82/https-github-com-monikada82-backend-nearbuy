package com.nearbuy.backend.dto.order;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderRequest {

    @NotNull(message = "Address ID is required")
    private Long addressId;
}
