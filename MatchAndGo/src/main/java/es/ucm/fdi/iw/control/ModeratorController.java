package es.ucm.fdi.iw.control;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Moderator controller
 * 
 * @author Gasan Nazer
 */
@Controller()
@RequestMapping("moderator")
public class ModeratorController {
    private static final Logger log = LogManager.getLogger(ModeratorController.class);
}