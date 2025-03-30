package com.example.JSONprocessing.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.JSONprocessing.entities.Location;
import com.example.JSONprocessing.entities.MergedLocation;
import com.example.JSONprocessing.service.LocationService;

@RestController
@RequestMapping("/locations")
public class LocationController {

    private final LocationService locationService;

    public LocationController(LocationService locationService) {
        this.locationService = locationService;
    }

    @GetMapping("/count-by-type")
    public Map<String, Long> getCountByType() {
        return locationService.countByType();
    }

    @GetMapping("/average-rating")
    public Map<String, Double> getAverageRatingByType() {
        return locationService.averageRatingByType();
    }

    @GetMapping("/highest-reviews")
    public MergedLocation getHighestReviewedLocation() {
        return locationService.highestReviews();
    }
    
    @GetMapping("/incomplete-data")
    public List<Location> getIncompleteData() {
        return locationService.findIncompleteData();
    }
}
