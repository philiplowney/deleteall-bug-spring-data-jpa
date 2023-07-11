package deleteAllBug.repository;

import deleteAllBug.domain.Form;
import org.springframework.data.repository.CrudRepository;

public interface FormRepository extends CrudRepository<Form, Long> {
}
