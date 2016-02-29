package com.tsystems.javaschool.webshop.services.impl;

import com.tsystems.javaschool.webshop.dao.api.UsersDAO;
import com.tsystems.javaschool.webshop.dao.entities.UserEntity;
import com.tsystems.javaschool.webshop.dao.impl.UsersDAOImpl;
import com.tsystems.javaschool.webshop.services.api.AccountService;
import com.tsystems.javaschool.webshop.services.exceptions.AccountServiceException;
import com.tsystems.javaschool.webshop.services.util.ServiceHelper;
import com.tsystems.javaschool.webshop.services.util.ServiceHelperImpl;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * The type Account service.
 */
public class AccountServiceImpl implements AccountService {

    /**
     * The constant LOGGER.
     */
    private static final Logger LOGGER =
            LogManager.getLogger(AccountService.class);

    /**
     * The Users dao.
     */
    private UsersDAO usersDAO;

    /**
     * The Service helper.
     */
    private ServiceHelper serviceHelper;

    /**
     * Instantiates a new Account service.
     */
    public AccountServiceImpl() {
        this.usersDAO = new UsersDAOImpl();
        this.serviceHelper = new ServiceHelperImpl(LOGGER);
    }

    @Override
    public final UserEntity signUpUser(final String name,
                                       final String lastName,
                                       final String email,
                                       final String password)
            throws AccountServiceException {
        return serviceHelper.loadInTransaction(manager -> {
            //TODO: validate email, hash password
            if (usersDAO.getUserByEmail(email, manager) != null) {
                throw new AccountServiceException("email already registered");
            }
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setPassword(password);
            newUser.setName(name);
            newUser.setLastName(lastName);
            newUser.setIsAdmin(false);

            usersDAO.create(newUser, manager);
            return newUser;
        });


    }


    @Override
    public final UserEntity signInUser(final String email,
                                       final String password)
            throws AccountServiceException {
        return serviceHelper.loadInTransaction(manager -> {
            UserEntity user = usersDAO.getUserByEmail(email, manager);

            if (user == null) {
                throw new AccountServiceException(
                        "User with this email not found");
            }
            //TODO: hash password

            if (!user.getPassword().equals(password)) {
                throw new AccountServiceException("Wrong password");
            }
            return user;
        });

    }
    @Override
    public final UserEntity getUser(final int userID) {
        return serviceHelper.load(manager -> {
            return usersDAO.getById(userID, manager);
        });
    }
    @Override
    public final UserEntity saveProfile(final UserEntity user) {
        return serviceHelper.loadInTransaction(manager -> {
                usersDAO.update(user, manager);
                return user;
        });
    }
}