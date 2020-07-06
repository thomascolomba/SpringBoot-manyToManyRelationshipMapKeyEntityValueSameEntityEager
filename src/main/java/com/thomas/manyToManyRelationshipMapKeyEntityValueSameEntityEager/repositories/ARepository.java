package com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.domains.A;

public interface ARepository extends CrudRepository<A, Long> {
	public List<A> findByA(String a);
}