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
    public TaskDto.response create(TaskDto.request task) {
        User user = userRepository.findById(task.getUser_id()).orElseThrow(() -> new RuntimeException("user with id " + task.getUser_id() + " cannot be found"));
        if(task.getUser_id() == null) throw new RuntimeException("user_id cannot be empty");
        if(task.getTask() == null || task.getTask().isEmpty() || task.getTask().isBlank()) throw new RuntimeException("task cannot be empty");

        Task newTask = taskRepository.save(Task.builder()
                .user(user)
                .task(task.getTask())
                .createdAt(new Date())
                .completed(false)
                .completedAt(null)
                .build()
        );

        return TaskDto.response.builder()
                .task(newTask.getTask())
                .user_id(newTask.getUser().getId())
                .createdAt(newTask.getCreatedAt())
                .completedAt(newTask.getCompletedAt())
                .completed(newTask.isCompleted())
                .build();
    }

    @Override
    public TaskDto.response update(Integer id, TaskDto.request task) {
        Task newTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " doesn't exist"));

        // Update user is optional
        if(task.getUser_id() != null){
            newTask.setUser(userRepository.findById(task.getUser_id()).orElseThrow(() -> new RuntimeException("user with id " + task.getUser_id() + " doesn't exist")));
        }

        if(task.getTask() == null || task.getTask().isEmpty() || task.getTask().isBlank()) throw new RuntimeException("task cannot be empty");
        newTask.setTask(task.getTask());

        taskRepository.save(newTask);

        return TaskDto.response.builder()
                .task(newTask.getTask())
                .user_id(newTask.getUser().getId())
                .createdAt(newTask.getCreatedAt())
                .completedAt(newTask.getCompletedAt())
                .completed(newTask.isCompleted())
                .build();
    }

    @Override
    public Page<Task> getAll(Pageable pageable) {
        return taskRepository.findAll(pageable);
    }

    @Override
    public TaskDto.response getById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " not found"));
        return TaskDto.response.builder()
                .task(task.getTask())
                .user_id(task.getUser().getId())
                .createdAt(task.getCreatedAt())
                .completedAt(task.getCompletedAt())
                .completed(task.isCompleted())
                .build();
    }

    @Override
    public void delete(Integer id) {
        if(!taskRepository.existsById(id)) throw new RuntimeException("task with id " + id + " not found");
        taskRepository.deleteById(id);
    }

    @Override
    public void toggleCompletion(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " not found"));

        task.setCompleted(!task.isCompleted());
        if (task.isCompleted()) {
            task.setCompletedAt(new Date());
        } else {
            task.setCompletedAt(null);
        }

        taskRepository.save(task);
    }
}
