package br.com.argos.interfaces;

import br.com.argos.model.User;
import java.util.UUID;

public interface IUserDAO extends GenericDAO<User, UUID> {
     User findByEmail(String email);
     void updatePassword(UUID id, String password);
}
