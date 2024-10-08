package com.example.yukka.model.roslina.relationshipnodes;

import java.util.List;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import com.example.yukka.model.roslina.Roslina;
@Node
public class Gleba {
    @Id @GeneratedValue
    private Long id;
    @Property("nazwa")
    private String nazwa;

    @Relationship(type="ma_rosline", direction=Relationship.Direction.OUTGOING)
    private List<Roslina> plants;

    public Gleba() {
    }

    public List<Roslina> getRosliny() {
        return plants;
    }

    public Long getId() {
        return id;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String name) {
        this.nazwa = name;
    }

}
