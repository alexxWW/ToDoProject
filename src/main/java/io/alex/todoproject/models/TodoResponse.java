package io.alex.todoproject.models;

import lombok.Builder;

import java.util.Objects;
import java.util.UUID;

@Builder
public class TodoResponse {
    private String id;
    private String title;
    private boolean completed;
    private Integer order;
    private String url;

    public TodoResponse(String id, String title, Boolean completed, Integer order, String url) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.url = url;
    }

    public TodoResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
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
        return order == that.order && Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(completed, that.completed) && Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, completed, order, url);
    }
}
