package com.postang.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Driver;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface DriverRepository extends JpaRepository<Driver, Long> {

}
