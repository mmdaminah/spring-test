package ir.mmdaminah.newtest.file;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {

    Optional<File> findByContentHash(String contentHash);

}
