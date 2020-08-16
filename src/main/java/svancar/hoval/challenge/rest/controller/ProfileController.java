package svancar.hoval.challenge.rest.controller;

import org.modelmapper.ModelMapper;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import svancar.hoval.challenge.exception.exceptions.ValidationException;
import svancar.hoval.challenge.model.Profile;
import svancar.hoval.challenge.rest.dto.ProfileDto;
import svancar.hoval.challenge.service.impl.ProfileServiceImpl;

import javax.validation.Valid;

/**
 * Rest controller handles all operations regarding user's profile
 */
@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final Logger logger = LoggerFactory.getLogger(ProfileController.class);

    private ProfileServiceImpl profileService;
    private ModelMapper modelMapper;

    @Autowired
    public ProfileController(ProfileServiceImpl profileService, ModelMapper modelMapper) {
        this.profileService = profileService;
        this.modelMapper = modelMapper;
    }

    /**
     * Get profile based on ID
     * @param id
     * @return Profile entity
     */
    @GetMapping("{id}")
    public Profile get(@PathVariable long id) {
        Profile profile =  profileService.getProfile(id);
        logger.info("Getting profile: {}", profile);
        return profile;
    }

    /**
     * Update profile with given ID with provided values in body
     * @param id
     * @param profileDto
     * @param bindingResult
     * @return Updated profile
     */
    @PutMapping("/{id}")
    public Profile update(@PathVariable long id, @Valid @RequestBody ProfileDto profileDto, BindingResult bindingResult) {
        logger.info("Received profile to update: {}", profileDto);
        if(bindingResult.hasErrors()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "Validation unsuccessful", bindingResult.getAllErrors());
        }

        // Map DTO object to entity
        Profile profile = modelMapper.map(profileDto, Profile.class);
        profile.setId(id);

        Profile result = profileService.save(profile);
        logger.info("Update successful, sending the result: {}", result);
        return result;
    }

    /**
     * Upload provided profile picture for given profile to Amazon S3
     * @param id
     * @param file
     */
    @PostMapping(
            path = "{id}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadImage(@PathVariable("id") long id, @RequestParam("file") MultipartFile file) {
        profileService.uploadUserProfilePicture(id, file);
    }

    /**
     * Get profile picture for given profile ID
     * @param id
     * @return Profile picture
     */
    @GetMapping("{id}/image")
    public byte[] downloadImage(@PathVariable("id") long id) {
        return profileService.downloadUserProfilePicture(id);
    }
}
