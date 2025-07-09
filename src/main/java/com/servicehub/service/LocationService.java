package com.servicehub.service;

import com.servicehub.model.Locations;
import com.servicehub.model.User;
import com.servicehub.repository.LocationsRepository;
import com.servicehub.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LocationsRepository locationsRepository;

    public void saveUserLocation(Long userId, double lat, double lng) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        GeometryFactory geometryFactory = new GeometryFactory();
        Point point = geometryFactory.createPoint(new Coordinate(lng, lat)); // Note: (longitude, latitude)
        point.setSRID(4326); // For PostGIS compatibility

        Locations location = Locations.builder()
                .user(user)
                .location(point)
                .build();

        locationsRepository.save(location);
    }
}
