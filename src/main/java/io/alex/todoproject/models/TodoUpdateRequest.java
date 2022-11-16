package io.alex.todoproject.models;

import lombok.Builder;

import java.util.Objects;

@Builder
public class TodoUpdateRequest {

    private String title;
    private boolean completed;
    private int rank;

    public TodoUpdateRequest(String title, boolean completed, int rank) {
        this.title = title;
        this.completed = completed;
        this.rank = rank;
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

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoUpdateRequest that = (TodoUpdateRequest) o;
        return completed == that.completed && rank == that.rank && Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, completed, rank);
    }
}
