package com.vip.gardenapp.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.vip.gardenapp.dto.CurrentLocation;
import com.vip.gardenapp.dto.Locations;


@Repository
public interface CurrentLocationRepository extends JpaRepository<CurrentLocation, Integer> {

	
}