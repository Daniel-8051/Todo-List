package mcauley.daniel.controller;

import lombok.extern.slf4j.Slf4j;
import mcauley.daniel.model.TodoItem;
import mcauley.daniel.service.TodoItemService;
import mcauley.daniel.util.AttributeNames;
import mcauley.daniel.util.Mappings;
import mcauley.daniel.util.ViewNames;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Controller
public class TodoItemController {

    private final TodoItemService todoItemService;

    @Autowired
    public TodoItemController(TodoItemService todoItemService) {
        this.todoItemService = todoItemService;
    }

    @ModelAttribute("todoItems")
    public List<TodoItem> todoItems() {
        return todoItemService.getAllItems();
    }

    @GetMapping(Mappings.ITEMS)
    public String items() {
        return ViewNames.ITEMS_LIST;
    }

    @GetMapping(Mappings.ADD_ITEM)
    public String addEditItem(@RequestParam(required = false, defaultValue = "-1") int id, Model model) {
        log.info("Editing item with id = {}", id);
        TodoItem todoItem;
        if (id != -1) {
            todoItem = todoItemService.getItem(id);
        } else {
            todoItem = TodoItem.builder().title("").details("").deadline(LocalDate.now()).build();
        }
        model.addAttribute(AttributeNames.TODO_ITEM, todoItem);
        return ViewNames.ADD_ITEM;
    }

    @PostMapping(Mappings.ADD_ITEM)
    public String processItem(@ModelAttribute(AttributeNames.TODO_ITEM) TodoItem todoItem) {
        log.info("todoItem from form = {}", todoItem);
        if (todoItem.getId() == 0) {
            todoItemService.addItem(todoItem);
        } else {
            todoItemService.updateItem(todoItem);
        }
        return "redirect:/" + Mappings.ITEMS;
    }

    @GetMapping(Mappings.DELETE_ITEM)
    public String deleteItem(@RequestParam int id) {
        log.info("Deleting item with id {}", id);
        todoItemService.removeItem(id);
        return "redirect:/" + Mappings.ITEMS;
    }

    @GetMapping(Mappings.VIEW_ITEM)
    public String viewItem(@RequestParam int id, Model model) throws Exception {
        TodoItem item = todoItemService.getItem(id);
        if (item != null) {
            log.info("Returning item {}", item);
            model.addAttribute(AttributeNames.TODO_ITEM, item);
            return ViewNames.VIEW_ITEM;
        } else {
            throw new RuntimeException("Could not get item with id " + id);
        }
    }

    @GetMapping(Mappings.HOME)
    public String viewHome() {
        return ViewNames.HOME;
    }

    @GetMapping(Mappings.DELETE_ALL)
    public String deleteAllItems(){
        todoItemService.removeAllItems();
        return "redirect:/" + Mappings.ITEMS;
    }

}
