package svancar.hoval.challenge.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import svancar.hoval.challenge.bucket.BucketName;
import svancar.hoval.challenge.exception.exceptions.NotFoundException;
import svancar.hoval.challenge.model.Profile;
import svancar.hoval.challenge.repository.ProfileRepository;
import svancar.hoval.challenge.service.ProfileService;
import svancar.hoval.challenge.utils.FileUtils;

import java.io.IOException;
import java.util.*;

/**
 * Class handles all operations regarding user's profile
 */
@Service
public class ProfileServiceImpl implements ProfileService {

    private final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private ProfileRepository profileRepository;

    private FileStorageServiceImpl fileStoreService;

    @Autowired
    public ProfileServiceImpl(ProfileRepository profileRepository, FileStorageServiceImpl fileStoreService) {
        this.profileRepository = profileRepository;
        this.fileStoreService = fileStoreService;
    }

    /**
     * Takes validated Profile object and store it
     * @param profile
     * @return stored Profile object
     */
    public Profile save(Profile profile) {
        return profileRepository.save(profile);
    }


    /**
     * Method takes file for upload with profile ID. File is validate first - if its not empty or if its in
     * required format. Then creates some additional meta data, build a path (bucketName/profileID)
     * and file name (originalFileName-UUID) and tries to store it into S3.
     * @param id
     * @param file
     */
    public void uploadUserProfilePicture(long id, MultipartFile file) {
        logger.info("Received file for user with ID - {}" , id);

        FileUtils.ensureFileNotEmpty(file);

        FileUtils.ensureFileTypeIsValid(file);

        Profile profile = getProfile(id);

        // Add some additional meta data
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));

        // Create a target location path based on bucket name and profile ID
        String path = getFilePathForS3(profile);
        // Create a file name based on original file name and random UUID
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());

        try {
            // Try to store file into S3 and save this new file in DB
            fileStoreService.save(path, fileName, metadata, file.getInputStream());
            profile.setProfileImagePath(fileName);
            profileRepository.save(profile);
        } catch (IOException e) {
            logger.error("Error during saving a file to S3", e);
            throw new IllegalStateException(e);
        }
    }

    /**
     * Download the file from S3 if profile with given ID exists.
     * @param id
     * @return File from S3
     */
    public byte[] downloadUserProfilePicture(long id) {
        Profile profile = getProfile(id);
        String path = getFilePathForS3(profile);

        // Get a file from bucket or return an empty file
        return profile.getProfileImagePath()
                .map(key -> fileStoreService.load(path, key))
                .orElse(new byte[0]);
    }

    /**
     * // Create a target location path based on bucket name and profile ID
     * @param profile
     * @return
     */
    public String getFilePathForS3(Profile profile) {
        return String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                profile.getId());
    }

    /**
     * Validates if profile with given ID exists. If it doesn't it throws ProfileNotFoundException
     * @param id
     * @return Found profile
     */
    public Profile getProfile(long id) {
        return profileRepository.findById(id).orElseThrow(() -> new NotFoundException("Profile with given ID not found. ID - " + id));
    }

}
