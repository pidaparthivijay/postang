package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Driver;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

	List<Driver> findByLocation(String location);

	Driver findByDriverLicense(String license);

}
