package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Vehicle;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleRepository extends CrudRepository<Vehicle, Long> {

}
