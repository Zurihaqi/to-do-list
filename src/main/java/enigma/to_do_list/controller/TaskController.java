package enigma.to_do_list.controller;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.User;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.PageResponseWrapper;
import enigma.to_do_list.utils.dto.TaskDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos/")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<?> create(@AuthenticationPrincipal User user, @RequestBody TaskDto.request request){
        return new ResponseEntity<>(taskService.create(user, request), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody TaskDto.request request){
        return new ResponseEntity<>(taskService.update(id, request), HttpStatus.OK);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestBody TaskDto.request request){
        return new ResponseEntity<>(taskService.updateStatus(id, request), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ){

        sortBy = (sortBy == null || sortBy.isEmpty()) ? "createdAt" : sortBy;
        order = (order == null || order.isEmpty()) ? "asc" : order;

        Page<Task> task = taskService.getAll(user, pageable, status, sortBy, order);
        PageResponseWrapper<Task> response = new PageResponseWrapper<>(task);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return new ResponseEntity<>(taskService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        taskService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
