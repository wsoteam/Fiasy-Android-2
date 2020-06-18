
package com.losing.weight.authHarvester.POJO;

import com.squareup.moshi.Json;

public class ProviderUserInfo {

    @Json(name = "providerId")
    private String providerId;
    @Json(name = "rawId")
    private String rawId;
    @Json(name = "email")
    private String email;
    @Json(name = "displayName")
    private String displayName;
    @Json(name = "photoUrl")
    private String photoUrl;

    /**
     * No args constructor for use in serialization
     * 
     */
    public ProviderUserInfo() {
    }

    /**
     * 
     * @param email
     * @param rawId
     * @param providerId
     * @param photoUrl
     * @param displayName
     */
    public ProviderUserInfo(String providerId, String rawId, String email, String displayName, String photoUrl) {
        super();
        this.providerId = providerId;
        this.rawId = rawId;
        this.email = email;
        this.displayName = displayName;
        this.photoUrl = photoUrl;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getRawId() {
        return rawId;
    }

    public void setRawId(String rawId) {
        this.rawId = rawId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
