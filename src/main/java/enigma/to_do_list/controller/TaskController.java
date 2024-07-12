package enigma.to_do_list.controller;

import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.dto.TaskDto;
import enigma.to_do_list.utils.response.PageResponse;
import enigma.to_do_list.utils.response.Response;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/tasks/")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> create(@RequestBody TaskDto request){
        return Response.renderJson(taskService.create(request), "task created", HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody TaskDto request){
        if(request.getTask() == null) throw new RuntimeException("task cannot be empty");

        return Response.renderJson(taskService.update(id, request), "task updated", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault Pageable pageable){
        return Response.renderJson(new PageResponse<>(taskService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return Response.renderJson(taskService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        taskService.delete(id);
        return Response.renderJson("task deleted", HttpStatus.OK);
    }

    @PatchMapping("/toggle-completion/{id}")
    public ResponseEntity<?> toggleCompletion(@PathVariable Integer id){
        taskService.toggleCompletion(id);
        return Response.renderJson(
                taskService.getById(id).isCompleted() ? "task with id " + id + ": set completed" : "task with id " + id + ": set incomplete",
                HttpStatus.OK
        );
    }
}
