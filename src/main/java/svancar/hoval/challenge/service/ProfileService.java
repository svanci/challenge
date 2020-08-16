package svancar.hoval.challenge.service;

import org.springframework.web.multipart.MultipartFile;
import svancar.hoval.challenge.model.Profile;

public interface ProfileService {

    Profile getProfile(long id);

    Profile save(Profile profile);

    void uploadUserProfilePicture(long id, MultipartFile file);

    byte[] downloadUserProfilePicture(long id);
}
