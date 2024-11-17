package com.example.fetch.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.util.List;

@Data
public class Receipt {
    @NotNull(message = "Retailer is required")
    @Pattern(regexp = "^[\\w\\s\\-&]+$", message = "Retailer name contains invalid characters")
    private String retailer;

    @NotNull(message = "Purchase date is required")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Purchase date must be in the format YYYY-MM-DD")
    private String purchaseDate;

    @NotNull(message = "Purchase time is required")
    @Pattern(regexp = "^\\d{2}:\\d{2}$", message = "Purchase time must be in the 24-hour format HH:mm")
    private String purchaseTime;

    @NotNull(message = "Total is required")
    @Pattern(regexp = "^\\d+\\.\\d{2}$", message = "Total must have two decimal places")
    private String total;

    @NotNull(message = "Items are required")
    @Size(min = 1, message = "At least one item is required")
    private List<Item> items;

}
