package services;

/** Used to manage user accounts.
 */
public interface AccountService {

    void createUser(String email, String password, String name);

}
