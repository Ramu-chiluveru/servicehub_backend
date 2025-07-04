package com.servicehub.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.servicehub.dto.LocationRequest;
import com.servicehub.service.LocationService;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/user")
public class LocationsController 
{

    @Autowired
    private LocationService locationService;

    @PostMapping("/location")
    public ResponseEntity<String> saveLocation(@RequestBody LocationRequest request) 
    {
        locationService.saveUserLocation(request.getUserId(), request.getLatitude(), request.getLongitude());
      return ResponseEntity.ok("Location saved");
    }

  
}
