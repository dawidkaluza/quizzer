package org.quizzer.category.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
public class Category {
    @Setter(AccessLevel.PROTECTED)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 192)
    private String name;

    @Column(length = 512)
    private String description;
}
