package io.alex.todoproject.models;

import lombok.Builder;

import java.util.Objects;

@Builder
public class CreateTodoRequest {

    private String title;

    public String getTitle() {
        return title;
    }

    public CreateTodoRequest(String title) {
        this.title = title;
    }

    public CreateTodoRequest(){}

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CreateTodoRequest todoDTO = (CreateTodoRequest) o;
        return title.equals(todoDTO.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
