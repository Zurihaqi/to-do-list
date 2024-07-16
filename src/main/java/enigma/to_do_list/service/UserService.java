package enigma.to_do_list.service;

import enigma.to_do_list.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    void updateName(Integer id, String name);

    void updateEmail(Integer id, String email);

    void updatePassword(Integer id, String password);

    Page<User> getAll(Pageable pageable);

    User getById(Integer id);

    void delete(Integer id);
}
