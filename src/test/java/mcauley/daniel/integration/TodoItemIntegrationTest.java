package mcauley.daniel.integration;

import mcauley.daniel.controller.TodoItemController;
import mcauley.daniel.dao.TodoItemDao;
import mcauley.daniel.model.TodoItem;
import mcauley.daniel.service.TodoItemService;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TodoItemIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TodoItemController controller;

    @SpyBean
    private TodoItemService service;

    @SpyBean
    private TodoItemDao dao;

    private final EasyRandom easyRandom = new EasyRandom();

    @Test
    @Order(1)
    public void getsAllItems() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/items"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "items_list");
        List<TodoItem> resultList = (List<TodoItem>) modelAndView.getModel().get("todoItems");
        assertEquals(5, resultList.size());
        verify(controller, times(1)).items();
        verify(service, times(1)).getAllItems();
        verify(dao, times(1)).getAllTodoItems();
    }

    @Test
    @Order(2)
    public void returnsItemToPopulateAddItemPage_IfExists() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/addItem").param("id", "1"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "add_item");
        verify(controller, times(1)).addEditItem(eq(1), any(Model.class));
        verify(service, times(1)).getItem(eq(1));
        verify(dao, times(1)).getItem(eq(1));
        ModelAndViewAssert.assertModelAttributeValue(modelAndView, "todoItem", dao.getItem(1));
    }

    @Test
    @Order(3)
    public void addNewItem() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // And
        TodoItem todoItem = easyRandom.nextObject(TodoItem.class);
        todoItem.setId(0);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/addItem")
                        .flashAttr("todoItem", todoItem))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(controller, times(1)).processItem(eq(todoItem));
        verify(service, times(1)).addItem(eq(todoItem));
        verify(dao, times(1)).addItem(eq(todoItem));
        assertEquals(6, dao.getAllTodoItems().size());
    }

    @Test
    @Order(4)
    public void updateExistingItem() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // And
        TodoItem todoItem = dao.getItem(3);
        todoItem.setDetails("update details");

        // When
        MvcResult mvcResult = mockMvc.perform(post("/addItem")
                        .flashAttr("todoItem", todoItem))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(controller, times(1)).processItem(eq(todoItem));
        verify(service, times(1)).updateItem(eq(todoItem));
        verify(dao, times(1)).updateItem(eq(todoItem));
        assertEquals(5, dao.getAllTodoItems().size());
        assertEquals("update details", dao.getItem(3).getDetails());
    }

    @Test
    @Order(5)
    public void deleteItem() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/deleteItem").param("id", "2"))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(controller, times(1)).deleteItem(eq(2));
        verify(service, times(1)).removeItem(eq(2));
        verify(dao, times(1)).removeItem(eq(2));
        assertEquals(4, dao.getAllTodoItems().size());
    }

    @Test
    @Order(6)
    public void viewItem_Success() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/item").param("id", "3"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "view_item");
        verify(controller, times(1)).viewItem(eq(3), any(Model.class));
        verify(service, times(1)).getItem(eq(3));
        verify(dao, times(1)).getItem(eq(3));
        ModelAndViewAssert.assertModelAttributeValue(modelAndView, "todoItem", dao.getItem(3));
    }

    @Test
    @Order(7)
    public void viewItem_Fail() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/item").param("id", "10"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "error");
        verify(controller, times(1)).viewItem(eq(10), any(Model.class));
        verify(service, times(1)).getItem(eq(10));
        verify(dao, times(1)).getItem(eq(10));
        String exceptionMessage = (String) modelAndView.getModel().get("exceptionMessage");
        assertEquals("Incorrect result size: expected 1, actual 0", exceptionMessage);
    }

    /**
     * This method needs executed last because TRUNCATE in MySQL cannot be rolled back.
     * If not executed last, the TRUNCATE will persist and effect the other tests
     */
    @Test
    @Order(8)
    public void deleteAllItems() throws Exception {
        // Given 5 items exist in DB (data.sql)
        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/deleteAllItems"))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(controller, times(1)).deleteAllItems();
        verify(service, times(1)).removeAllItems();
        verify(dao, times(1)).deleteAllItems();
        assertEquals(0, dao.getAllTodoItems().size());
    }
}
