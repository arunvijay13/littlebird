package com.littlebird.userservice.repository;

import com.littlebird.userservice.dao.Person;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonNodeRepository extends Neo4jRepository<Person, String> {
}
