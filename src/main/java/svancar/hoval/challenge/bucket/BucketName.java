package svancar.hoval.challenge.bucket;

/**
 * Class stores bucket names
 */
public enum BucketName {

    PROFILE_IMAGE("svancar-challenge");

    private final String bucketName;

    BucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    public String getBucketName() {
        return bucketName;
    }
}
