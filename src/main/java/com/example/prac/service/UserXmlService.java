package com.example.prac.service;

import com.example.prac.data.auth.User;
import com.example.prac.data.auth.Users;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserXmlService {
    private final String XML_FILE_PATH = "/Users/mihailagei/Documents/ITMO/TPO/blps/src/main/resources/users.xml";

    public List<User> loadUsers() throws JAXBException {
        File file = new File(XML_FILE_PATH);
        if (!file.exists()) {
            return new ArrayList<>();
        }
        JAXBContext context = JAXBContext.newInstance(Users.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Users users = (Users) unmarshaller.unmarshal(file);
        return users.getUsers();
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
