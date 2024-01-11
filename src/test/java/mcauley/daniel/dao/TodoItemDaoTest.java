package mcauley.daniel.dao;

import mcauley.daniel.model.TodoItem;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT, properties = ("spring.h2.console.enabled=true"))
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
public class TodoItemDaoTest {

    private final TodoItemDao todoItemDao;

    @Autowired
    public TodoItemDaoTest(TodoItemDao todoItemDao, JdbcTemplate jdbcTemplate) {
        this.todoItemDao = todoItemDao;
    }

    @Test
    @Order(1)
    public void getAllTodoItemsTest() {
        // Given 5 items have been inserted into table through data.sql

        // When
        List<TodoItem> todoItems = todoItemDao.getAllTodoItems();

        // Then
        assertEquals(5, todoItems.size());
        assertEquals("test item 1", todoItems.get(0).getTitle());
        assertEquals(1, todoItems.get(0).getId());
    }

    @Test
    @Order(2)
    public void addItemTest() {
        // Given
        TodoItem todoItem = TodoItem.builder()
                .id(6)
                .details("test item 6")
                .details("test item 6 details")
                .deadline(LocalDate.now())
                .build();

        // When
        boolean successfullyAdded = todoItemDao.addItem(todoItem);

        // Then
        assertTrue(successfullyAdded);
        assertEquals(todoItem, todoItemDao.getItem(6));
    }

    @Test
    @Order(3)
    public void removeItemTest() {
        // Given 5 items have been inserted into table through data.sql

        // When
        boolean successfullyRemoved = todoItemDao.removeItem(5);

        // Then
        assertTrue(successfullyRemoved);
        assertEquals(4, todoItemDao.getAllTodoItems().size());
    }

    @Test
    @Order(4)
    public void getItemByIdTest() {
        // Given 5 items have been inserted into table through data.sql

        // When
        TodoItem todoItem = todoItemDao.getItem(3);

        // Then
        assertEquals(3, todoItem.getId());
        assertEquals("test item 3", todoItem.getTitle());
    }

    @Test
    @Order(5)
    public void updateItemTest() {
        // Given 5 items have been inserted into table through data.sql
        // AND
        TodoItem todoItem = TodoItem.builder()
                .id(2)
                .title("updated title")
                .details("updated details")
                .deadline(LocalDate.parse("2024-01-05"))
                .build();

        // When
        boolean successfullyUpdated = todoItemDao.updateItem(todoItem);

        // Then
        assertTrue(successfullyUpdated);
        assertEquals("updated title", todoItemDao.getItem(2).getTitle());
        assertEquals("updated details", todoItemDao.getItem(2).getDetails());
    }

    /**
     * This method needs executed last because TRUNCATE in MySQL cannot be rolled back.
     * If not executed last, the TRUNCATE will persist and effect the other tests
     */
    @Test
    @Order(6)
    public void deleteAllItemsTest() {
        // Given 5 items have been inserted into table through data.sql

        // When
        todoItemDao.deleteAllItems();

        // Then
        assertEquals(0, todoItemDao.getAllTodoItems().size());
    }
}
