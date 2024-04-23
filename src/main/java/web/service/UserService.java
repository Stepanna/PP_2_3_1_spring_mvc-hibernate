package web.service;

import web.model.User;

import java.util.List;

public interface UserService {
    List<User> listUsers();
    void saveUser(User user);
    User getUser(long id);
    void updateUser(User user);
    void deleteUser(long id);
}
