package io.alex.todoproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.alex.todoproject.models.Todo;
import io.alex.todoproject.models.CreateTodoRequest;
import io.alex.todoproject.models.TodoResponse;
import io.alex.todoproject.models.TodoUpdateRequest;
import io.alex.todoproject.service.TodoServiceImpl;
import io.alex.todoproject.todoRepository.TodoRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


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
        Todo expectedTodo = new Todo(id, "title", false, 1, "www.google.fr");

        Mockito.when(todoService.create(Mockito.any(CreateTodoRequest.class))).thenReturn(expectedTodo);
        String URICreateTodo = "/todos";

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URICreateTodo)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(expectedTodo));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("title"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(false))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("www.google.fr"));
    }

    @Test
    @DisplayName("Should return a list of Todo")
    void getAllTodos() throws Exception {
        String URIGetAll = ("/todos");
        final List<TodoResponse> todoResponses = List.of(TodoResponse.builder().id(UUID.randomUUID()).title("cuisine").rank(1).completed(false).url("www.google.fr").build(), TodoResponse.builder().id(UUID.randomUUID()).title("sale de bain").rank(2).completed(false).url("www.google.fr").build());
        Mockito.when(todoService.getAll()).thenReturn(todoResponses);

        mockMvc.perform(MockMvcRequestBuilders.get(URIGetAll))
                .andExpect(MockMvcResultMatchers.status()
                        .isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].title", CoreMatchers.is("cuisine")))
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should delete all todos")
    void deleteAllTodoIfNoQueryParameter() throws Exception {
        String URI = "/todos";
        UUID id = UUID.randomUUID();
        Todo expectedTodo = new Todo(id, "title", false, 1, "www.google.fr");

        Mockito.doReturn(expectedTodo).when(todoService).create(Mockito.any(CreateTodoRequest.class));

        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URI)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should delete todo depending on the query param completed")
    void deleteAllTodoIfQueryParameter() throws Exception {

        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/todos").param("completed", String.valueOf(false))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should delete todo by UUID")
    void deleteByUUID() throws Exception {
        UUID id = UUID.randomUUID();
        TodoResponse expectedTodo = new TodoResponse(id, "title", false, 1, "www.google.fr");

        Mockito.doReturn(Optional.of(expectedTodo)).when(todoService).getTodoById(expectedTodo.getId());

        mockMvc.perform(MockMvcRequestBuilders.delete("/todos/{id}", expectedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should get a todo with the id")
    void canGetTodoById() throws Exception {
        Todo expectedTodo = new Todo(UUID.randomUUID(), "title", false, 1, "www.google.fr");
        TodoResponse expectedTodoResponse = TodoResponse.builder().id(expectedTodo.getId()).completed(true).rank(1).url("www.google.fr").build();

        Mockito.when(todoService.getTodoById(expectedTodo.getId())).thenReturn(Optional.of(expectedTodoResponse));

        mockMvc.perform(MockMvcRequestBuilders.get("/todos/{id}", expectedTodo.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should return Not Found if can't found todo")
    void cantGetTodoById() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(todoService.getTodoById(id)).thenReturn(Optional.empty());

        mockMvc.perform(MockMvcRequestBuilders.get("/api/todo/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andDo(MockMvcResultHandlers.print());
    }

    @Test
    @DisplayName("Should update a Todo by id")
    void cantUpdateTodoById() throws Exception {
        TodoResponse todoResponse = TodoResponse.builder().id(UUID.randomUUID()).title("salon").completed(false).rank(1).url("www.google.fr").build();
        TodoUpdateRequest todo = TodoUpdateRequest.builder().title("salon").completed(false).rank(1).build();
        Todo todoUpdated = Todo.builder().title("chambre").completed(true).rank(1).build();

        Mockito.when(todoService.getTodoById(todoResponse.getId())).thenReturn(Optional.of(todoResponse));
        Mockito.when(todoService.updateTodoById(todoResponse.getId(), todo)).thenReturn(todoUpdated);

        RequestBuilder requestBuilder = MockMvcRequestBuilders.put("/todos/{id}", todoResponse.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(todoUpdated));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("chambre"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.completed").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.rank").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.url").value("www.google.fr"));
    }
}