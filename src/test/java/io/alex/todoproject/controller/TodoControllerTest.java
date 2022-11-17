package io.alex.todoproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.alex.todoproject.exceptions.TodoConflictException;
import io.alex.todoproject.exceptions.TodoNotFoundException;
import io.alex.todoproject.models.*;
import io.alex.todoproject.service.TodoServiceImpl;
import io.alex.todoproject.todoRepository.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static io.alex.todoproject.fakeObject.fakeTodoObjects.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.*;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepository;

    @MockBean
    private TodoServiceImpl todoService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    @DisplayName("Should create a todo and return 201 created")
    void create() throws Exception {
        String URICreateTodo = "/todos";

        when(todoService.create(any())).thenReturn(todo);

        mockMvc.perform(post(URICreateTodo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTodoRequest)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(todo), true))
                .andDo(print());

        verify(todoService).create(any());
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should return a list of Todo")
    void getAllTodos() throws Exception {
        String URIGetAll = ("/todos");

        when(todoService.getAll()).thenReturn(todoList);

        mockMvc.perform(get(URIGetAll))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todoList)))
                .andDo(print());
        verify(todoService).getAll();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should delete all todos")
    void deleteAllTodoIfNoQueryParameter() throws Exception {
        String URI = "/todos";

        doNothing().when(todoService).deleteAllTodo();

        mockMvc.perform(delete(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());

        verify(todoService).deleteAllTodo();
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should delete todo depending on the query param completed")
    void deleteAllTodoIfQueryParameter() throws Exception {

        doNothing().when(todoService).deleteTodoByCompleted(false);

        mockMvc.perform(delete("/todos").param("completed", String.valueOf(false))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());
    }

    @Test
    @DisplayName("Should delete todo by UUID")
    void deleteByUUID() throws Exception {

        when(todoService.getTodoById(todo.getId())).thenReturn(Optional.of(todo));
        doNothing().when(todoService).deleteTodoById(todo.getId());

        mockMvc.perform(delete("/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());

        verify(todoService).getTodoById(todo.getId());
        verify(todoService).deleteTodoById(todo.getId());
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should get a todo with the id")
    void canGetTodoById() throws Exception {

        when(todoService.getTodoById(any())).thenReturn(Optional.of(todo));

        mockMvc.perform(get("/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(todoService, times(2)).getTodoById(todo.getId());
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should return Not Found if can't found todo")
    void cantGetTodoById() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/todo/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(print());

        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should update a Todo by id")
    void updateTodoById() throws Exception {

        when(todoService.getAll()).thenReturn(List.of(todo));
        when(todoService.updateByUUID(todo.getId(), todoUpdateRequest)).thenReturn(todo);

        mockMvc.perform(put("/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponse), true));

        verify(todoService).updateByUUID(todo.getId(), todoUpdateRequest);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should return a 409 conflict error if existing todo with existing rank")
    void cantUpdateTodoAndReturn409() throws Exception {

        doThrow(TodoConflictException.class).when(todoService).updateByUUID(todo.getId(), todoUpdateRequest);

        mockMvc.perform(put("/todos/{id}", todo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateRequest)))
                .andExpect(status().isConflict())
                .andDo(print());

        verify(todoService).updateByUUID(todo.getId(), todoUpdateRequest);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should return 404 not found if todo isn't exist")
    void cantUpdateTodoIfNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        doThrow(TodoNotFoundException.class).when(todoService).updateByUUID(id, todoUpdateRequest);

        mockMvc.perform(put("/todos/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());

        verify(todoService).updateByUUID(id, todoUpdateRequest);
        verifyNoMoreInteractions(todoService);
    }
}