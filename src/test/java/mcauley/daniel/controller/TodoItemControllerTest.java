package mcauley.daniel.controller;

import mcauley.daniel.model.TodoItem;
import mcauley.daniel.service.TodoServiceImpl;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class TodoItemControllerTest {

    @MockBean
    private TodoServiceImpl todoServiceMock;

    @InjectMocks
    private TodoItemController todoItemController;

    @Autowired
    private MockMvc mockMvc;

    private final EasyRandom easyRandom = new EasyRandom();

    private List<TodoItem> todoItemList;

    @BeforeEach
    public void setUp(){
        todoItemList = easyRandom.objects(TodoItem.class, 3).collect(Collectors.toList());
        when(todoServiceMock.getAllItems()).thenReturn(todoItemList);
    }

    @Test
    public void getsAllItems() throws Exception {
        // Given
        List<TodoItem> todoItemList = easyRandom.objects(TodoItem.class, 3).collect(Collectors.toList());
        when(todoServiceMock.getAllItems()).thenReturn(todoItemList);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/items"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "items_list");
        ModelAndViewAssert.assertCompareListModelAttribute(modelAndView, "todoItems", todoItemList);
    }

    @Test
    public void returnsItemToPopulateAddItemPage_IfExists() throws Exception {
        // Given
        TodoItem todoItem = easyRandom.nextObject(TodoItem.class);
        when(todoServiceMock.getItem(anyInt())).thenReturn(todoItem);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/addItem").param("id", "1"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "add_item");
        ModelAndViewAssert.assertModelAttributeValue(modelAndView, "todoItem", todoItem);
    }

    @Test
    public void returnsBlankNewItemToPopulateAddItemPage_IfItemDoesNotExist() throws Exception {
        // Given & When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/addItem").param("id", "-1"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "add_item");
        TodoItem todoItem = (TodoItem) modelAndView.getModel().get("todoItem");
        assertEquals("", todoItem.getTitle());
    }

    @Test
    public void addNewItem() throws Exception {
        // Given
        TodoItem todoItem = easyRandom.nextObject(TodoItem.class);
        todoItem.setId(0);
        when(todoServiceMock.addItem(any())).thenReturn(true);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/addItem")
                .flashAttr("todoItem", todoItem))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(todoServiceMock, times(1)).addItem(eq(todoItem));
    }

    @Test
    public void updateExistingItem() throws Exception {
        // Given
        TodoItem todoItem = easyRandom.nextObject(TodoItem.class);
        todoItem.setId(1);
        when(todoServiceMock.updateItem(any())).thenReturn(true);

        // When
        MvcResult mvcResult = mockMvc.perform(post("/addItem")
                        .flashAttr("todoItem", todoItem))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(todoServiceMock, times(1)).updateItem(eq(todoItem));
    }

    @Test
    public void deleteItem() throws Exception {
        // Given
        when(todoServiceMock.removeItem(anyInt())).thenReturn(true);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/deleteItem").param("id", "2"))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(todoServiceMock, times(1)).removeItem(eq(2));
    }

    @Test
    public void viewItem_Success() throws Exception {
        // Given
        TodoItem todoItem = easyRandom.nextObject(TodoItem.class);
        todoItem.setId(3);
        when(todoServiceMock.getItem(anyInt())).thenReturn(todoItem);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/item").param("id", "3"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        verify(todoServiceMock, times(1)).getItem(eq(3));
        ModelAndViewAssert.assertViewName(modelAndView, "view_item");
        ModelAndViewAssert.assertModelAttributeValue(modelAndView, "todoItem", todoItem);
        TodoItem result = (TodoItem) modelAndView.getModel().get("todoItem");;
        assertEquals(3, result.getId());
    }

    @Test
    public void viewItem_Fail() throws Exception {
        // Given
        when(todoServiceMock.getItem(anyInt())).thenReturn(null);

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/item").param("id", "3"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        verify(todoServiceMock, times(1)).getItem(eq(3));
        ModelAndViewAssert.assertViewName(modelAndView, "error");
        String exceptionMessage = (String) modelAndView.getModel().get("exceptionMessage");
        assertEquals("Could not get item with id 3", exceptionMessage);
    }

    @Test
    public void viewHome() throws Exception {
        // Given & When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/home"))
                .andExpect(status().isOk()).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "home");
    }

    @Test
    public void deleteAll() throws Exception {
        // Given
        doNothing().when(todoServiceMock).removeAllItems();

        // When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/deleteAllItems"))
                .andExpect(redirectedUrl("/items")).andReturn();
        ModelAndView modelAndView = mvcResult.getModelAndView();

        // Then
        ModelAndViewAssert.assertViewName(modelAndView, "redirect:/items");
        verify(todoServiceMock, times(1)).removeAllItems();
    }

}
