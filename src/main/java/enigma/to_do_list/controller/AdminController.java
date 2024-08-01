package enigma.to_do_list.controller;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.User;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.service.UserService;
import enigma.to_do_list.utils.PageResponseWrapper;
import enigma.to_do_list.utils.dto.AuthDto;
import enigma.to_do_list.utils.dto.TaskDto;
import enigma.to_do_list.utils.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final UserService userService;
    private final TaskService taskService;
    private final String superAdminSecretKey = "superman admin turing machine gigachad sigma";
    private final String adminSecretKey = "admin turing machine alpha beta";

    @GetMapping("/users")
    public ResponseEntity<?> getAll(@PageableDefault Pageable pageable){
        PageResponseWrapper<UserDto> response = new PageResponseWrapper<>(userService.getAll(pageable), true);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @PatchMapping("/users/{id}/role")
    public ResponseEntity<?> updateRole(
            @AuthenticationPrincipal User user,
            @PathVariable Integer id,
            @Valid @RequestBody User request,
            @RequestHeader(value = "X-Admin-Secret-Key", required = false) String adminSecretKeyHeader
    ){
        if (!adminSecretKey.equals(adminSecretKeyHeader)) {
            return new ResponseEntity<>("Invalid admin secret key", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(userService.updateRole(id, request.getRole().toString()), HttpStatus.OK);
    }

    @PostMapping("/super-admin")
    public ResponseEntity<?> createSuperAdmin(
            @Valid @RequestBody AuthDto.RegisterRequest request,
            @RequestHeader(value = "X-Super-Admin-Secret-Key", required = false) String superAdminSecretKeyHeader){
        if (!superAdminSecretKey.equals(superAdminSecretKeyHeader)) {
            return new ResponseEntity<>("Invalid admin secret key", HttpStatus.FORBIDDEN);
        }

        return new ResponseEntity<>(userService.createSuperAdmin(request), HttpStatus.CREATED);
    }

    @GetMapping("/todos")
    public ResponseEntity<?> getAllTodos(
            @AuthenticationPrincipal User user,
            @PageableDefault Pageable pageable,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String order
    ){
        sortBy = (sortBy == null || sortBy.isEmpty()) ? "createdAt" : sortBy;
        order = (order == null || order.isEmpty()) ? "asc" : order;

        Page<TaskDto.response> task = taskService.getAll(user, pageable, status, sortBy, order);
        PageResponseWrapper<TaskDto.response> response = new PageResponseWrapper<>(task);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/todos/{id}")
    public ResponseEntity<?> getTodoById(@PathVariable Integer id){
        return new ResponseEntity<>(taskService.getById(id), HttpStatus.OK);
    }
}
