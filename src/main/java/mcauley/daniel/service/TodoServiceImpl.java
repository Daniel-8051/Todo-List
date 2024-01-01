package mcauley.daniel.service;

import lombok.extern.slf4j.Slf4j;
import mcauley.daniel.dao.TodoItemDao;
import mcauley.daniel.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class TodoServiceImpl implements TodoItemService {

    private final TodoItemDao todoItemDao;

    @Autowired
    public TodoServiceImpl(TodoItemDao todoItemDao) {
        this.todoItemDao = todoItemDao;
    }

    @Override
    public boolean addItem(TodoItem todoItem) {
        return todoItemDao.addItem(todoItem);
    }

    @Override
    public boolean removeItem(int id) {
        return todoItemDao.removeItem(id);
    }

    @Override
    public void removeAllItems(){
        todoItemDao.deleteAllItems();
    }

    @Override
    public TodoItem getItem(int id) {
        return todoItemDao.getItem(id);
    }

    @Override
    public List<TodoItem> getAllItems(){
        List<TodoItem> items = todoItemDao.getAllTodoItems();
        log.info("Returning {} items", items.size());
        return items;
    }

    @Override
    public boolean updateItem(TodoItem todoItem) {
        return todoItemDao.updateItem(todoItem);
    }
}
