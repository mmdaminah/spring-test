package ir.mmdaminah.newtest.file;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "files")
@Setter
@Getter
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "object_name")
    private String objectName;

    private String bucket;

    @Column(name = "content_hash")
    private String contentHash;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "original_file_name")
    private String originalFileName;

    private Long size;

    @Column(name = "number_of_references")
    private Integer numberOfReferences;

}
