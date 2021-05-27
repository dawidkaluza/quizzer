package org.quizzer.question.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    private String content;

    private boolean correct;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    private Question question;
}
