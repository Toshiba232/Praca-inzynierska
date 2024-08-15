package com.example.yukka.model.social;

import java.util.List;

import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Oceniany {
    @Property(name = "ocenyLubi")
    private Integer ocenyLubi;

    @Property(name = "ocenyNieLubi")
    private Integer ocenyNieLubi;

    @Relationship(type = "OCENIL", direction = Relationship.Direction.INCOMING)
    private List<Ocenil> ocenil;
}
