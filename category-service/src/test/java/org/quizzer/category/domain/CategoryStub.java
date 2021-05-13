package org.quizzer.category.domain;

import lombok.*;
import org.springframework.lang.Nullable;

@Setter
@Getter
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CategoryStub extends Category {
    @Setter(AccessLevel.PROTECTED)
    private Long id;

    private String name;

    private String description;

    public static Category of(@Nullable Long id, String name, String description) {
        return new CategoryStub(id, name, description);
    }
}
