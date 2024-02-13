package com.littlebird.userservice.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Version;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Node
public class Person {

    @Id
    private String username;

    private String email;

    @Version
    private Long id;

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.OUTGOING)
    private Set<Person> following = new HashSet<>();

    @Relationship(type = "FOLLOWS", direction = Relationship.Direction.INCOMING)
    private Set<Person> followers = new HashSet<>();

    public Person(String username, String email) {
        this.username = username;
        this.email = email;
    }
}
