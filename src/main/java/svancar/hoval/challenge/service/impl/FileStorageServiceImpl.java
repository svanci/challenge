package svancar.hoval.challenge.service.impl;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import svancar.hoval.challenge.service.FileStorageService;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Service handles all operations with files
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final AmazonS3 s3;

    @Autowired
    public FileStorageServiceImpl(AmazonS3 s3) {
        this.s3 = s3;
    }

    /**
     * Method takes validated file, creates meta data and add optional meta data and put file to S3 to given bucket
     * @param path
     * @param fileName
     * @param optionalMetaData
     * @param inputStream
     */
    public void save(String path, String fileName, Map<String, String> optionalMetaData,
                     InputStream inputStream) {
        ObjectMetadata metaData = new ObjectMetadata();
        if(optionalMetaData != null) {
            optionalMetaData.forEach(metaData::addUserMetadata);
        }

        try{
            byte[] bytes = IOUtils.toByteArray(inputStream);
            metaData.setContentLength(bytes.length);
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
            PutObjectRequest putObjectRequest = new PutObjectRequest(path, fileName, byteArrayInputStream, metaData);
            s3.putObject(putObjectRequest);
        } catch (AmazonServiceException | IOException e) {
            throw new IllegalStateException("Failed to store file to s3", e);
        }
    }

    /**
     * Method gets the file from bucket (if exists) and returns as array of bytes
     * @param path
     * @param key
     * @return File from bucket
     */
    public byte[] load(String path, String key) {
        try{
            S3Object object = s3.getObject(path, key);
            return IOUtils.toByteArray(object.getObjectContent());
        } catch (AmazonServiceException | IOException e ) {
            throw new IllegalStateException("Failed to download!", e);
        }
    }
}
