package com.postang.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Vehicle;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

}
