package mcauley.daniel.service;

import mcauley.daniel.model.TodoItem;

import java.util.List;

public interface TodoItemService {
    boolean addItem(TodoItem todoItem);
    boolean removeItem(int id);
    TodoItem getItem(int id);
    List<TodoItem> getAllItems();
    boolean updateItem(TodoItem todoItem);
    void removeAllItems();
}
