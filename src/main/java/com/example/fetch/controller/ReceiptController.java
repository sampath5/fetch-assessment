package com.example.fetch.controller;

import com.example.fetch.dto.PointsResponse;
import com.example.fetch.dto.Receipt;
import com.example.fetch.dto.ReceiptResponse;
import com.example.fetch.service.ReceiptService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/receipts")
public class ReceiptController {

    @Autowired
    private ReceiptService receiptService;

    @PostMapping("/process")
    public ResponseEntity<ReceiptResponse> processReceipt(@Valid @RequestBody Receipt receiptRequest) {
        String id = UUID.randomUUID().toString(); // Generate a unique ID
        int points = receiptService.calculatePoints(receiptRequest); // Calculate points based on rules
        receiptService.storePoints(id, points); // Store only the ID and points in H2

        ReceiptResponse response = new ReceiptResponse(id);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/points")
    public ResponseEntity<PointsResponse> getPoints(@PathVariable String id) {
        int points = receiptService.getPoints(id);

        PointsResponse response = new PointsResponse(points);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
