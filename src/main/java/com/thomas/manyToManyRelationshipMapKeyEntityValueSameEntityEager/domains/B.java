package com.thomas.manyToManyRelationshipMapKeyEntityValueSameEntityEager.domains;

import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "B")
@NoArgsConstructor
@Setter @Getter
@EqualsAndHashCode
public class B implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String b;

    public B(String b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "B{" +
                "id=" + id +
                ", b=" + b +
                '}';
    }
}