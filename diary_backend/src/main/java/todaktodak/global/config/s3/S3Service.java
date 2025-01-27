package todaktodak.global.config.s3;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@Service
public class S3Service {
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3 amazonS3;


    public String uploadFile(MultipartFile file, S3SaveDir s3SaveDir, String s3FileName) throws IOException {
        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentLength(file.getInputStream().available());
        String bucketName = bucket + s3SaveDir.path;
        amazonS3.putObject(new PutObjectRequest(bucketName, s3FileName, file.getInputStream(),objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3.getUrl(bucketName, s3FileName).toString();
    }

    public void deleteFile(String url, S3SaveDir s3SaveDir) {
        String bucketName = bucket + s3SaveDir.path;
        amazonS3.deleteObject(new DeleteObjectRequest(bucketName, url));
    }



}
