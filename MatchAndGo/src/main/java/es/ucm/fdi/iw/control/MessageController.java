package es.ucm.fdi.iw.control;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import es.ucm.fdi.iw.model.Message;
import es.ucm.fdi.iw.model.User;

/**
 * Message controller
 * @author EnriqueTorrijos
 */
@Controller
@RequestMapping("/messages")
public class MessageController {

    private static final Logger log = LogManager.getLogger(MessageController.class);
	
	@Autowired
    private EntityManager entityManager;

    @Autowired
	private SimpMessagingTemplate messagingTemplate;

    /*
     * Shows all the contacts of the user but doesn't start a chat
     */
    @GetMapping("/")
    @Transactional
    public String startMessagesUser(Model model, HttpSession session) {
        // Get the user from the session
        User usuario = (User) session.getAttribute("u");
        usuario =  entityManager.find(User.class, usuario.getId());

        // Get all the contacts the user-session sended a message
        List<User> sentTo = entityManager.createNamedQuery(
            "Message.getSendedUsers", User.class)
                .setParameter("sender", usuario.getId())
                .getResultList();
        
        // Get all the contacts who sended a message to user-session
        List<User> receivedFrom = entityManager.createNamedQuery(
            "Message.getReceivedUsers", User.class)
                .setParameter("receiver", usuario.getId())
                .getResultList();
        
        // Join both list
        Set<User> all = new HashSet<>();
        all.addAll(sentTo);
        all.addAll(receivedFrom);

        log.info("Tenemos los contactos del usuario.");
        model.addAttribute("contactos", all);
        model.addAttribute("mensajes", new ArrayList<Message> ());
        return "mensajes";
    }
    
    /*
     * Get the Messages between the User and his Contact from the Database
     */
    private List<Message.Transfer> getMessagesFromDatabase(long senderId, long receiverId) {
        // The messages between the contact and the user
        List<Message> mensajes = entityManager.createNamedQuery("Message.getListMessages")
            .setParameter("sender", senderId).setParameter("receiver", receiverId).getResultList();

        log.info("Preparando los transfer de los mensajes.");
        List<Message.Transfer> messagesT = new ArrayList<Message.Transfer> ();

        for (int i = 0; i < mensajes.size(); ++i) {
            messagesT.add(new Message.Transfer(mensajes.get(i)));
        }
        return messagesT;
    }

    /*
     * Shows the chat between the user and his contact
     */
    @GetMapping("/{id}")
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
        List<Message.Transfer> messagesT = getMessagesFromDatabase(usuario.getId(), id);

        log.warn(messagesT);

        log.info("Enviando los transfer al cliente.");
        return messagesT;
    }

    /*
     * Sends a message to another user.
     */
    @PostMapping("/addMessage")
    @Transactional
    @ResponseBody
    public Message.Transfer sendMessage(@RequestBody Message.Transfer message, HttpSession session) {
        
        User sender = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
        User receiver = entityManager.find(User.class, Long.parseLong(message.getReceiverId()));

        // Save the message in the system
        Message newMessage = this.saveMessageInDatabase(message, sender, receiver);
        Message.Transfer msg = new Message.Transfer(newMessage);
        
        // We prepare the messages and send them to the user and his contact by WebSocket
        log.info("\nEmpezamos a preparar los mensajes a los usuarios por el WebSocket.");

        messagingTemplate.convertAndSend("/user/"+receiver.getUsername()+"/queue/updates", msg);
        messagingTemplate.convertAndSend("/notification/" + receiver.getId(), msg);

        log.info("\nMensajes enviados por el WebSocket.\n");
        return msg;
    }

    /*
     * A User sends a Message to another User who isn't one of his contacts
     */
    @PostMapping("/sendMessageNewUser")
    @Transactional
    public void sendMessageNewUser(@RequestBody Message.Transfer message, HttpSession session) {

        User sender = entityManager.find(User.class, ((User)session.getAttribute("u")).getId());
        User receiver = entityManager.find(User.class, Long.parseLong(message.getReceiverId()));

        this.saveMessageInDatabase(message, sender, receiver);
    }

    /*
     * Saves a new Message in the database
     */
    private Message saveMessageInDatabase(Message.Transfer message, User sender, User receiver) {
        log.info("\n\n\nEl server tiene un mensaje:");
        log.info("  - Contenido: " + message.getTextMessage());
        log.info("  - Sender_id: " + sender.getId());
        log.info("  - Receiver_id: " + receiver.getId());

        Message newMessage = new Message(message.getTextMessage(), sender, receiver, LocalDateTime.now());

        log.info("Guardando el mensaje {} en la BBDD.", newMessage.toString());

        entityManager.persist(newMessage);
        entityManager.flush();

        log.info("\n\nId del nuevo mensaje: {}", newMessage.getId());
        return newMessage;
    }
}