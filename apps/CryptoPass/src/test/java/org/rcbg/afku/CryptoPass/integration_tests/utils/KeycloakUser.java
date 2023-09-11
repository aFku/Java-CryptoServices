package org.rcbg.afku.CryptoPass.integration_tests.utils;

public enum KeycloakUser {
    PASSUSER1("5298afc5-574c-485f-ae7a-709aef7856f1"), // Generators + Storage
    PASSUSER2("884f1246-f7db-49eb-9a26-4d18ffbf03cb"), // Storage
    PASSUSER3("11555e7f-9da4-46fb-9b00-753b10dbadc5"); // Nothing

    private final String userId;

    KeycloakUser(String userId){
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
