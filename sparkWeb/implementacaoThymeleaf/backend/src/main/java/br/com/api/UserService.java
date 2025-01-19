package br.com.api;

import java.util.ArrayList;

public class UserService {
    private ArrayList<User> users = new ArrayList<>();
    private int idCounter = 1;

    public User add(String name, String email) {
        User user = new User(String.valueOf(idCounter++), name, email);
        users.add(user);
        return user;
    }

    public User findById(String id) {
        for (User user : users) {
            if (user.getId().equals(id)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> findAll() {
        return users;
    }

    public void update(String id, String name, String email) {
        User user = findById(id);
        if (user != null) {
            user.setName(name);
            user.setEmail(email);
        }
    }

    public void delete(String id) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(id)) {
                users.remove(i);
                break;
            }
        }
    }
}
