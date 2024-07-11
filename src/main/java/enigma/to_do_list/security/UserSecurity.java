package enigma.to_do_list.security;

import enigma.to_do_list.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserSecurity {

    public boolean isUser(Authentication authentication, int userId) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return false;
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof User user)) {
            return false;
        }

        return user.getId() == userId;
    }
}
