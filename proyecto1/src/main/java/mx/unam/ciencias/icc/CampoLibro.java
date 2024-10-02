package mx.unam.ciencias.icc;

/**
 * Enumeración para los campos de un {@link Libro}.
 */
public enum CampoLibro {

    /** El título del libro. */
    TITULO,
    /** El autor del libro. */
    AUTOR,
    /** La editorial del libro. */
    EDITORIAL,
    /** El año de publicación del libro. */
    AÑO,
    /** El número de edición del libro. */
    EDICION,
    /** El número de páginas del libro. */
    PAGINAS,
    /** El precio del libro. */
    PRECIO;

    /**
     * Regresa una representación en cadena del campo para ser usada en
     * interfaces gráficas.
     * @return una representación en cadena del campo.
     */
    @Override public String toString() {
	String s = null;
        switch(this) {
           case TITULO:    s = "Titulo";
	                   break;
	   case AUTOR:     s = "Autor";
	                   break;
	   case EDITORIAL: s = "Editorial";
	                   break;
	   case AÑO:       s = "Año";
	                   break;
	   case EDICION:   s = "Edicion";
	                   break;
	   case PAGINAS:   s = "Paginas";
	                   break;
	   case PRECIO:    s = "Precio";
	                   break;
	}
	return s;
    }
}
