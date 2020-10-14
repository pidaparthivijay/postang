package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Vehicle;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	List<Vehicle> findByLocation(String location);

	Vehicle findByRegNum(String regNum);

}
