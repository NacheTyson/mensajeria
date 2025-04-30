package modelo;

public class MensajeNormal extends Mensaje {
    private static final long serialVersionUID = 2L;
    public MensajeNormal(int codigo, String texto) {
        super(codigo, texto);
    }
    // No necesitamos métodos adicionales, hereda de toString()
}