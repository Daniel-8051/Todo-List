package mcauley.daniel.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
@Builder
@EqualsAndHashCode(of = "id")
public class TodoItem {
    private int id;
    private String title;
    private String details;
    private LocalDate deadline;
}
