package com.seowon.coding.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class CreateOrderRequest {

    @NotBlank
    private String customerName;

    @Email
    private String customerEmail;

    private List<Products> products;

    @Builder
    public CreateOrderRequest(String customerName, String customerEmail, List<Products> products) {
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.products = products;
    }

    @NoArgsConstructor
    @Getter
    @Setter
    public static class Products {
        @NotNull
        private Long productId;

        @NotNull
        private int quantity;

        @Builder
        public Products(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }
    }
}
