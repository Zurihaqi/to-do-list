package enigma.to_do_list.service;

import enigma.to_do_list.model.User;
import enigma.to_do_list.utils.dto.UserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void updateName(Integer id, String name);

    void updateEmail(Integer id, String email);

    void updatePassword(Integer id, String password);

    void updateRole(Integer id, String role);

    Page<UserDto> getAll(Pageable pageable);

    UserDto getById(Integer id);

    void delete(Integer id);
}
