package io.alex.todoproject.models;

import com.sun.istack.NotNull;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "TODO")
@Builder
public class Todo {

    @Id
    @NotNull
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private UUID id;

    private String title;
    private boolean completed;
    private int rank;
    private String url;

    public Todo(String title, boolean completed, int rank, String url) {
        this.title = title;
        this.completed = completed;
        this.rank = rank;
        this.url = url;
    }

    public Todo(UUID id, String title, boolean completed, int rank, String url) {
        this.id=id;
        this.title=title;
        this.completed=completed;
        this.rank=rank;
        this.url=url;
    }

    public Todo() {

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

    public void setCompleted(boolean completed) {
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
        Todo todo = (Todo) o;
        return isCompleted() == todo.isCompleted() && getRank() == todo.getRank() && Objects.equals(getId(), todo.getId()) && Objects.equals(getTitle(), todo.getTitle()) && Objects.equals(getUrl(), todo.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), isCompleted(), getRank(), getUrl());
    }

}
