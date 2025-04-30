package modelo;

import java.io.Serializable;

public abstract class Mensaje implements Serializable {
    private static final long serialVersionUID = 1L; // Esto es para la serialización
    private int codigo;
    private String texto;

    public Mensaje(int codigo, String texto) {
        this.codigo = codigo;
        this.texto = texto;
    }

//Getters y Setters esenciales

    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    } // Necesario para encreiptar y desencriptar

    @Override
    public String toString() {  // estaba como "toStrig"
        return "Código: " + codigo + ", Texto: " + texto;
    }
}