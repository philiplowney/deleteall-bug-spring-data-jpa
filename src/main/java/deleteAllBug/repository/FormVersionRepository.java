package deleteAllBug.repository;

import deleteAllBug.domain.FormVersion;
import deleteAllBug.domain.FormVersionId;
import org.springframework.data.repository.CrudRepository;

public interface FormVersionRepository extends CrudRepository<FormVersion, FormVersionId> {

}
