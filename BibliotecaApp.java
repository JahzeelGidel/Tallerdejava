import java.util.*;
import java.io.*;

abstract class Publicacion {
    protected String codigo;
    protected String titulo;
    protected boolean disponible;
    protected int diasPrestado;

    public Publicacion(String codigo, String titulo, boolean disponible, int diasPrestado) {
        this.codigo = codigo;
        this.titulo = titulo;
        this.disponible = disponible;
        this.diasPrestado = diasPrestado;
    }

    public void prestar(int dias) {
        if (disponible) {
            disponible = false;
            diasPrestado = dias;
        }
    }

    public void devolver() {
        disponible = true;
        diasPrestado = 0;
    }

    public abstract void mostrarInfo();

    public boolean isDisponible() {
        return disponible;
    }

    public String getCodigo() {
        return codigo;
    }

    public int getDiasPrestado() {
        return diasPrestado;
    }

    public String getTitulo() {
        return titulo;
    }

    public abstract String toData();
}

class LibroFisico extends Publicacion {
    private String autor;

    public LibroFisico(String codigo, String titulo, String autor, boolean disponible, int diasPrestado) {
        super(codigo, titulo, disponible, diasPrestado);
        this.autor = autor;
    }

    public void mostrarInfo() {
        System.out.println("Libro Físico: " + titulo + " - " + autor + " - " + (disponible ? "Disponible" : "Prestado"));
    }

    public String toData() {
        return "LIBROFISICO;" + codigo + ";" + titulo + ";" + autor + ";" + disponible + ";" + diasPrestado;
    }
}

class LibroDigital extends Publicacion {
    private String autor;

    public LibroDigital(String codigo, String titulo, String autor, boolean disponible, int diasPrestado) {
        super(codigo, titulo, disponible, diasPrestado);
        this.autor = autor;
    }

    public void mostrarInfo() {
        System.out.println("Libro Digital: " + titulo + " - " + autor + " - " + (disponible ? "Disponible" : "Prestado"));
    }

    public String toData() {
        return "LIBRODIGITAL;" + codigo + ";" + titulo + ";" + autor + ";" + disponible + ";" + diasPrestado;
    }
}

class Revista extends Publicacion {
    private int numeroEdicion;

    public Revista(String codigo, String titulo, int numeroEdicion, boolean disponible, int diasPrestado) {
        super(codigo, titulo, disponible, diasPrestado);
        this.numeroEdicion = numeroEdicion;
    }

    public void mostrarInfo() {
        System.out.println("Revista: " + titulo + " - Edición " + numeroEdicion + " - " + (disponible ? "Disponible" : "Prestado"));
    }

    public String toData() {
        return "REVISTA;" + codigo + ";" + titulo + ";" + numeroEdicion + ";" + disponible + ";" + diasPrestado;
    }
}

public class BibliotecaApp {
    static ArrayList<Publicacion> publicaciones = new ArrayList<>();
    static final String ARCHIVO = "publicaciones.txt";

