package se.comhem.quantum.integration.geocode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static java.util.Arrays.asList;

@Service
public class GeoCodeService {

    public static final String GEO_CODE_ADDRESS_SEARCH_API = "http://maps.googleapis.com/maps/api/geocode/json?address={searchTerm}&sensor=false";
    private final RestTemplate restTemplate;

    @Autowired
    public GeoCodeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Double> getGeoLocation(String searchTerm) {
        if (searchTerm.isEmpty()) {
            return Collections.emptyList();
        } else {
            return restTemplate.getForObject(GEO_CODE_ADDRESS_SEARCH_API, PlaceLocation.class, searchTerm)
                .getLocation()
                .map(location -> asList(location.getLat(), location.getLng()))
                .orElseGet(Collections::emptyList);
        }
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class PlaceLocation {

        private List<Result> results = new ArrayList<>();

        Optional<Location> getLocation() {
            return results.stream()
                .findFirst()
                .map(Result::getGeometry)
                .map(Geometry::getLocation);
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        private static class Result {
            private Geometry geometry;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        private static class Geometry {
            private Location location;
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        private static class Location {
            private Double lat;
            private Double lng;
        }
    }
}
