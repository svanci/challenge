package svancar.hoval.challenge.rest.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class ProfileDto {

    @NotNull(message = "First name cannot be null")
    @Size(min = 1, max = 16, message = "First name should contain max of 16 characters")
    private String firstName;

    @NotNull(message = "Last name cannot be null")
    @Size(min = 1, max = 16, message = "Last name should contain max of 16 characters")
    private String lastName;

    @NotNull(message = "Occupation cannot be null")
    @Size(max = 30, message = "Occupation field should contain max of 30 characters")
    private String occupation;

    @NotNull(message = "LinkedIn URL cannot be null")
    @Size(max = 50, message = "LinkedIn URL should contain max of 50 characters")
    private String linkedIn;

    @NotNull(message = "Description cannot be null")
    @Size(max = 255, message = "Description should contain max of 255 characters")
    private String description;

    @Size(max = 255, message = "Profile image path should contain max of 255 characters")
    private String profileImagePath;

    public ProfileDto() {
    }

    public ProfileDto(String firstName, String lastName, String occupation, String linkedIn, String description, String profileImagePath) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.occupation = occupation;
        this.linkedIn = linkedIn;
        this.description = description;
        this.profileImagePath = profileImagePath;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public void setProfileImagePath(String profileImagePath) {
        this.profileImagePath = profileImagePath;
    }

    @Override
    public String toString() {
        return "ProfileDto{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", occupation='" + occupation + '\'' +
                ", linkedIn='" + linkedIn + '\'' +
                ", description='" + description + '\'' +
                ", profileImagePath='" + profileImagePath + '\'' +
                '}';
    }
}