    public static void main(String[] args) {
        cargarArchivo();
        Scanner sc = new Scanner(System.in);
        int opcion;

        do {
            System.out.println("\n---- MENÚ ----");
            System.out.println("1. Ver publicaciones disponibles");
            System.out.println("2. Prestar publicación");
            System.out.println("3. Devolver publicación");
            System.out.println("4. Ver publicaciones prestadas");
            System.out.println("5. Salir");
            System.out.print("Opción: ");
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
            case 1:
                mostrarDisponibles();
                break;
            case 2:
                prestarDesdeLista(sc);
                break;
            case 3:
                devolverDesdeLista(sc);
                break;
                case 4:
                    mostrarPrestadas();
                    break;
                case 5:
                    guardarArchivo();
                    System.out.println("Datos guardados. ¡Hasta luego!");
                    break;
                default:
                    System.out.println("Opción inválida.");
            }
        } while (opcion != 5);
    }

    public static void mostrarDisponibles() {
        for (Publicacion p : publicaciones) {
            if (p.isDisponible()) p.mostrarInfo();
        }
    }

    public static void prestarDesdeLista(Scanner sc) {
        ArrayList<Publicacion> disponibles = new ArrayList<>();
        System.out.println("\nPublicaciones disponibles:");
        for (Publicacion p : publicaciones) {
            if (p.isDisponible()) {
                disponibles.add(p);
            }
        }

        if (disponibles.isEmpty()) {
            System.out.println("No hay publicaciones disponibles.");
            return;
        }

        // Mostrar lista numerada
        for (int i = 0; i < disponibles.size(); i++) {
            System.out.println((i + 1) + ". " + disponibles.get(i).getTitulo() + " [" + disponibles.get(i).getCodigo() + "]");
        }

        System.out.print("Seleccione el número de la publicación a prestar: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        if (opcion < 1 || opcion > disponibles.size()) {
            System.out.println("Opción inválida.");
            return;
        }

        Publicacion seleccionada = disponibles.get(opcion - 1);

        System.out.print("¿Cuántos días prestado?: ");
        int dias = sc.nextInt();
        sc.nextLine();
        seleccionada.prestar(dias);
        System.out.println("Prestado correctamente.");
    }


    public static void devolverDesdeLista(Scanner sc) {
        ArrayList<Publicacion> prestadas = new ArrayList<>();
        System.out.println("\nPublicaciones prestadas:");
        for (Publicacion p : publicaciones) {
            if (!p.isDisponible()) {
                prestadas.add(p);
            }
        }

        if (prestadas.isEmpty()) {
            System.out.println("No hay publicaciones prestadas.");
            return;
        }

        for (int i = 0; i < prestadas.size(); i++) {
            System.out.println((i + 1) + ". " + prestadas.get(i).getTitulo() + " [" + prestadas.get(i).getCodigo() + "]");
        }

        System.out.print("Seleccione el número de la publicación a devolver: ");
        int opcion = sc.nextInt();
        sc.nextLine();

        if (opcion < 1 || opcion > prestadas.size()) {
            System.out.println("Opción inválida.");
            return;
        }

        Publicacion seleccionada = prestadas.get(opcion - 1);
        seleccionada.devolver();
        System.out.println("Devuelto correctamente.");
    }


    public static void mostrarPrestadas() {
        for (Publicacion p : publicaciones) {
            if (!p.isDisponible()) {
                p.mostrarInfo();
                System.out.println("Días prestado: " + convertirNumeroATexto(p.getDiasPrestado()));
            }
        }
    }

    public static String convertirNumeroATexto(int numero) {
        String[] numeros = {
            "cero", "uno", "dos", "tres", "cuatro", "cinco", "seis", "siete",
            "ocho", "nueve", "diez", "once", "doce", "trece", "catorce", "quince",
            "dieciséis", "diecisiete", "dieciocho", "diecinueve", "veinte"
        };
        if (numero >= 0 && numero <= 20) return numeros[numero];
        return numero + " días";
    }

    public static void cargarArchivo() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARCHIVO))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(";");
                String tipo = datos[0];
                String codigo = datos[1];
                String titulo = datos[2];
                boolean disponible;
                int dias;

                switch (tipo) {
                    case "LIBROFISICO":
                        String autor1 = datos[3];
                        disponible = Boolean.parseBoolean(datos[4]);
                        dias = Integer.parseInt(datos[5]);
                        publicaciones.add(new LibroFisico(codigo, titulo, autor1, disponible, dias));
                        break;
                    case "LIBRODIGITAL":
                        String autor2 = datos[3];
                        disponible = Boolean.parseBoolean(datos[4]);
                        dias = Integer.parseInt(datos[5]);
                        publicaciones.add(new LibroDigital(codigo, titulo, autor2, disponible, dias));
                        break;
                    case "REVISTA":
                        int edicion = Integer.parseInt(datos[3]);
                        disponible = Boolean.parseBoolean(datos[4]);
                        dias = Integer.parseInt(datos[5]);
                        publicaciones.add(new Revista(codigo, titulo, edicion, disponible, dias));
                        break;
                }
            }
        } catch (IOException e) {
            System.out.println("Archivo no encontrado, iniciando vacío.");
        }
    }

    public static void guardarArchivo() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(ARCHIVO))) {
            for (Publicacion p : publicaciones) {
                pw.println(p.toData());
            }
        } catch (IOException e) {
            System.out.println("Error al guardar archivo.");
        }
    }
}
