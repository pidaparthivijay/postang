package com.postang.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.postang.domain.Lookup;

/**
 * @author Subrahmanya Vijay
 *
 */
public interface LookupRepository extends JpaRepository<Lookup, Long> {

	Lookup findByLookupId(long lookupId);

	List<Lookup> findByLookupDefName(String lookupDefName);

}
