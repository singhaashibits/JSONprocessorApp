package com.example.JSONprocessing.service;

import com.example.JSONprocessing.entities.Location;
import com.example.JSONprocessing.entities.MergedLocation;
import com.example.JSONprocessing.entities.Metadata;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LocationService {

    private final List<MergedLocation> mergedLocations = new ArrayList<>();
    private List<Location> locations = new ArrayList<>();
    private List<Metadata> metadata = new ArrayList<>();

    @PostConstruct
    public void loadData() throws IOException {
        locations = loadJson("location.json", new TypeReference<>() {});
        metadata = loadJson("metadata.json", new TypeReference<>() {});

        Map<String, Metadata> metadataMap = metadata.stream()
                .collect(Collectors.toMap(Metadata::getId, meta -> meta));

        mergedLocations.addAll(locations.stream()
                .filter(loc -> metadataMap.containsKey(loc.getId()))
                .map(loc -> new MergedLocation(loc.getId(), loc.getLatitude(), loc.getLongitude(),
                        metadataMap.get(loc.getId()).getType(),
                        metadataMap.get(loc.getId()).getRating(),
                        metadataMap.get(loc.getId()).getReviews()))
                .collect(Collectors.toList()));
    }

    private <T> List<T> loadJson(String fileName, TypeReference<List<T>> typeRef) throws IOException {
        InputStream stream = getClass().getClassLoader().getResourceAsStream(fileName);
        if (stream == null) throw new IOException(fileName + " not found in resources folder.");
        return new ObjectMapper().readValue(stream, typeRef);
    }

    public Map<String, Long> countByType() {
        return mergedLocations.stream()
                .collect(Collectors.groupingBy(MergedLocation::getType, Collectors.counting()));
    }

    public Map<String, Double> averageRatingByType() {
        return mergedLocations.stream()
                .collect(Collectors.groupingBy(MergedLocation::getType,
                        Collectors.averagingDouble(MergedLocation::getRating)));
    }

    public MergedLocation highestReviews() {
        return mergedLocations.stream()
                .max(Comparator.comparingInt(MergedLocation::getReviews))
                .orElse(null);
    }

    public List<Location> findIncompleteData() {
        Set<String> metadataIds = metadata.stream().map(Metadata::getId).collect(Collectors.toSet());

        return locations.stream()
                .filter(loc -> loc.getId() == null || loc.getLatitude() == 0 || loc.getLongitude() == 0 || !metadataIds.contains(loc.getId()))
                .collect(Collectors.toList());
    }
}
