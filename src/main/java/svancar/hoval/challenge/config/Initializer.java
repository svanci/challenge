package svancar.hoval.challenge.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import svancar.hoval.challenge.model.Profile;
import svancar.hoval.challenge.repository.ProfileRepository;

/**
 * Initializer class is only for demo purposes. In case of real DB this class can be removed.
 */
@Component
public class Initializer implements CommandLineRunner {

    private final ProfileRepository profileRepository;

    @Autowired
    public Initializer(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        String desc = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Curabitur accumsan tortor elit, a scelerisque nibh molestie eu. In pellentesque dictum sapien, eu pretium leo posuere a. Donec at accumsan orci, nec dignissim tortor.";
        String url = "www.linkedin.com/in/msvancar";
        profileRepository.save(new Profile("Martin", "Svancar", desc, "Java Developer", url, null));
    }
}
