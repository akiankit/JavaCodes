package com.bluestone.app.account;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.bluestone.app.account.dao.UserDao;
import com.bluestone.app.account.model.User;

@Service
@Transactional(propagation = Propagation.REQUIRED, readOnly = true)
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserDao userDao;


    public User getUserByEmailId(String email) {
        log.debug("UserService.getUserByEmailId() for [{}]", email);
        User existingUser = userDao.getUserFromEmail(email);
        return existingUser;
    }
}
