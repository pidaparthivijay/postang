package com.postang.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.VehicleDriverMapping;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehicleDriverMappingRepository extends JpaRepository<VehicleDriverMapping, Long> {

	VehicleDriverMapping findByTourPackageRequestId(long tourPackageRequestId);

}
