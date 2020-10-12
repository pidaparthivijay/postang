/**
 * 
 */
package com.postang.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.TourPackage;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface TourPackageRepository extends JpaRepository<TourPackage, Long> {

	TourPackage findByTourPackageName(String tourPackageName);

}
