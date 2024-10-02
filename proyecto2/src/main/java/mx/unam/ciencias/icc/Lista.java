package mx.unam.ciencias.icc;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase para listas genéricas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas implementan la interfaz {@link Iterable}, y por lo tanto se
 * pueden recorrer usando la estructura de control <em>for-each</em>. Las listas
 * no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Iterable<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase Iterador privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            anterior = null;
	    siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente != null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if (siguiente == null)
		throw new NoSuchElementException();
            anterior = siguiente;
	    siguiente = siguiente.siguiente;
	    return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior != null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if (anterior == null)
		throw new NoSuchElementException();
            siguiente = anterior;
	    anterior = anterior.anterior;
	    return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
	    siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
	    siguiente = null;
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
    private Nodo getNodo(T elemento, Nodo n) {
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
    public void agregaFinal(T elemento) {
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
    public void agregaInicio(T elemento) {
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
    public void inserta(int i, T elemento) {
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
    public void elimina(T elemento) {
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
    public T eliminaPrimero() {
        if (cabeza == null)
	    throw new NoSuchElementException();
        T elemento = cabeza.elemento;
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
    public T eliminaUltimo() {
        if (cabeza == null)
	    throw new NoSuchElementException();
	T elemento = rabo.elemento;
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
    public boolean contiene(T elemento) {
        return getNodo(elemento, cabeza) != null;
    }

    /**
     * Regresa la reversa de una lista.
     * @param n el nodo que se agrega a la nueva lista.
     * @return una nueva lista que es la reversa de la lista a la que pertenece
     * el nodo.
     */
    private Lista<T> reversa(Nodo n) {
	Lista<T> lista =
	   n == rabo ? new Lista<T>() : reversa(n.siguiente);
	lista.agregaFinal(n.elemento);
	return lista;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        return cabeza == null ? new Lista<T>() : reversa(cabeza);
    }

    /**
     * Regresa la copia de una lista.
     * @param n el nodo que se agrega a la nueva lista.
     * @return una copia de la lista a la que pertenece el nodo. La copia tiene 
     * los mismos elementos que la lista, en el mismo orden.
     */
    private Lista<T> copia(Nodo n) {
	Lista<T> lista = n == rabo ? new Lista<T>() : copia(n.siguiente);
	lista.agregaInicio(n.elemento);
	return lista;
     }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        return cabeza == null ? new Lista<T>() : copia(cabeza);
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
    public T getPrimero() {
        if (cabeza == null)
	    throw new NoSuchElementException();
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
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
    public T get(int i) {
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
    private int indiceDe(T elemento, Nodo n) {
	return n.elemento.equals(elemento) ?
	    0 : indiceDe(elemento, n.siguiente)+1;
     }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
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
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;
        if (lista.longitud != longitud)
	    return false;
	return equals(cabeza, lista.cabeza);
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    private Lista<T> mezcla(Lista<T> l2, Comparator<T> comparador){
	Lista<T> lista = new Lista<T>();
        Nodo i = cabeza;
	Nodo j = l2.cabeza;
	while (i != null && j != null) {
	    if (comparador.compare(i.elemento, j.elemento) <= 0) {
		lista.agregaFinal(i.elemento);
		i = i.siguiente;
	    } else {
		lista.agregaFinal(j.elemento);
		j = j.siguiente;
	    }
	}
	if (i == null)
	    i = j;
	while (i != null) {
	    lista.agregaFinal(i.elemento);
	    i = i.siguiente;
	}
	return lista;
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
	if (longitud == 0 || longitud == 1)
	    return copia();
	int l = longitud % 2 == 0 ? longitud/2 : (longitud - 1)/2;
        Lista<T> l1 = new Lista<T>();
	Lista<T> l2 = new Lista<T>();
	for (int i=0; i<l; i++) 
	    l1.agregaFinal(get(i));
        for (int i=l; i<longitud; i++)
	    l2.agregaFinal(get(i));
	l1 = l1.mergeSort(comparador);
	l2 = l2.mergeSort(comparador);
	return l1.mezcla(l2, comparador);
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
	Nodo n = cabeza;
        while(n.elemento != null && comparador.compare(n.elemento,elemento)<=0){
	    if (comparador.compare(n.elemento, elemento) == 0)
		return true;
	    n = n.siguiente;
	}
	return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
