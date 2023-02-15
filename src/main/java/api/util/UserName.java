package api.util;

import api.model.User;

public class UserName {
    private String name;

    public UserName () {}

    public UserName(String name) {
        this.name = name;
    }

    public static UserName from(User user) {
        return new UserName(user.getName());
    }
}
