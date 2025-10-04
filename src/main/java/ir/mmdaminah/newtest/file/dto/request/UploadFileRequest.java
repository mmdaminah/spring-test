package ir.mmdaminah.newtest.file.dto.request;

import ir.mmdaminah.newtest.file.dto.validation.ValidBucket;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UploadFileRequest {

    @NotNull
    @ValidBucket
    private String bucket;

}
