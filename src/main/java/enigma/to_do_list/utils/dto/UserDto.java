package enigma.to_do_list.utils.dto;

import enigma.to_do_list.model.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
public class UserDto {
    private Integer id;
    private String username;
    private String email;
    private User.Role role;
    private Date createdAt;
}
