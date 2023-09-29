package no.hvl.dat250.rest.todos;

import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/todos")
public class TodoController {
  public static final String TODO_WITH_THE_ID_X_NOT_FOUND = "Todo with the ID %d not found";
  private final ArrayList<Todo> todos = new ArrayList<>();
  private Long idCount = 0L;

//  Get all Todos
  @GetMapping
  public List<Todo> getAllTodos() {
    return todos;
  }
  //  Get one Todo by ID
  @GetMapping("/{id}")
  public Todo getTodo(@PathVariable Long id) {
    return todos.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new TodoNotFoundException(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id)));
  }
  //  Post Todo
  @PostMapping
  public Todo createTodo(@RequestBody Todo todo) {
    todo.setId(idCount++);
    todos.add(todo);
    return todos.get(todos.size() - 1);
  }

  // Edit one todo
  @PutMapping("/{id}")
  public Todo updateTodo(@PathVariable Long id, @RequestBody Todo newTodo) {
    Optional<Todo> oldTodo = todos.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst();

    if (oldTodo.isPresent()) {
      todos.remove(oldTodo.get());
      newTodo.setId(id);
      todos.add(newTodo);
      return todos.get(todos.size() - 1);
    }

    throw new TodoNotFoundException(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id));
  }
  // Delete Todo by Id
  @DeleteMapping("/{id}")
  public String deleteTodo(@PathVariable Long id) {
    Todo todo = todos.stream()
            .filter(t -> t.getId().equals(id))
            .findFirst()
            .orElseThrow(() -> new TodoNotFoundException(String.format(TODO_WITH_THE_ID_X_NOT_FOUND, id)));

    todos.remove(todo);
    return "Todo deleted successfully!";
  }









  private static class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(String message) {
      super(message);
    }
  }

}