package io.alex.todoproject.controller;

import io.alex.todoproject.Todo;
import io.alex.todoproject.service.TodoServiceImpl;
import io.alex.todoproject.todoInterface.TodoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(TodoController.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TodoRepository todoRepository;

    @MockBean
    private TodoServiceImpl todoService;

    @Test
    @DisplayName("Should return bad request not found if wrong given url")
    void cantGetAllTodosBecauseOfBadRequest() throws Exception {
        String uri=("/all");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isNotFound());

    }

    @Test
    @DisplayName("Should return a list of Todo")
    void getAllTodos() throws Exception {
        String uri=("/api/todo/all");
        mockMvc.perform(MockMvcRequestBuilders.get(uri)).andExpect(MockMvcResultMatchers.status().isOk());
        final List<Todo> todos = List.of(new Todo("1","maison", false, 3, "www.google.fr"));

        Mockito.when(todoService.getAllTodo()).thenReturn(todos);
        Mockito.verifyNoInteractions(todoService);
    }
}