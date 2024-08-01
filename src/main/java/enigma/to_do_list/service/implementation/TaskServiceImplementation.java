package enigma.to_do_list.service.implementation;

import enigma.to_do_list.exception.UnauthorizedRoleException;
import enigma.to_do_list.model.Task;
import enigma.to_do_list.model.User;
import enigma.to_do_list.repository.TaskRepository;
import enigma.to_do_list.repository.UserRepository;
import enigma.to_do_list.security.UserSecurity;
import enigma.to_do_list.service.TaskService;
import enigma.to_do_list.utils.dto.TaskDto;
import enigma.to_do_list.utils.specification.TaskSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TaskServiceImplementation implements TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserSecurity userSecurity;

    @Override
    public TaskDto.response create(User user, TaskDto.request task) {
        if(task.getTitle() == null || task.getTitle().isEmpty() || task.getTitle().isBlank()) throw new RuntimeException("title cannot be empty");
        if(task.getDueDate() == null) throw new RuntimeException("dueDate cannot be empty");
        if(task.getDescription() == null || task.getDescription().isEmpty() || task.getDescription().isBlank()) throw new RuntimeException("description cannot be empty");

        Date validDueDate = task.getDueDate();
        if(validDueDate.before(new Date())) throw new RuntimeException("due date must be in the future");

        Task newTask = taskRepository.save(Task.builder()
                .user(user)
                .title(task.getTitle())
                .description(task.getDescription())
                .createdAt(new Date())
                .status(Task.Status.PENDING)
                .dueDate(task.getDueDate())
                .build()
        );

        return TaskDto.response.builder()
                .title(newTask.getTitle())
                .id(newTask.getId())
                .description(newTask.getDescription())
                .createdAt(newTask.getCreatedAt())
                .dueDate(newTask.getDueDate())
                .status(newTask.getStatus())
                .build();
    }

    @Override
    public TaskDto.response update(Integer id, TaskDto.request task) {
        Task newTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " doesn't exist"));
        if(userSecurity.getCurrentUserRole().equals("ROLE_USER") && !userSecurity.getCurrentUserId().equals(newTask.getUser().getId())) throw new UnauthorizedRoleException("you can't update other user's tasks");

        if(task.getTitle() != null && !task.getTitle().isEmpty() && !task.getTitle().isBlank()) newTask.setTitle(task.getTitle());
        if(task.getStatus() != null) newTask.setStatus(task.getStatus());
        if(task.getDueDate() != null) newTask.setDueDate(task.getDueDate());
        if(task.getDescription() != null && !task.getDescription().isEmpty() && !task.getDescription().isBlank()) newTask.setDescription(task.getDescription());

        taskRepository.save(newTask);

        return TaskDto.response.builder()
                .title(newTask.getTitle())
                .id(newTask.getId())
                .createdAt(newTask.getCreatedAt())
                .description(newTask.getDescription())
                .dueDate(newTask.getDueDate())
                .status(newTask.getStatus())
                .build();
    }

    @Override
    public TaskDto.response updateStatus(Integer id, TaskDto.request task) {
        Task newTask = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " doesn't exist"));
        if(userSecurity.getCurrentUserRole().equals("ROLE_USER") && !userSecurity.getCurrentUserId().equals(newTask.getUser().getId())) throw new UnauthorizedRoleException("you can't update other user's tasks");
        if(task.getStatus() == null) throw new RuntimeException("status cannot be empty");

        newTask.setStatus(task.getStatus());
        taskRepository.save(newTask);

        return TaskDto.response.builder()
                .title(newTask.getTitle())
                .id(newTask.getId())
                .createdAt(newTask.getCreatedAt())
                .description(newTask.getDescription())
                .dueDate(newTask.getDueDate())
                .status(newTask.getStatus())
                .build();
    }

    @Override
    public Page<Task> getAll(User user, Pageable pageable, String status, String sortBy, String order) {

        Specification<Task> specStatus = TaskSpecification.filterByStatus(status);
        Specification<Task> specUserId = TaskSpecification.filterByUserId(user.getId());

        Specification<Task> specification = user.getRole().equals(User.Role.ROLE_ADMIN)
                ? specStatus
                : specUserId.and(specStatus);

        Pageable sortedPageable = createPageableWithSorting(pageable, sortBy, order);

        return taskRepository.findAll(specification, sortedPageable);
    }

    private Pageable createPageableWithSorting(Pageable pageable, String sortBy, String order) {
        Sort sort = Sort.by(Sort.Order.asc("createdAt"));
        if ("dueDate".equalsIgnoreCase(sortBy)) {
            sort = Sort.by(Sort.Order.asc("dueDate"));
        }

        if ("desc".equalsIgnoreCase(order)) {
            sort = sort.and(Sort.by(Sort.Order.desc(sortBy)));
        } else {
            sort = sort.and(Sort.by(Sort.Order.asc(sortBy)));
        }

        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    }

    @Override
    public TaskDto.response getById(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " not found"));
        if(userSecurity.getCurrentUserRole().equals("ROLE_USER") && !userSecurity.getCurrentUserId().equals(task.getUser().getId())) throw new UnauthorizedRoleException("you can't view other user's tasks");

        return TaskDto.response.builder()
                .title(task.getTitle())
                .id(task.getId())
                .createdAt(task.getCreatedAt())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    @Override
    public void delete(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " not found"));
        if(userSecurity.getCurrentUserRole().equals("ROLE_USER") && !userSecurity.getCurrentUserId().equals(task.getUser().getId())) throw new UnauthorizedRoleException("you can't delete other user's tasks");

        taskRepository.deleteById(id);
    }
}
