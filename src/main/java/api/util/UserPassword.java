package api.util;

import api.model.User;

public class UserPassword {
    private String password;

    public UserPassword () {}

    public UserPassword(String password) {
        this.password = password;
    }

    public static UserPassword from(User user) {
        return new UserPassword(user.getPassword());
    }
}
