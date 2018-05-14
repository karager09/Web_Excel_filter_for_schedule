package server.security;

import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import parser.FilesController;
import java.io.IOException;


@Service
public class UserService implements UserDetailsService {


    private static final String ROLE = "ADMIN";

    @Override
    /**
     * wczytanie uzytkoniwka do konfiguracji serwera
     */
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user =
                null;
        try {
//            if(username.equals(MASTER)){
//                user = User.withUsername(MASTER)
//                        .password(FilesController.getMasterPassword())
//                        .roles(ROLE)
//                        .build();
            /*}else*/
            if(FilesController.checkUserName(username)){
                user = User.withUsername(username)
                        .password(FilesController.getPassword(username))
                        .roles(ROLE)
                        .build();
            }
            else throw new UsernameNotFoundException("Wrong username!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return user;
    }


    public void updatePassword(String userName, String password) throws IOException {
        FilesController.setPassword(userName, password);
    }
}
