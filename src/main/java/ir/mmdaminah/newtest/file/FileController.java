package ir.mmdaminah.newtest.file;

import ir.mmdaminah.newtest.file.dto.request.UploadFileRequest;
import ir.mmdaminah.newtest.file.dto.response.FileUploadResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileRepository fileRepository;

    @PostMapping("/upload")
    public FileUploadResponse uploadFile(
            @RequestParam("file") MultipartFile file,
            @Valid @ModelAttribute UploadFileRequest request
            ) {
        return fileService.uploadFile(file, request.getBucket());
    }

    @GetMapping("/download/{bucketName}/{fileName}")
    public void download(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName, HttpServletResponse response) {

        try (InputStream stream = fileService.downloadFile(bucketName, fileName)) {
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
            stream.transferTo(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/view/{bucketName}/{fileName}")
    public void view(
            @PathVariable("bucketName") String bucketName,
            @PathVariable("fileName") String fileName, HttpServletResponse response
    ) {

        try (InputStream stream = fileService.downloadFile(bucketName, fileName)) {
            response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + fileName + "\"");
            stream.transferTo(response.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/list")
    public Page<File> list(Pageable pageable) {
        return fileRepository.findAll(pageable);
    }

}
