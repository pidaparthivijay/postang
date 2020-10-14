package com.postang.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.postang.domain.Driver;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

	List<Driver> findByLocation(String location);

	Driver findByDriverLicense(String license);

	@Query("select dr from Driver dr where dr.location=:location and dr.driverLicense not in (select vdm.driverLicense from VehicleDriverMapping vdm where vdm.assignedDate between :startDate and :endDate)")
	List<Driver> findSimilar(String location, Date startDate, Date endDate);

}
