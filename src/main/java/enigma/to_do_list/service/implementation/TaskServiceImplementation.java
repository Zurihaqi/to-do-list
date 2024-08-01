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

import java.time.OffsetDateTime;

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

        // if task due date dont have "Z" at the end, add it
        if(!task.getDueDate().endsWith("Z")) task.setDueDate(task.getDueDate() + "Z");
        OffsetDateTime validDueDate = OffsetDateTime.parse(task.getDueDate());
        if(validDueDate.isBefore(OffsetDateTime.now())) throw new RuntimeException("due date must be in the future");

        Task newTask = taskRepository.save(Task.builder()
                .user(user)
                .title(task.getTitle())
                .description(task.getDescription())
                .createdAt(OffsetDateTime.now())
                .status(Task.Status.PENDING)
                .dueDate(validDueDate)
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
        if(userSecurity.getCurrentUserRole().equals("USER") && !userSecurity.getCurrentUserId().equals(newTask.getUser().getId())) throw new UnauthorizedRoleException("you can't update other user's tasks");

        // if task due date dont have "Z" at the end, add it
        if(task.getDueDate() != null && !task.getDueDate().endsWith("Z")) task.setDueDate(task.getDueDate() + "Z");

        if(task.getTitle() != null && !task.getTitle().isEmpty() && !task.getTitle().isBlank()) newTask.setTitle(task.getTitle());
        if(task.getStatus() != null) newTask.setStatus(task.getStatus());
        if(task.getDueDate() != null) newTask.setDueDate(OffsetDateTime.parse(task.getDueDate()));
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
        if(userSecurity.getCurrentUserRole().equals("USER") && !userSecurity.getCurrentUserId().equals(newTask.getUser().getId())) throw new UnauthorizedRoleException("you can't update other user's tasks");
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
    public Page<TaskDto.response> getAll(User user, Pageable pageable, String status, String sortBy, String order) {

        Specification<Task> specStatus = TaskSpecification.filterByStatus(status);
        Specification<Task> specUserId = TaskSpecification.filterByUserId(user.getId());

        Specification<Task> specification = user.getRole().equals(User.Role.ADMIN)
                ? specStatus
                : specUserId.and(specStatus);

        Pageable sortedPageable = createPageableWithSorting(pageable, sortBy, order);

        Page<Task> tasks = taskRepository.findAll(specification, sortedPageable);

        Page<TaskDto.response> taskDtos = tasks.map(task -> TaskDto.response.builder()
                .id(task.getId())
                .userId(userSecurity.getCurrentUserRole().equals("USER") ? null : task.getUser().getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .createdAt(task.getCreatedAt())
                .build());

        return taskDtos;
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
        if(userSecurity.getCurrentUserRole().equals("USER") && !userSecurity.getCurrentUserId().equals(task.getUser().getId())) throw new UnauthorizedRoleException("you can't view other user's tasks");

        return TaskDto.response.builder()
                .title(task.getTitle())
                .id(task.getId())
                .userId(userSecurity.getCurrentUserRole().equals("USER") ? null : task.getUser().getId())
                .createdAt(task.getCreatedAt())
                .description(task.getDescription())
                .dueDate(task.getDueDate())
                .status(task.getStatus())
                .build();
    }

    @Override
    public void delete(Integer id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new RuntimeException("task with id " + id + " not found"));
        if(userSecurity.getCurrentUserRole().equals("USER") && !userSecurity.getCurrentUserId().equals(task.getUser().getId())) throw new UnauthorizedRoleException("you can't delete other user's tasks");

        taskRepository.deleteById(id);
    }
}
