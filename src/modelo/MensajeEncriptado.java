package modelo;

public class MensajeEncriptado extends Mensaje implements IEncriptable {
    private static final long serialVersionUID = 3L;
    // Constructor de encreptación al crear
    public MensajeEncriptado(int codigo, String textoOriginal) {
        super(codigo, ""); // iniciamos vacío o con el mensaje original temporalmente
        setTexto(textoOriginal); //Guardamos el mensaje original
        encriptar(); // Llama a encriptar para que el texto alamcenado sea encriptado
    }

    @Override
    public void encriptar(){
        String textoOriginal=getTexto();
        StringBuilder textoEncriptado=new StringBuilder();
        for(char c: textoOriginal.toCharArray()){
            textoEncriptado.append((char)(c+1));
        }
        setTexto(textoEncriptado.toString());//sobreescribimos el texto original con el encriptado
    }

    @Override
    public String desencriptar(){
        String textoEncriptado = getTexto();
        StringBuilder textoOriginal= new StringBuilder();
        for (char c: textoEncriptado.toCharArray()){
            textoOriginal.append((char)(c-1));
        }
        return textoOriginal.toString();
    }
// sobreescribimos toString para indicar que es un mensaje encriptado
    @Override
    public String toString() {
        return "Código: " + getCodigo() + ", Texto (Encriptado): " + getTexto();
    }
}