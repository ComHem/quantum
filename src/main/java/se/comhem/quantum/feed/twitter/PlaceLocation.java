package se.comhem.quantum.feed.twitter;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PlaceLocation {

    private List<Result> results = new ArrayList<>();

    public Optional<Location> getLocation() {
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
    public static class Location {
        private Double lat;
        private Double lng;
    }
}