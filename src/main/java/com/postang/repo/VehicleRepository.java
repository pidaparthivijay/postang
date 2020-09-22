package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Vehicle;

public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

}
