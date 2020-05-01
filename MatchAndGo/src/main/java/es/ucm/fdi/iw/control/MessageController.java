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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
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
import org.springframework.web.bind.annotation.ResponseBody;
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
        User usuario = (User) session.getAttribute("u");
        usuario =  entityManager.find(User.class, usuario.getId());
        
        // The contacts of the user
        List<User> contacts = new ArrayList<User>();
        for (int i = 0; i < usuario.getSentMessages().size(); ++i) {
            User user = usuario.getSentMessages().get(i).getReceiver();
            if (!contacts.contains(user)) {
                contacts.add(user);
            }
        }

        // People we have received messages from
        // TODO: this has O(n²) runtime due to linear checks for "contains"
        //       see above for a more efficient (O(n)) implementation
        for (int i = 0; i < usuario.getReceivedMessages().size(); ++i) {
            User user = usuario.getReceivedMessages().get(i).getSender();
            if (!contacts.contains(user)) {
                contacts.add(user);
            }
        }
        return contacts;
    }

    /*
     * Shows all the contacts of the user but doesn't start a chat
     */
    @GetMapping("/messages")
    @Transactional
    public String startMessagesUser(Model model, HttpSession session) {
        List<User> contacts = getContactsFromUser(session);
        model.addAttribute("contactos", contacts);
        model.addAttribute("mensajes", new ArrayList<Message> ());
        return "mensajes";
    }
    
    /*
     * Shows the chat between the user and his contact
     */
    @GetMapping("/messages/{id}")
    @Transactional
    @ResponseBody
    public List<Message.Transfer> getMessagesUser(@PathVariable long id, Model model, HttpSession session) {

        log.info("Preparando los mensajes del usuario con su contacto " + id + ".");
        User contact = null;
        try {
            contact = entityManager.find(User.class, id);
        } catch (Exception e) {
            log.info("Error al buscar el contacto del usuario:");
            log.info("  - idContacto: {}", id);
            log.info("  - error: {}", e.getMessage());
        }
        User usuario = (User) session.getAttribute("u");
        usuario =  entityManager.find(User.class, usuario.getId());

        // The messages between the contact and the user
        List<Message> mensajes = new ArrayList<Message> (entityManager.createNamedQuery("Message.getListMessages")
            .setParameter("sender", usuario.getId()).setParameter("receiver", id).executeUpdate());

        log.info("Preparando los transfer de los mensajes.");
        List<Message.Transfer> messagesT = new ArrayList<Message.Transfer> ();

        for (int i = 0; i < mensajes.size(); ++i) {
            messagesT.add(new Message.Transfer(mensajes.get(i)));
        }

        log.warn(messagesT);

        log.info("Enviando los transfer al cliente.");
        return messagesT;
    }
    // Para Post: @RequestParam long id

    /*
     * Shows the chat between the user and his contact
     */
    /*
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

        // The messages between the contact and the user
        List<Message> messages = getMessagesFromContact(session, contact);
        List<User> contacts = getContactsFromUser(session);
        model.addAttribute("contactos", contacts);
        model.addAttribute("mensajes", messages);
        model.addAttribute("contacto", contact);

        return "mensajes";
    }
    // Para Post: @RequestParam long id
    */
}