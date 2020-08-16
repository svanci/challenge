package svancar.hoval.challenge.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;
import svancar.hoval.challenge.exception.exceptions.FileValidationException;
import svancar.hoval.challenge.exception.exceptions.NotFoundException;
import svancar.hoval.challenge.model.Profile;
import svancar.hoval.challenge.repository.ProfileRepository;
import svancar.hoval.challenge.service.impl.FileStorageServiceImpl;
import svancar.hoval.challenge.service.impl.ProfileServiceImpl;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ProfileServiceTest {

    @Mock
    ProfileRepository profileRepository;

    @InjectMocks
    ProfileServiceImpl profileService;

    @Mock
    FileStorageServiceImpl fileStorageService;

    @Test
    public void should_saveProfile_and_returnProfile() {
        final Profile profile = new Profile("Martin", "Svancar", "desc", "Java Developer", "", null);

        given(profileRepository.save(profile)).willReturn(profile);

        final Profile expected = profileService.save(profile);

        assertThat(expected).isNotNull();
        verify(profileRepository).save(any(Profile.class));
    }

    @Test
    public void should_returnProfile_when_profileExists() {
        final Long id = 1L;
        final Profile profile = new Profile("Martin", "Svancar", "desc", "Java Developer", "", null);

        given(profileRepository.findById(id)).willReturn(Optional.of(profile));

        final Profile expected = profileService.getProfile(id);

        assertThat(expected).isNotNull();
    }

    @Test(expected = NotFoundException.class)
    public void should_return404_when_profileNotExists() {
        final Long id = 2L;

        given(profileRepository.findById(id)).willReturn(Optional.empty());
        profileService.getProfile(id);
    }

    @Test(expected = FileValidationException.class)
    public void should_return400_when_fileIsEmpty() {
        final Long id = 1L;
        MultipartFile mockFile = new MockMultipartFile("empty", new byte[0]);
        profileService.uploadUserProfilePicture(id, mockFile);
    }

    @Test(expected = FileValidationException.class)
    public void should_return400_when_fileHasWrongContentType() {
        final Long id = 1L;
        MockMultipartFile jsonFile = new MockMultipartFile("json", "", "application/json", "{\"json\": \"someValue\"}".getBytes());

        profileService.uploadUserProfilePicture(id, jsonFile);
    }

    @Test(expected = NotFoundException.class)
    public void should_return404_when_profileForFileNotExists() {
        final Long id = 2L;
        MockMultipartFile image = new MockMultipartFile("image", "", "image/jpeg", "{\"json\": \"someValue\"}".getBytes());

        given(profileRepository.findById(id)).willReturn(Optional.empty());

        profileService.uploadUserProfilePicture(id, image);
    }

    @Test()
    public void should_upload_when_everythingIsValid() throws IOException {
        final Long id = 1L;
        final Profile profile = new Profile("Martin", "Svancar", "desc", "Java Developer", "", null);
        MockMultipartFile image = new MockMultipartFile("image", "", "image/jpeg", "{\"json\": \"someValue\"}".getBytes());
        given(profileRepository.findById(id)).willReturn(Optional.of(profile));
        doNothing().when(fileStorageService).save(anyString(), anyString(), anyMap(), anyObject());
        profileService.uploadUserProfilePicture(id, image);
    }

    @Test()
    public void should_download_when_everythingIsValid() throws IOException {
        final Long id = 1L;
        final Profile profile = new Profile("Martin", "Svancar", "desc", "Java Developer", "", null);
        final byte[] fileToReturn = "Testing".getBytes();

        given(profileRepository.findById(id)).willReturn(Optional.of(profile));
        given(fileStorageService.load(anyString(), anyString())).willReturn(fileToReturn);

        final byte[] expected = profileService.downloadUserProfilePicture(id);

        assertThat(expected.length > 0);
    }

}
