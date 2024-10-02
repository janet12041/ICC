package mx.unam.ciencias.icc;

import java.util.NoSuchElementException;

/**
 * <p>Clase para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas son iterables utilizando sus nodos. Las listas no aceptan a
 * <code>null</code> como elemento.</p>
 */
public class Lista {

    /**
     * Clase interna para nodos.
     */
    public class Nodo {

        /* El elemento del nodo. */
        private Object elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(Object elemento) {
            this.elemento = elemento;
        }

        /**
         * Regresa el nodo anterior del nodo.
         * @return el nodo anterior del nodo.
         */
        public Nodo getAnterior() {
            return anterior;
        }

        /**
         * Regresa el nodo siguiente del nodo.
         * @return el nodo siguiente del nodo.
         */
        public Nodo getSiguiente() {
            return siguiente;
        }

        /**
         * Regresa el elemento del nodo.
         * @return el elemento del nodo.
         */
        public Object get() {
            return elemento;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el nodo que contiene al elemento recibido en la lista.
     * @param elemento el elemento del que se busca el nodo que lo contiene.
     * @param n el nodo en el que se revisa si está contenido el elemento
     * recibido.
     * @return el nodo que contiene al elemento recibido en la lista, o null 
     * si el elemento no está contenido en la lista.
     */
    private Nodo getNodo(Object elemento, Nodo n) {
	return n == null ? null : n.elemento.equals(elemento) ?
	    n : getNodo (elemento, n.siguiente);
    }

    /**
     * Regresa el <em>i</em>-ésimo nodo de la lista.
     * @param i el índice del nodo que queremos, siendo el índice recibido 
     * siempre mayor que cero y menor que longitud - 1.
     * @param c el contador que indica el índice del nodo actual.
     * @param n el nodo a partir del cual recorremos la lista.
     * @return el <em>i</em>-ésimo nodo de la lista.
     */
    private Nodo getNodo(int i, int c, Nodo n){
	return c++ == i ? n : getNodo(i, c, n.siguiente);
     }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(Object elemento) {
        if (elemento == null)
	    throw new IllegalArgumentException();
	Nodo n = new Nodo(elemento);
	if (cabeza == null) {
	    cabeza = rabo = n;
	} else {
	    rabo.siguiente = n;
	    n.anterior = rabo;
	    rabo = n;
	}
	longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(Object elemento) {
        if (elemento == null)
	    throw new IllegalArgumentException();
	Nodo n = new Nodo(elemento);
	if (cabeza == null) {
	    cabeza = rabo = n;
	} else {
	    cabeza.anterior = n;
	    n.siguiente = cabeza;
	    cabeza = n;
	}
	longitud++;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, Object elemento) {
        if (elemento == null)
	    throw new IllegalArgumentException();
	if (i <= 0) {
	    agregaInicio(elemento);
	    return;
	}
	if (i >= longitud){
	    agregaFinal(elemento);
	    return;
	} 
        Nodo e = new Nodo(elemento);
        Nodo n = getNodo(i, 0, cabeza);
	e.anterior = n.anterior;
        e.siguiente = n;
        e.anterior.siguiente = n.anterior = e;
        longitud++;
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    public void elimina(Object elemento) {
        Nodo n = getNodo(elemento, cabeza);
	if (n == null)
	    return;
	if (n == cabeza) {
	    eliminaPrimero();
	    return;
	}
	if (n == rabo){
	    eliminaUltimo();
	    return;
	}
	n.anterior.siguiente = n.siguiente;
	n.siguiente.anterior = n.anterior;
	longitud--;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public Object eliminaPrimero() {
        if (cabeza == null)
	    throw new NoSuchElementException();
        Object elemento = cabeza.elemento;
	if (cabeza.equals(rabo)) {
	    cabeza = rabo = null;
	} else {
	    cabeza = cabeza.siguiente;
	    cabeza.anterior = null;
	}
	longitud--;
	return elemento;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public Object eliminaUltimo() {
        if (cabeza == null)
	    throw new NoSuchElementException();
	Object elemento = rabo.elemento;
	if (cabeza.equals(rabo)) {
	    cabeza = rabo = null;
	} else {
	    rabo = rabo.anterior;
	    rabo.siguiente = null;
	}
        longitud--;
	return elemento;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(Object elemento) {
        return getNodo(elemento, cabeza) != null;
    }

    /**
     * Regresa la reversa de una lista.
     * @param n el nodo que se agrega a la nueva lista.
     * @return una nueva lista que es la reversa de la lista a la que pertenece
     * el nodo.
     */
    private Lista reversa(Nodo n) {
	Lista lista =
	   n == rabo ? new Lista() : reversa(n.siguiente);
	lista.agregaFinal(n.elemento);
	return lista;
    }

    /**
     * Regresa la copia de una lista.
     * @param n el nodo que se agrega a la nueva lista.
     * @return una copia de la lista a la que pertenece el nodo. La copia tiene 
     * los mismos elementos que la lista, en el mismo orden.
     */
    private Lista copia(Nodo n) {
	Lista lista = n == rabo ? new Lista() : copia(n.siguiente);
	lista.agregaInicio(n.elemento);
	return lista;
     }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista reversa() {
       return cabeza == null ? new Lista() : reversa(cabeza);
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista copia() {
        return cabeza == null ? new Lista() : copia(cabeza);
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    public void limpia() {
        cabeza = rabo = null;
	longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public Object getPrimero() {
	if (cabeza == null)
	    throw new NoSuchElementException();
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public Object getUltimo() {
	if (rabo == null)
	    throw new NoSuchElementException();
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public Object get(int i) {
        if (i < 0 || i >= longitud)
	    throw new ExcepcionIndiceInvalido();
	return getNodo(i, 0, cabeza).elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @param n el nodo en el que se revisa si está contenido el elemento 
     * recibido.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    private int indiceDe(Object elemento, Nodo n) {
	return n.elemento.equals(elemento) ?
	    0 : indiceDe(elemento, n.siguiente)+1;
     }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(Object elemento) {
        return !contiene(elemento) ? -1 : indiceDe(elemento, cabeza);
    }

    /**
     * Regresa una representación en cadena de una lista.
     * @param n el nodo del que se regresara una representación en cadena.
     * @return una representación en cadena de la lista.
     */
    private String toString(Nodo n) {
	if(n.anterior != null) 
	    return toString(n.anterior) + String.format(", %s", n.elemento);
        return String.format("[%s", n.elemento);
     }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        if (cabeza == null)
	    return "[]";
	return toString(rabo) + "]";
    }

    /**
     * Nos dice si la lista de un nodo es igual a la lista de otro nodo
     * recibido.
     * @param n1 nodo de la lista original.
     * @param n2 nodo de la lista con la que hay que comparar.
     * @return <code>true</code> si la lista es igual a la recibida;
     *         <code>false</code> en otro caso.
     */
    private boolean equals(Nodo n1, Nodo n2) {
	if (n1 == null)
	    return true;
	if (!n1.elemento.equals(n2.elemento))
	    return false;
	return equals(n1.siguiente, n2.siguiente); 
     }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (!(objeto instanceof Lista))
            return false;
        Lista lista = (Lista)objeto;
        if (lista.longitud != longitud)
	    return false;
	return equals(cabeza, lista.cabeza);
    }

    /**
     * Regresa el nodo cabeza de la lista.
     * @return el nodo cabeza de la lista.
     */
    public Nodo getCabeza() {
        return cabeza;
    }

    /**
     * Regresa el nodo rabo de la lista.
     * @return el nodo rabo de la lista.
     */
    public Nodo getRabo() {
        return rabo;
    }
}
