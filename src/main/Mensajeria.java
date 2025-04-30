package main;

import modelo.*; // Importar todo del paquete modelo

import java.io.*;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.Iterator; // Para eliminar de forma segura mientras se itera si fuera necesario

public class Mensajeria {

    // Atributos directamente en la clase principal
    private static List<Mensaje> listaMensajes = new ArrayList<>();
    private static final String ARCHIVO = "mensajes.dat";
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        cargarMensajes(); // Cargar al inicio

        int opcion = 0;
        do {
            mostrarMenu();
            try {
                System.out.print("Seleccione una opción: ");
                opcion = sc.nextInt();
                sc.nextLine(); // Consumir el salto de línea

                switch (opcion) {
                    case 1: listarTodosLosMensajes(); break;
                    case 2: mostrarMensajesNormales(); break;
                    case 3: mostrarMensajesEncriptados(); break;
                    case 4: buscarMensaje(); break;
                    case 5: agregarMensaje(); break;
                    case 6: desencriptarMensaje(); break;
                    case 7: eliminarMensaje(); break;
                    case 8: salir(); break; // Llama a guardar antes de salir
                    default: System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Error: Ingrese un número válido.");
                sc.nextLine(); // Limpiar buffer
            } catch (Exception e) {
                System.out.println("Error inesperado: " + e.getMessage());
                // e.printStackTrace(); // Descomentar para depurar si es necesario
            }
            System.out.println("------------------------------------");

        } while (opcion != 8);

        sc.close();
        System.out.println("Programa terminado.");
    }

    // --- Métodos de Lógica de Negocio (antes en GestorMensajes) ---

    private static void reorganizarCodigos() {
        for (int i = 0; i < listaMensajes.size(); i++) {
            listaMensajes.get(i).setCodigo(i + 1); // Códigos 1-based
        }
    }

    private static Mensaje buscarMensajePorCodigo(int codigo) {
        for (Mensaje m : listaMensajes) {
            if (m.getCodigo() == codigo) {
                return m;
            }
        }
        return null; // Devuelve null si no se encuentra (más simple que Optional)
    }

    // --- Métodos del Menú ---

    private static void mostrarMenu() {
        System.out.println("\n--- MENÚ ---");
        System.out.println("1. Listar todos (Pantalla/Archivo)");
        System.out.println("2. Mostrar normales");
        System.out.println("3. Mostrar encriptados");
        System.out.println("4. Buscar por código");
        System.out.println("5. Añadir mensaje");
        System.out.println("6. Desencriptar y mostrar");
        System.out.println("7. Eliminar por código");
        System.out.println("8. Salir");
    }

