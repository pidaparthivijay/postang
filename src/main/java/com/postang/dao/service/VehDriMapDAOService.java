/**
 * 
 */
package com.postang.dao.service;

import com.postang.domain.TourPackageRequest;
import com.postang.domain.VehicleDriverMapping;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface VehDriMapDAOService {

	VehicleDriverMapping saveMapping(VehicleDriverMapping vehicleDriverMapping);

	VehicleDriverMapping getMappingByTourPackageRequestId(TourPackageRequest tourPackageRequest);

}
