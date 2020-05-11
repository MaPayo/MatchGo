package es.ucm.fdi.iw.control;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import org.springframework.web.bind.annotation.RequestBody;
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
     * Sends a message to another user.
     * 
     * Normally, a Message.Transfer.sender is the "username" of the user, in this case we use it
     * to save the "Id" so we can save it in the Database.
     */
    @PostMapping(value = "/messages/addMessage")
    @Transactional
    public String sendMessage(@RequestBody final Message.Transfer message, Model model, HttpSession session) {
        
        log.info("El server tiene un mensaje:");
        log.info("  - Contenido: " + message.getTextMessage());
        log.info("  - Sender_id: " + message.getSender());
        log.info("  - Receiver_id: " + message.getReceiver());

        User sender = entityManager.find(User.class, message.getSender());
        User receiver = entityManager.find(User.class, message.getReceiver());

        Message newMessage = new Message(message.getTextMessage(), sender, receiver, LocalDateTime.now());

        log.info("Guardando el mensaje en la BBDD.");

        entityManager.merge(newMessage);
        entityManager.flush();

        log.info("Volviendo a la página de mensajes.");
        return "mensajes";
    }

    /*
     * Shows all the contacts of the user but doesn't start a chat
     */
    @GetMapping("/messages")
    @Transactional
    public String startMessagesUser(Model model, HttpSession session) {
        // Get the user from the session
        User usuario = (User) session.getAttribute("u");
        usuario =  entityManager.find(User.class, usuario.getId());

        // Get all the contacts the user-session sended a message
        List<User> contacts = new ArrayList (entityManager.createNamedQuery("Message.getSendedUsers")
            .setParameter("sender", usuario.getId()).getResultList());
        
        // Get all the contacts who sended a message to user-session
        List<User> contactsR = new ArrayList (entityManager.createNamedQuery("Message.getReceivedUsers")
            .setParameter("receiver", usuario.getId()).getResultList());
        
        // Join both list
        for (User c : contactsR) {
            if (!contacts.contains(c)) {
                contacts.add(c);
            }
        }

        log.info("Tenemos los contactos del usuario.");
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
        
        try {
            User contact = entityManager.find(User.class, id);
            session.setAttribute("contact", contact);
            //model.addAttribute("contacto", contact);
        } catch (Exception e) {
            log.info("Error al buscar el contacto del usuario:");
            log.info("  - idContacto: {}", id);
            log.info("  - error: {}", e.getMessage());
        }
        User usuario = (User) session.getAttribute("u");
        usuario =  entityManager.find(User.class, usuario.getId());

        // The messages between the contact and the user
        List<Message> mensajes = new ArrayList (entityManager.createNamedQuery("Message.getListMessages")
            .setParameter("sender", usuario.getId()).setParameter("receiver", id).getResultList());

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
}