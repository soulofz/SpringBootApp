package by.tms.exception;

import by.tms.model.User;

public class UserNotFoundException extends RuntimeException {
    private final User user;

    public UserNotFoundException(User user) {
        super();
        this.user = user;
    }

    @Override
    public String toString(){
        return "UserNotFoundException {" + user.toString() + "}";
    }
}
