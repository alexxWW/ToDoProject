package io.alex.todoproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
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
        UUID id = UUID.randomUUID();
        Todo expectedTodo = new Todo(id, "title", false, 1, "http://localhost/todos/" + id);
        CreateTodoRequest createTodo = CreateTodoRequest.builder().title("title").build();
        String URICreateTodo = "/todos";

        when(todoService.create(any(CreateTodoRequest.class))).thenReturn(expectedTodo);

        mockMvc.perform(post(URICreateTodo)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createTodo)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedTodo), true))
                .andDo(print());

        verify(todoService).create(any(CreateTodoRequest.class));
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should return a list of Todo")
    void getAllTodos() throws Exception {
        String URIGetAll = ("/todos");
        final List<Todo> todoResponses = List.of(Todo.builder().id(UUID.randomUUID()).title("cuisine").rank(1).completed(false).url("www.google.fr").build(), Todo.builder().id(UUID.randomUUID()).title("sale de bain").rank(2).completed(false).url("www.google.fr").build());

        when(todoService.getAll()).thenReturn(todoResponses);

        mockMvc.perform(get(URIGetAll))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponses)))
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
        UUID id = UUID.randomUUID();
        Todo expectedTodo = Todo.builder().id(id).title("title").completed(false).rank(1).url("www.google.fr").build();

        when(todoService.getTodoById(expectedTodo.getId())).thenReturn(Optional.of(expectedTodo));
        doNothing().when(todoService).deleteTodoById(expectedTodo.getId());

        mockMvc.perform(delete("/todos/{id}", expectedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(print());

        verify(todoService).getTodoById(expectedTodo.getId());
        verify(todoService).deleteTodoById(expectedTodo.getId());
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should get a todo with the id")
    void canGetTodoById() throws Exception {
        Todo expectedTodo = new Todo(UUID.randomUUID(), "title", false, 1, "www.google.fr");
        Todo expectedTodoResponse = Todo.builder().id(expectedTodo.getId()).completed(true).rank(1).url("www.google.fr").build();

        when(todoService.getTodoById(expectedTodo.getId())).thenReturn(Optional.of(expectedTodoResponse));

        mockMvc.perform(get("/todos/{id}", expectedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print());

        verify(todoService, times(2)).getTodoById(expectedTodo.getId());
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
        Todo getTodo = Todo.builder().id(UUID.randomUUID()).title("salon").completed(false).rank(1).url("www.google.fr").build();
        TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder().title("chambre").completed(false).rank(2).build();
        Todo todoUpdated = Todo.builder().id(getTodo.getId()).title(todoUpdateRequest.getTitle()).completed(todoUpdateRequest.isCompleted()).rank(todoUpdateRequest.getRank()).url(getTodo.getUrl()).build();
        TodoResponse todoResponse = TodoResponse.builder().id(todoUpdated.getId()).title(todoUpdated.getTitle()).completed(todoUpdated.isCompleted()).rank(todoUpdated.getRank()).url(todoUpdated.getUrl()).build();

        when(todoService.getAll()).thenReturn(List.of(getTodo));
        when(todoService.updateByUUID(getTodo.getId(), todoUpdateRequest)).thenReturn(todoUpdated);

        mockMvc.perform(put("/todos/{id}", getTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().json(objectMapper.writeValueAsString(todoResponse), true));

        verify(todoService).getAll();
        verify(todoService).updateByUUID(getTodo.getId(), todoUpdateRequest);
        verifyNoMoreInteractions(todoService);
    }

    @Test
    @DisplayName("Should return a 409 conflict error if existing todo with existing rank")
    void cantUpdateTodoAndReturn409() throws Exception {
        Todo getTodo = Todo.builder().id(UUID.randomUUID()).title("salon").completed(false).rank(1).url("www.google.fr").build();
        List<Todo> todoList = List.of(Todo.builder().id(UUID.randomUUID()).title("salon").completed(false).rank(2).url("www.google.fr").build(), Todo.builder().id(UUID.randomUUID()).title("salon").completed(false).rank(1).url("www.google.fr").build());
        TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder().title("chambre").completed(false).rank(getTodo.getRank()).build();
        Todo todoUpdated = Todo.builder().id(getTodo.getId()).title(todoUpdateRequest.getTitle()).completed(todoUpdateRequest.isCompleted()).rank(todoUpdateRequest.getRank()).url(getTodo.getUrl()).build();
        TodoResponse todoResponse = TodoResponse.builder().id(todoUpdated.getId()).title(todoUpdated.getTitle()).completed(todoUpdated.isCompleted()).rank(todoUpdated.getRank()).url(todoUpdated.getUrl()).build();

        when(todoService.getAll()).thenReturn(todoList);
        when(todoService.getTodoById(getTodo.getId())).thenReturn(Optional.of(getTodo));
        when(todoService.updateByUUID(any(), any())).thenReturn(todoUpdated);

        mockMvc.perform(put("/todos/{id}", getTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateRequest)))
                .andExpect(status().isConflict())
                .andDo(print());

//        verify(todoService).getTodoById(todo.getId());
//        verify(todoService).updateByUUID(todo.getId(), todoUpdateRequest);
//        verifyNoInteractions(todoService);
    }

    @Test
    @DisplayName("Should return 404 not found if todo isn't exist")
    void cantUpdateTodoIfNotExist() throws Exception {
        TodoUpdateRequest todoUpdateRequest = TodoUpdateRequest.builder().title("chambre").completed(true).rank(1).build();
        Todo todo = Todo.builder().id(UUID.randomUUID()).title("chambre").completed(true).rank(1).build();

        when(todoService.getTodoById(todo.getId())).thenReturn(Optional.empty());

        mockMvc.perform(put("/todos/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(todoUpdateRequest)))
                .andExpect(status().isNotFound())
                .andDo(print());

//        verify(todoService).getTodoById(todo.getId());
//        verify(todoService).updateByUUID(todo.getId(), todoUpdateRequest);
//        verifyNoInteractions(todoService);
    }
}