package enigma.to_do_list.service.implementation;

import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.TaskRepository;
import enigma.to_do_list.repository.UserRepository;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public Task create(TaskDto task) {
        User user = userRepository.findById(task.getUser_id()).orElseThrow(() -> new RuntimeException("user with id " + task.getUser_id() + " cannot be found"));
        if(task.getUser_id() == null) throw new RuntimeException("user_id cannot be empty");
        if(task.getTask() == null || task.getTask().isEmpty() || task.getTask().isBlank()) throw new RuntimeException("task cannot be empty");

        return taskRepository.save(Task.builder()
                .user(user)
                .task(task.getTask())
                .createdAt(new Date())
                .completed(false)
                .completedAt(null)
                .build()
        );
    }

    @Override
    public Task update(Integer id, TaskDto task) {
        Task newTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " doesn't exist"));

        if(task.getUser_id() != null){
            boolean userExist = userRepository.existsById(task.getUser_id());
            if(!userExist) throw new RuntimeException("user with id " + task.getUser_id() + " doesn't exist");
            task.setUser_id(task.getUser_id());
        }

        if(task.getTask() == null || task.getTask().isEmpty() || task.getTask().isBlank()) throw new RuntimeException("task cannot be empty");
        newTask.setTask(task.getTask());

        return taskRepository.save(newTask);
    }

    @Override
    public Page<Task> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public Task getById(Integer id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " not found"));
    }

    @Override
    public void delete(Integer id) {
        if(!taskRepository.existsById(id)) throw new RuntimeException("task with id " + id + " not found");
        taskRepository.deleteById(id);
    }

    @Override
    public void completeTask(Integer id) {
        if(!taskRepository.existsById(id)) throw new RuntimeException("task with id " + id + " not found");
        Task task = getById(id);

        task.setCompleted(true);
        task.setCompletedAt(new Date());

        taskRepository.save(task);
    }
}
