package com.example.fetch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class Item {
    @NotNull(message = "Item description is required")
    @Pattern(regexp = "^[\\w\\s\\-]+$", message = "Item description contains invalid characters")
    private String shortDescription;

    @NotNull(message = "Item price is required")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Price must have two decimal places")
    private String price;
}

