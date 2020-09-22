package com.postang.repo;

import org.springframework.data.repository.CrudRepository;

import com.postang.model.Lookup;

public interface LookupRepository extends CrudRepository<Lookup, Long> {

	Lookup findByLookupId(long lookupId);

	Iterable<Lookup> findByLookupDefName(String lookupDefName);

}
