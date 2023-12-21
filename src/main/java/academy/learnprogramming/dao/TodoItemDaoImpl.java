package academy.learnprogramming.dao;

import academy.learnprogramming.model.TodoItem;
import lombok.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TodoItemDaoImpl implements TodoItemDao{


    @Override
    public List<TodoItem> getAllTodoItems() {
        return null;
    }

    @Override
    public boolean addItem(@NonNull TodoItem item) {
        return false;
    }

    @Override
    public boolean removeItem(int id) {
        return false;
    }

    @Override
    public TodoItem getItem(int id) {
        return null;
    }

    @Override
    public boolean updateItem(@NonNull TodoItem item) {
        return false;
    }
}
