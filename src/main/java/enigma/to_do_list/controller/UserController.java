package enigma.to_do_list.controller;

import enigma.to_do_list.model.User;
import enigma.to_do_list.service.UserService;
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
@RequestMapping("/api/users/")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PutMapping("/{id}")
    @Validated
    public ResponseEntity<?> update(@PathVariable Integer id, @Valid @RequestBody User request){

        if(request.getName() != null) userService.updateName(id, request.getName());
        if(request.getEmail() != null) userService.updateEmail(id, request.getEmail());

        return Response.renderJson(userService.getById(id), "user updated", HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault Pageable pageable){
        return Response.renderJson(new PageResponse<>(userService.getAll(pageable)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return Response.renderJson(userService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        userService.delete(id);
        return Response.renderJson("user deleted", HttpStatus.OK);
    }
}
