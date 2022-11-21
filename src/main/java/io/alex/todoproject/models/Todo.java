package io.alex.todoproject.models;

import lombok.Builder;

import java.util.Objects;

@Builder
public class Todo {

    private String id;
    private String title;
    private boolean completed;
    private Integer order;
    private String url;

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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer rank) {
        this.order = rank;
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
        Todo todo = (Todo) o;
        return completed == todo.completed && order == todo.order && Objects.equals(id, todo.id) && Objects.equals(title, todo.title) && Objects.equals(url, todo.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, completed, order, url);
    }
}
