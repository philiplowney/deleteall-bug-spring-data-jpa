package deleteAllBug;

import deleteAllBug.domain.Form;
import deleteAllBug.domain.FormVersion;
import deleteAllBug.repository.FormRepository;
import deleteAllBug.repository.FormVersionRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/META-INF/spring/context.xml")
@TestPropertySource(locations = "classpath:META-INF/spring/database.properties")
public class TestCase {
    @Autowired
    private FormVersionRepository formVersionRepository;

    @Autowired
    private FormRepository formRepository;

    @Test
    public void deleteInviduallyTest() {
        Form aForm = new Form();
        formRepository.save(aForm);

        FormVersion formVersion = new FormVersion(aForm, 1);
        formVersionRepository.save(formVersion);

        FormVersion formVersion2 = new FormVersion(aForm, 2);
        formVersionRepository.save(formVersion2);

        Iterable<FormVersion> allVersions = formVersionRepository.findAll();
        allVersions.forEach(version->{
            formVersionRepository.delete(version);
        });
    }

    @Test
    public void deleteAllTest() {
        Form aForm = new Form();
        formRepository.save(aForm);

        FormVersion formVersion = new FormVersion(aForm, 1);
        formVersionRepository.save(formVersion);

        FormVersion formVersion2 = new FormVersion(aForm, 2);
        formVersionRepository.save(formVersion2);

        formVersionRepository.deleteAll(); // <-- This will fail
    }

}
