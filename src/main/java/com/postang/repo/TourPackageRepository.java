/**
 * 
 */
package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.TourPackage;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageRepository extends CrudRepository<TourPackage, Long> {

	TourPackage findByTourPackageName(String tourPackageName);

}
