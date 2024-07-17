package enigma.to_do_list.repository;

import enigma.to_do_list.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Integer> {
    Optional<Task> findByUser_id(Integer id);
    Page<Task> findAllByUser_id(Integer id, Pageable pageable);
}
