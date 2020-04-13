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
import javax.persistence.EntityManagerFactory;
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

import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.User;
import es.ucm.fdi.iw.model.User.Role;

/**
 * Message controller
 * @author EnriqueTorrijos
 */
@Controller
public class MessageController {

    private static final Logger log = LogManager.getLogger(MessageController.class);
	
	@Autowired
    private EntityManager entityManager;
    
    /*
     * This method gets all the contacts from the user in the session.
     */
    private List<User> getContactsFromUser (HttpSession session) {
        User usuario = (User) session.getAttribute("user");

        // The contacts of the user
        List<User> contacts = new ArrayList<User>();
        for (int i = 0; i < usuario.getSentMessages().size(); ++i) {
            User user = usuario.getSentMessages().get(i).getSender();
            if (!contacts.contains(user)) {
                contacts.add(user);
            }
        }

        for (int i = 0; i < usuario.getReceivedMessages().size(); ++i) {
            User user = usuario.getReceivedMessages().get(i).getReceiver();
            if (!contacts.contains(user)) {
                contacts.add(user);
            }
        }
        return contacts;
    }

    /*
     * This method gets all the messages between the user in the session and his contact "contact".
     */
    private List<Message> getMessagesFromContact(HttpSession session, User contact) {
        User usuario = (User) session.getAttribute("user");
        List<Message> messages = new ArrayList<Message>();

        for (int i = 0; i < usuario.getSentMessages().size(); ++i) {
            if (contact.getId() == usuario.getId()) {
                messages.add(usuario.getSentMessages().get(i));
            }
        }

        for (int i = 0; i < usuario.getReceivedMessages().size(); ++i) {
            if (contact.getId() == usuario.getId()) {
                messages.add(usuario.getSentMessages().get(i));
            }
        }

        messages = orderMessagesByDate(messages);

        return messages;
    }

    /*
     * This method order the messages by their date.
     * NOT IMPLEMENTED YET.
     */
    private List<Message> orderMessagesByDate(List<Message> messages) {
        return messages;
    }

    @GetMapping("/messages/{id}")
    @Transactional
    public String getMessagesUser(@PathVariable long id, Model model, HttpSession session) {

        User contact = null;
        try {
            contact = entityManager.find(User.class, id);
        } catch (Exception e) {
            log.info("Error al buscar el contacto del usuario:");
            log.info("  - idContacto: {}", id);
            log.info("  - error: {}", e.getMessage());
        }

        // The contacts of the user
        List<User> contacts = getContactsFromUser(session);

        // The messages between the contact and the user
        List<Message> messages = getMessagesFromContact(session, contact);
        
        model.addAttribute("contactos", contacts);
        model.addAttribute("mensajes", messages);
        model.addAttribute("usuario", (User) session.getAttribute("user"));
        model.addAttribute("contacto", contact);

        return "mensajes";
    }
    // Para Post: @RequestParam long id
}