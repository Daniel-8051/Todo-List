package mcauley.daniel.dao;

import mcauley.daniel.model.TodoItem;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class TodoItemRowMapper implements RowMapper<TodoItem> {
    @Override
    public TodoItem mapRow(ResultSet rs, int rowNum) throws SQLException {
        return TodoItem.builder()
                .id(Integer.parseInt(rs.getString("id")))
                .title(rs.getString("title"))
                .details(rs.getString("details"))
                .deadline(LocalDate.parse(rs.getString("deadline")))
                .build();
    }
}
