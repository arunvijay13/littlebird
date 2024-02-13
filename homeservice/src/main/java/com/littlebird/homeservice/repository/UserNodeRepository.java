package com.littlebird.homeservice.repository;

import com.littlebird.homeservice.constants.Neo4jConstants;
import com.littlebird.homeservice.dao.Person;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface UserNodeRepository extends Neo4jRepository<Person, String> {


    @Modifying
    @Query("MATCH (a)-[r:"+ Neo4jConstants.RELATIONSHIP_TYPE_FOLLOWS + "]->(b) " +
            "WHERE a.username = '${userA}' AND b.username = '${userB}' " +
            "DELETE r")
    void removeRelationship(@Param("userA") String userA, @Param("userB") String userB);
}
