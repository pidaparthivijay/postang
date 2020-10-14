package com.postang.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.postang.domain.Vehicle;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

	List<Vehicle> findByLocation(String location);

	Vehicle findByRegNum(String regNum);

	@Query("select vh from Vehicle vh where vh.vehicleType=:vehicleType and vh.location=:location and vh.regNum not in (select vdm.vehicleRegNum from VehicleDriverMapping vdm where vdm.assignedDate between :startDate and :endDate)")
	List<Vehicle> findSimilar(@Param("vehicleType") String vehicleType, @Param("location") String location,
			@Param("startDate") Date startDate, @Param("endDate") Date endDate);

}