    private static void listarTodosLosMensajes() {
        if (listaMensajes.isEmpty()) {
            System.out.println("No hay mensajes.");
            return;
        }
        System.out.println("Mostrar en: 1. Pantalla | 2. Archivo");
        int destino = sc.nextInt(); sc.nextLine();

        if (destino == 1) {
            System.out.println("\n--- Todos los Mensajes ---");
            listaMensajes.forEach(System.out::println);
        } else if (destino == 2) {
            System.out.print("Nombre del archivo de texto: ");
            String nombreArchivo = sc.nextLine();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nombreArchivo))) {
                writer.write("--- Todos los Mensajes ---\n");
                for (Mensaje m : listaMensajes) {
                    writer.write(m.toString() + "\n");
                }
                System.out.println("Guardado en " + nombreArchivo);
            } catch (IOException e) {
                System.err.println("Error al guardar en archivo: " + e.getMessage());
            }
        } else {
            System.out.println("Opción no válida.");
        }
    }

    private static void mostrarMensajesNormales() {
        System.out.println("\n--- Mensajes Normales ---");
        boolean encontrados = false;
        for (Mensaje m : listaMensajes) {
            if (m instanceof MensajeNormal) {
                System.out.println(m);
                encontrados = true;
            }
        }
        if (!encontrados) System.out.println("No hay mensajes normales.");
    }

    private static void mostrarMensajesEncriptados() {
        System.out.println("\n--- Mensajes Encriptados (sin desencriptar) ---");
        boolean encontrados = false;
        for (Mensaje m : listaMensajes) {
            if (m instanceof MensajeEncriptado) {
                System.out.println(m); // Usa el toString() de MensajeEncriptado
                encontrados = true;
            }
        }
        if (!encontrados) System.out.println("No hay mensajes encriptados.");
    }

    private static void buscarMensaje() {
        System.out.print("Código a buscar: ");
        int codigo = sc.nextInt(); sc.nextLine();
        Mensaje encontrado = buscarMensajePorCodigo(codigo);
        if (encontrado != null) {
            System.out.println("Encontrado: " + encontrado);
        } else {
            System.out.println("Mensaje con código " + codigo + " no existe.");
        }
    }

    private static void agregarMensaje() {
        System.out.println("Tipo: 1. Normal | 2. Encriptado");
        int tipo = sc.nextInt(); sc.nextLine();
        System.out.print("Texto: ");
        String texto = sc.nextLine();

        int nuevoCodigo = listaMensajes.size() + 1;
        Mensaje nuevoMensaje = null;

        if (tipo == 1) {
            nuevoMensaje = new MensajeNormal(nuevoCodigo, texto);
        } else if (tipo == 2) {
            nuevoMensaje = new MensajeEncriptado(nuevoCodigo, texto); // Se encripta en el constructor
        } else {
            System.out.println("Tipo inválido.");
            return;
        }

        listaMensajes.add(nuevoMensaje);
        // Ya tiene el código correcto, no hace falta reorganizar al añadir al final.
        System.out.println("Mensaje añadido con código: " + nuevoCodigo);
    }

    private static void desencriptarMensaje() {
        System.out.print("Código del mensaje a desencriptar: ");
        int codigo = sc.nextInt(); sc.nextLine();
        Mensaje mensaje = buscarMensajePorCodigo(codigo);

        if (mensaje == null) {
            System.out.println("Mensaje no encontrado.");
        } else if (mensaje instanceof MensajeEncriptado) {
            // Casteo seguro porque ya comprobamos el tipo
            String textoOriginal = ((MensajeEncriptado) mensaje).desencriptar();
            System.out.println("Mensaje " + codigo + " desencriptado:");
            System.out.println("Texto original: " + textoOriginal);
        } else {
            System.out.println("El mensaje " + codigo + " no está encriptado.");
        }
    }

    private static void eliminarMensaje() {
        System.out.print("Código del mensaje a eliminar: ");
        int codigo = sc.nextInt(); sc.nextLine();

        boolean eliminado = false;
        // Usamos un iterador para eliminar de forma segura si fuera necesario,
        // pero buscar por índice y eliminar es más simple aquí si garantizamos
        // que los códigos coinciden con índice+1 después de reorganizar.
        // Opcion A: Buscar el objeto y eliminarlo (más robusto si los códigos se desordenan)
        Mensaje aEliminar = buscarMensajePorCodigo(codigo);
        if (aEliminar != null) {
            listaMensajes.remove(aEliminar);
            eliminado = true;
            reorganizarCodigos(); // !! Fundamental después de eliminar
            System.out.println("Mensaje eliminado. Códigos reorganizados.");
        }

        // Opcion B: Eliminar por índice (más rápido si los códigos están sincronizados)
        /*
        if (codigo > 0 && codigo <= listaMensajes.size()) {
             listaMensajes.remove(codigo - 1); // indice = codigo - 1
             eliminado = true;
             reorganizarCodigos(); // !! Fundamental
             System.out.println("Mensaje eliminado. Códigos reorganizados.");
        }
        */

        if (!eliminado) {
            System.out.println("Mensaje con código " + codigo + " no existe.");
        }
    }

    // --- Persistencia ---

    @SuppressWarnings("unchecked") // Necesario por el casting de la lista leída
    private static void cargarMensajes() {
        File archivo = new File(ARCHIVO);
        if (archivo.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ARCHIVO))) {
                Object obj = ois.readObject();
                if (obj instanceof List) { // Comprobación extra
                    listaMensajes = (List<Mensaje>) obj;
                    System.out.println("Mensajes cargados (" + listaMensajes.size() + ").");
                    // Asegurar códigos correctos tras cargar
                    reorganizarCodigos();
                } else {
                    System.out.println("Archivo .dat corrupto, iniciando lista vacía.");
                    listaMensajes = new ArrayList<>();
                }

            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error al cargar '" + ARCHIVO + "'. Empezando con lista vacía. Error: " + e.getMessage());
                listaMensajes = new ArrayList<>(); // Asegura que la lista exista aunque falle la carga
            }
        } else {
            System.out.println("Archivo '" + ARCHIVO + "' no encontrado. Se creará al salir.");
            listaMensajes = new ArrayList<>(); // Iniciar lista vacía
        }
    }

    private static void guardarMensajes() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ARCHIVO))) {
            oos.writeObject(listaMensajes);
            System.out.println("Mensajes guardados en '" + ARCHIVO + "'.");
        } catch (IOException e) {
            System.err.println("Error al guardar mensajes: " + e.getMessage());
        }
    }

    private static void salir() {
        guardarMensajes(); // Guardar antes de terminar
        System.out.println("Saliendo...");
    }
}