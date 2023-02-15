package api.util;

import api.model.User;

public class UserEmail {
    private String email;

    public UserEmail () {}

    public UserEmail(String email) {
        this.email = email;
    }

    public static UserEmail from(User user) {
        return new UserEmail(user.getEmail());
    }
}
