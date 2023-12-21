package mcauley.daniel.service;

import mcauley.daniel.model.TodoData;
import mcauley.daniel.model.TodoItem;

public interface TodoItemService {
    void addItem(TodoItem todoItem);
    void removeItem(int id);
    TodoItem getItem(int id);
    void updateItem(TodoItem todoItem);
    TodoData getData();
}
