package com.example.prac.service;

import com.example.prac.data.auth.User;
import com.example.prac.data.auth.Users;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserXmlService {
    private final String XML_FILE_PATH = "./users.xml";

    public List<User> loadUsers() throws JAXBException {
        try (InputStream inputStream = getClass().getResourceAsStream("/users.xml")) {
            if (inputStream == null) {
                throw new IllegalStateException("Файл не найден: /users.xml");
            }

            JAXBContext context = JAXBContext.newInstance(Users.class);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            Users users = (Users) unmarshaller.unmarshal(inputStream);
            return users.getUsers();
        } catch (IOException e) {
            throw new RuntimeException("Ошибка доступа к XML", e);
        }
    }

    public void saveUsers(List<User> users) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(Users.class);
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        Users usersWrapper = new Users();
        usersWrapper.setUsers(users);
        marshaller.marshal(usersWrapper, new File(XML_FILE_PATH));
    }

    public Optional<User> findByUsername(String username) throws JAXBException {
        return loadUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    public void saveUser(User user) throws JAXBException {
        List<User> users = loadUsers();
        users.add(user);
        saveUsers(users);
    }
}
