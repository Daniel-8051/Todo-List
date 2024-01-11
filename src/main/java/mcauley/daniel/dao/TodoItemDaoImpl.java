package mcauley.daniel.dao;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import mcauley.daniel.model.TodoItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;

@Repository
@Slf4j
@Transactional
public class TodoItemDaoImpl implements TodoItemDao {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    @Override
    public List<TodoItem> getAllTodoItems() {
        log.info("Getting all Items from database todo_item table");
        String sqlQuery = "SELECT * FROM todo_item";
        return namedParameterJdbcTemplate.query(sqlQuery, new TodoItemRowMapper());
    }

    @Override
    public boolean addItem(@NonNull TodoItem item) {
        SqlParameterSource beansParams = new BeanPropertySqlParameterSource(item);
        String sqlQuery = "INSERT INTO `spring_db`.`todo_item` (`title`, `details`, `deadline`) " +
                "VALUES (:title, :details, :deadline)";
        return namedParameterJdbcTemplate.update(sqlQuery, beansParams) == 1;
    }

    @Override
    public boolean removeItem(int id) {
        SqlParameterSource params = new MapSqlParameterSource("ID", id);
        String sqlQuery = "DELETE FROM todo_item WHERE id = :ID";
        return namedParameterJdbcTemplate.update(sqlQuery, params) == 1;
    }

    @Override
    public TodoItem getItem(int id) {
        log.info("Getting Item with id {} from todo_item database table", id);
        SqlParameterSource params = new MapSqlParameterSource("ID", id);
        String sqlQuery = "SELECT * FROM todo_item WHERE id = :ID";
        return namedParameterJdbcTemplate.queryForObject(sqlQuery, params, new TodoItemRowMapper());
    }

    @Override
    public boolean updateItem(@NonNull TodoItem item) {
        SqlParameterSource beanParam = new BeanPropertySqlParameterSource(item);
        String sqlQuery = "UPDATE `spring_db`.`todo_item` SET title = :title, details = :details, deadline = :deadline WHERE id = :id";
        return namedParameterJdbcTemplate.update(sqlQuery, beanParam) == 1;
    }

    /**
     * Truncate operations drop and re-create the table, which is much faster than deleting rows one by one, particularly for large tables.
     * Truncate operations cause an implicit commit, and so cannot be rolled back.
     */
    @Override
    public void deleteAllItems(){
        String sqlQuery = "TRUNCATE TABLE todo_item";
        namedParameterJdbcTemplate.getJdbcTemplate().execute(sqlQuery);
    }
}
