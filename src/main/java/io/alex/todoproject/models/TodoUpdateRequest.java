package io.alex.todoproject.models;

import lombok.Builder;

import java.util.Objects;

@Builder
public class TodoUpdateRequest {

    private String title;
    private boolean completed;
    private int order;

    public TodoUpdateRequest(String title, boolean completed, int order) {
        this.title = title;
        this.completed = completed;
        this.order = order;
    }

    public TodoUpdateRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int rank) {
        this.order = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoUpdateRequest that = (TodoUpdateRequest) o;
        return completed == that.completed && order == that.order && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, completed, order);
    }
}
