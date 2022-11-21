package io.alex.todoproject.models;

import com.sun.istack.NotNull;
import lombok.Builder;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.UUID;

@Entity(name = "TODO")
@Builder
public class TodoEntity {

    @Id
    @NotNull
    @GeneratedValue(generator="uuid")
    @GenericGenerator(name="uuid", strategy = "uuid")
    private String id;

    @NotNull
    @NotBlank
    private String title;
    private boolean completed;

    @Column(name = "rank")
    private Integer order;
    private String url;

    public TodoEntity(String title, boolean completed, Integer order, String url) {
        this.title = title;
        this.completed = completed;
        this.order = order;
        this.url = url;
    }

    public TodoEntity(String id, String title, boolean completed, Integer order, String url) {
        this.id=id;
        this.title=title;
        this.completed=completed;
        this.order=order;
        this.url=url;
    }

    public TodoEntity(String title, Integer order) {
        this.title = title;
        this.order = order;
    }

    public TodoEntity() {

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

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(int order) {
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
        TodoEntity todo = (TodoEntity) o;
        return isCompleted() == todo.isCompleted() && getOrder() == todo.getOrder() && Objects.equals(getId(), todo.getId()) && Objects.equals(getTitle(), todo.getTitle()) && Objects.equals(getUrl(), todo.getUrl());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), isCompleted(), getOrder(), getUrl());
    }

}
