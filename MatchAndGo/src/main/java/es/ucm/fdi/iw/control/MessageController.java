package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import es.ucm.fdi.iw.LocalData;
import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

public class MessageController {

    private static final Logger log = LogManager.getLogger(MessageController.class);
	
	@Autowired
    private EntityManager entityManager;
    
    /*
        This method gets all the contacts from the user in the session.
    * */
    private List<User> getContactsFromUser (HttpSession session) {
        User usuario = (User) session.getAttribute("user");

        // The contacts of the user
        List<User> contacts = new ArrayList<User>();
        for (int i = 0; i < usuario.getSent().size(); ++i) {
            User user = usuario.getSent().get(i).getSender();
            if (!contacts.contains(user)) {
                contacts.add(user);
            }
        }

        for (int i = 0; i < usuario.getReceived().size(); ++i) {
            User user = usuario.getReceived().get(i).getRecipient();
            if (!contacts.contains(user)) {
                contacts.add(user);
            }
        }
        return contacts;
    }

    /*
        This method gets all the messages between the user in the session and his contact "contact".
    * */
    private List<Message> getMessagesFromContact(HttpSession session, User contact) {
        User usuario = (User) session.getAttribute("user");
        List<Message> messages = new ArrayList<Message>();

        for (int i = 0; i < usuario.getSent().size(); ++i) {
            if (contact.getId() == usuario.getId()) {
                messages.add(usuario.getSent().get(i));
            }
        }

        for (int i = 0; i < usuario.getReceived().size(); ++i) {
            if (contact.getId() == usuario.getId()) {
                messages.add(usuario.getSent().get(i));
            }
        }

        return messages;
    }

    @GetMapping("/messages/{id}")
    public String getMessagesUser(Model model, HttpSession session) {

        // User target = entityManager.find(User.class, id);

        // model.addAttribute("users", entityManager.createQuery(
		//      "SELECT u FROM User u").getResultList());

        User usuario = (User) session.getAttribute("user");

        usuario.getSent();

        // The contacts of the user
        List<User> contacts = getContactsFromUser(session);



        // List<Message> messages = getMessagesFromContact(session, );
        
        return "mensajes";
    }
}