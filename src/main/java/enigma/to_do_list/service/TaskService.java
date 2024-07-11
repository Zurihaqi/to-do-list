package enigma.to_do_list.service;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.utils.dto.TaskDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TaskService {
    Task create(TaskDto task);

    Task update(Integer id, TaskDto task);

    Page<Task> getAll(Pageable pageable);

    Task getById(Integer id);

    void delete(Integer id);

    void completeTask(Integer id);
}
