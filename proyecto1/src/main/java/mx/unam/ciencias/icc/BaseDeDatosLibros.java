package mx.unam.ciencias.icc;

/**
 * Clase para bases de datos de libros.
 */
public class BaseDeDatosLibros extends BaseDeDatos {

    /**
     * Crea un libro en blanco.
     * @return un libro en blanco.
     */
    @Override public Registro creaRegistro() {
        return (Registro) new Libro(null, null, null, 0, 0, 0, 0.0);
    }
}
