package server;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import parser.FilesController;
import java.io.IOException;


@Service
public class UserService implements UserDetailsService {


    private static final String USERNAME = "admin";
    private static final String ROLE = "ADMIN";

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user =
                null;
        try {
            user = User.withUsername(USERNAME)
                    .password(FilesController.getPassword())
                    .roles(ROLE)
                    .build();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }


    public void updatePassword(String password) {
        FilesController.setPassword(password);
    }
}
