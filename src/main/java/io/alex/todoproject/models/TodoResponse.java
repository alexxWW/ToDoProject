package io.alex.todoproject.models;

import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

@Builder
public class TodoResponse {

    private UUID id;
    private String title;
    private boolean completed;
    private int rank;
    private String url;

    public TodoResponse(UUID id, String title, Boolean completed, int rank, String url) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.rank = rank;
        this.url = url;
    }

    public TodoResponse() {
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
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

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoResponse that = (TodoResponse) o;
        return rank == that.rank && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(completed, that.completed) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, completed, rank, url);
    }
}