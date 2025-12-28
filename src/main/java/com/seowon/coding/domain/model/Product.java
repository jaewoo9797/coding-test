package com.seowon.coding.domain.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    private String name;

    private String description;

    @Positive(message = "Price must be positive")
    private BigDecimal price;

    private int stockQuantity;

    private String category;

    // Business logic
    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public void decreaseStock(int quantity) {
        if (quantity > stockQuantity) {
            throw new IllegalArgumentException("Not enough stock available");
        }
        stockQuantity -= quantity;
    }

    public void increaseStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        stockQuantity += quantity;
    }

    public void changePrice(double percentage, boolean includeTax) {
        // percentage → 비율(BigDecimal)
        BigDecimal rate = BigDecimal.valueOf(percentage)
                .divide(BigDecimal.valueOf(100), 10, RoundingMode.HALF_UP);

        // 가격 증가 적용
        BigDecimal changed = this.price.multiply(BigDecimal.ONE.add(rate));

        // 세금 포함
        if (includeTax) {
            changed = changed.multiply(new BigDecimal("1.10")); //VAT 10%, 지역/카테고리별 규칙 미반영
        }

        // 반올림 규칙 적용
        BigDecimal newPrice = changed.setScale(2, RoundingMode.HALF_UP);

        setPrice(newPrice);
    }
}