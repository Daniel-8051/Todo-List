package mcauley.daniel.model;

import jakarta.annotation.PostConstruct;
import lombok.NonNull;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

@Component
public class TodoData {

    private static int idValue = 1;
    private final List<TodoItem> items = new ArrayList<>();

    @PostConstruct
    public void createDummyData(){
        // dummy data
        addItem(new TodoItem("first", "first details", LocalDate.now()));
        addItem(new TodoItem("second", "second details", LocalDate.now()));
        addItem(new TodoItem("third", "third details", LocalDate.now()));
        addItem(new TodoItem("fourth", "fourth details", LocalDate.now()));
    }

    public List<TodoItem> getItems() {
        return Collections.unmodifiableList(items); // does not allow other class to add or remove from list
    }

    public void addItem(@NonNull TodoItem toAdd) {
        toAdd.setId(idValue);
        items.add(toAdd);
        idValue++;
    }

    public void removeItem(int id) {
        ListIterator<TodoItem> itemListIterator = items.listIterator();
        while (itemListIterator.hasNext()) {
            TodoItem todoItem = itemListIterator.next();
            if (todoItem.getId() == id) {
                itemListIterator.remove();
                break;
            }
        }
    }

    public TodoItem getItem(int id) {
        for (TodoItem item : items) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    public void updateItem(@NonNull TodoItem toUpdate){
        ListIterator<TodoItem> itemListIterator = items.listIterator();
        while(itemListIterator.hasNext()){
            TodoItem todoItem = itemListIterator.next();
            if(todoItem.equals(toUpdate)){
                itemListIterator.set(toUpdate);
                break;
            }
        }
    }
}
