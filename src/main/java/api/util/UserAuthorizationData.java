package api.util;

import api.model.User;

public class UserAuthorizationData {
    private String email;
    private String password;

    public UserAuthorizationData () {}

    public UserAuthorizationData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserAuthorizationData from(User user) {
        return new UserAuthorizationData(user.getEmail(), user.getPassword());
    }
}
