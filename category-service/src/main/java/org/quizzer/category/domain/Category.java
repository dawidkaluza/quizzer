package org.quizzer.category.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
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
