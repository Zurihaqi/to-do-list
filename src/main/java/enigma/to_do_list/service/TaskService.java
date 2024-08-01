package enigma.to_do_list.service;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.User;
import enigma.to_do_list.utils.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto.response create(User user, TaskDto.request task);

    TaskDto.response update(Integer id, TaskDto.request task);

    TaskDto.response updateStatus(Integer id, TaskDto.request task);

    Page<TaskDto.response> getAll(User user, Pageable pageable, String status, String sortBy, String order);

    TaskDto.response getById(Integer id);

    void delete(Integer id);
}
