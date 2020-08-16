package svancar.hoval.challenge.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import svancar.hoval.challenge.model.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {
}
