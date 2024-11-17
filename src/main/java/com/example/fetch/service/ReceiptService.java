package com.example.fetch.service;

import com.example.fetch.dto.Item;
import com.example.fetch.dto.Receipt;
import com.example.fetch.exception.ReceiptNotFoundException;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
public class ReceiptService {

    // In-memory map to store ID and points
    private final Map<String, Integer> pointsStore = new HashMap<>();

    // Method to store points for an ID
    public void storePoints(String id, int points) {
        pointsStore.put(id, points);
    }

    // Method to retrieve points for an ID
    public int getPoints(String id) {
        if (!pointsStore.containsKey(id)) {
            throw new ReceiptNotFoundException("Receipt not found");
        }
        return pointsStore.get(id);
    }

    // Method to calculate points based on rules
    public int calculatePoints(Receipt receiptRequest) {
        int points = 0;

        points += calculateRetailerPoints(receiptRequest.getRetailer());
        points += calculateTotalPoints(receiptRequest.getTotal());
        points += calculateItemPoints(receiptRequest.getItems());
        points += calculateOddDayPoints(receiptRequest.getPurchaseDate());
        points += calculateTimePoints(receiptRequest.getPurchaseTime());

        log.debug("Total calculated points: {}", points);
        return points;
    }

    // 1. Calculate points for alphanumeric characters in retailer name
    private int calculateRetailerPoints(String retailer) {
        int points = retailer.replaceAll("[^a-zA-Z0-9]", "").length();
        log.debug("Retailer points: {}", points);
        return points;
    }

    // 2. Calculate points based on total amount
    private int calculateTotalPoints(String totalStr) {
        int points = 0;
        double total = Double.parseDouble(totalStr);

        if (total % 1 == 0) {
            points += 50;
            log.debug("Added 50 points for round dollar amount");
        }
        if (total % 0.25 == 0) {
            points += 25;
            log.debug("Added 25 points for multiple of 0.25");
        }

        return points;
    }

    // 3. Calculate points for each item
    private int calculateItemPoints(@NotNull(message = "Items are required") @Size(min = 1, message = "At least one item is required") List<Item> items) {
        int points = 0;

        // 5 points for every two items
        points += (items.size() / 2) * 5;
        log.debug("Points for item pairs: {}", points);

        // Points based on item description length
        for (Item item : items) {
            String description = item.getShortDescription().trim();
            if (description.length() % 3 == 0) {
                double price = Double.parseDouble(item.getPrice());
                points += (int) Math.ceil(price * 0.2);
                log.debug("Added points for item '{}' with price {}", description, price);
            }
        }

        return points;
    }

    // 4. Calculate points if the day of the purchase date is odd
    private int calculateOddDayPoints(String purchaseDate) {
        int day = Integer.parseInt(purchaseDate.split("-")[2]);
        if (day % 2 != 0) {
            log.debug("Added 6 points for odd day of purchase");
            return 6;
        }
        return 0;
    }

    // 5. Calculate points for purchase time between 2:00 PM and 4:00 PM
    private int calculateTimePoints(String purchaseTime) {
        int hour = Integer.parseInt(purchaseTime.split(":")[0]);
        int minute = Integer.parseInt(purchaseTime.split(":")[1]);

        if (hour == 14 || (hour == 15 && minute < 60)) {
            log.debug("Added 10 points for purchase time between 2:00 PM and 4:00 PM");
            return 10;
        }
        return 0;
    }
}
