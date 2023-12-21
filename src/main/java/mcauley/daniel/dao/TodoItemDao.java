package mcauley.daniel.dao;

import mcauley.daniel.model.TodoItem;
import lombok.NonNull;

import java.util.List;

public interface TodoItemDao {
    List<TodoItem> getAllTodoItems();
    boolean addItem(@NonNull TodoItem item);
    boolean removeItem(int id);
    TodoItem getItem(int id);
    boolean updateItem(@NonNull TodoItem item);
}
