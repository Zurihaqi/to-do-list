package enigma.to_do_list.service;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.utils.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    TaskDto.response create(TaskDto.request task);

    TaskDto.response update(Integer id, TaskDto.request task);

    Page<Task> getAll(Pageable pageable);

    TaskDto.response getById(Integer id);

    void delete(Integer id);

    void toggleCompletion(Integer id);
}
