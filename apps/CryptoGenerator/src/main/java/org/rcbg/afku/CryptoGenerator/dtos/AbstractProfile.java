package org.rcbg.afku.CryptoGenerator.dtos;

public abstract class AbstractProfile {
    private int id;
    private String name;
    private String creationTimestamp;
    private String creatorUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreationTimestamp() {
        return creationTimestamp;
    }

    public void setCreationTimestamp(String creationTimestamp) {
        this.creationTimestamp = creationTimestamp;
    }

    public String getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(String creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    @Override
    public String toString(){
        return String.format("ProfileID: %d, ProfileName: %s, CreationTimestamp: %s, CreatorID: %s", this.id, this.name, this.creationTimestamp, this.creatorUserId);
    }
}
