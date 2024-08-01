package enigma.to_do_list.controller;

import enigma.to_do_list.model.User;
import enigma.to_do_list.service.UserService;
import enigma.to_do_list.utils.PageResponseWrapper;
import enigma.to_do_list.utils.dto.UserDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

        if(request.getUsername() != null) userService.updateName(id, request.getUsername());
        if(request.getEmail() != null) userService.updateEmail(id, request.getEmail());
        if(request.getPassword() != null) userService.updatePassword(id, request.getPassword());
        if(request.getRole() != null) userService.updateRole(id, request.getRole().name());

        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<?> getAll(@PageableDefault Pageable pageable){
        PageResponseWrapper<UserDto> response = new PageResponseWrapper<>(userService.getAll(pageable));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getSelfProfile(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(userService.getById(user.getId()), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id){
        return new ResponseEntity<>(userService.getById(id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
