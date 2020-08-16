package svancar.hoval.challenge.model;

import javax.persistence.*;
import java.util.Objects;
import java.util.Optional;


@Entity
@Table(name = "profile")
public class Profile {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "occupation")
    private String occupation;

    @Column(name = "linkedIn")
    private String linkedIn;

    @Column(name = "description")
    private String description;

    @Column(name = "profile_image_url")
    private String profileImagePath;

    public Profile() {
    }

    public Profile(String firstName, String lastName, String description, String occupation, String linkedIn, String imagePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
        this.occupation = occupation;
        this.linkedIn = linkedIn;
        this.profileImagePath = imagePath;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public void setLinkedIn(String linkedIn) {
        this.linkedIn = linkedIn;
    }

    public Optional<String> getProfileImagePath() {
        return Optional.ofNullable(profileImagePath);
    }

    public void setProfileImagePath(String profileImageLink) {
        this.profileImagePath = profileImageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(id, profile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", occupation='" + occupation + '\'' +
                ", linkedIn='" + linkedIn + '\'' +
                ", description='" + description + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                '}';
    }
}
