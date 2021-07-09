package app.springboot.service;

import app.springboot.dao.UserDao;
import app.springboot.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService{
    private final UserDao userDao;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserDao userDao, BCryptPasswordEncoder passwordEncoder) {
        this.userDao = userDao;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public void addUser(User user) {
        user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        userDao.addUser(user);
    }

    @Override
    public void removeUserById(Long id) {
        userDao.removeUserById(id);
    }

    @Override
    public void updateUser(User user) {
        User oldUser = userDao.getUserById(user.getUserId());
        if(!oldUser.getUserPassword().equals(user.getUserPassword())) {
            user.setUserPassword(passwordEncoder.encode(user.getUserPassword()));
        }
        userDao.updateUser(user);
    }

    @Override
    public User getUserById(Long id) {
        return userDao.getUserById(id);
    }

    @Override
    public User getUserByIdWithRoles(Long id) {
        return userDao.getUserByIdWithRoles(id);
    }

    @Override
    public User getUserByNameWithRoles(String name) {
        return userDao.getUserByNameWithRoles(name);
    }

    @Override
    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }
}
