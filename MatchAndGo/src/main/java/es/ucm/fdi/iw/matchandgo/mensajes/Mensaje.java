package es.ucm.fdi.iw.matchandgo.mensajes;

// CLASE AUXILIAR PARA LA PANTALLA "mensajes"
public class Mensaje {
    private String contenido;
    private String sender; // La persona que lo envía

    public Mensaje(String c, String s) {
        this.contenido = c;
        this.sender = s;
    }

    /**
     * @return the contenido
     */
    public String getContenido() {
        return contenido;
    }

    /**
     * @return the sender
     */
    public String getSender() {
        return sender;
    }
}