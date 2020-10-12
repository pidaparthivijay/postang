package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.OneTimePassword;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface OneTimePassRepo extends JpaRepository<OneTimePassword, Long> {
	public List<OneTimePassword> findByUserName(String userName);

}
