/**
 * 
 */
package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.AmenityRequest;


/**
 * @author Subrahmanya Vijay
 *
 */
public interface AmenityRequestRepository extends JpaRepository<AmenityRequest, Long> {

	List<AmenityRequest> findByUserName(String userName);

}
