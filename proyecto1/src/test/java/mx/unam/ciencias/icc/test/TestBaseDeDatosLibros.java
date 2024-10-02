package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.BaseDeDatos;
import mx.unam.ciencias.icc.BaseDeDatosLibros;
import mx.unam.ciencias.icc.CampoLibro;
import mx.unam.ciencias.icc.Libro;
import mx.unam.ciencias.icc.Lista;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la clase {@link BaseDeDatosLibros}.
 */
public class TestBaseDeDatosLibros {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Base de datos de libros. */
    private BaseDeDatosLibros bdd;
    /* Número total de libros. */
    private int total;

    /* Enumeración espuria. */
    private enum X {
        /* Campo espurio. */
        A;
    }

    /**
     * Crea un generador de números aleatorios para cada prueba y una base de
     * datos de libros.
     */
    public TestBaseDeDatosLibros() {
        random = new Random();
        bdd = new BaseDeDatosLibros();
        total = 1 + random.nextInt(100);
    }

    /**
     * Prueba unitaria para {@link
     * BaseDeDatosLibros#BaseDeDatosLibros}.
     */
    @Test public void testConstructor() {
        Lista libros = bdd.getRegistros();
        Assert.assertFalse(libros == null);
        Assert.assertTrue(libros.getLongitud() == 0);
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getNumRegistros}.
     */
    @Test public void testGetNumRegistros() {
        Assert.assertTrue(bdd.getNumRegistros() == 0);
        for (int i = 0; i < total; i++) {
            Libro l = TestLibro.libroAleatorio();
            bdd.agregaRegistro(l);
            Assert.assertTrue(bdd.getNumRegistros() == i+1);
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#getRegistros}.
     */
    @Test public void testGetRegistros() {
        Lista l = bdd.getRegistros();
        Lista r = bdd.getRegistros();
        Assert.assertTrue(l.equals(r));
        Assert.assertFalse(l == r);
        Libro[] libros = new Libro[total];
        for (int i = 0; i < total; i++) {
            libros[i] = TestLibro.libroAleatorio();
            bdd.agregaRegistro(libros[i]);
        }
        l = bdd.getRegistros();
        int c = 0;
        Lista.Nodo nodo = l.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(libros[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
        l.elimina(libros[0]);
        Assert.assertFalse(l.equals(bdd.getRegistros()));
        Assert.assertFalse(l.getLongitud() == bdd.getNumRegistros());
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#agregaRegistro}.
     */
    @Test public void testAgregaRegistro() {
        for (int i = 0; i < total; i++) {
            Libro l = TestLibro.libroAleatorio();
            Assert.assertFalse(bdd.getRegistros().contiene(l));
            bdd.agregaRegistro(l);
            Assert.assertTrue(bdd.getRegistros().contiene(l));
            Lista ll = bdd.getRegistros();
            Assert.assertTrue(ll.get(ll.getLongitud() - 1).equals(l));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#eliminaRegistro}.
     */
    @Test public void testEliminaRegistro() {
        double precio = random.nextInt(5000) / 10.0 + 50.0;
        for (int i = 0; i < total; i++) {
            Libro l = TestLibro.libroAleatorio(precio + i);
            bdd.agregaRegistro(l);
        }
        while (bdd.getNumRegistros() > 0) {
            int i = random.nextInt(bdd.getNumRegistros());
            Libro l = (Libro)bdd.getRegistros().get(i);
            Assert.assertTrue(bdd.getRegistros().contiene(l));
            bdd.eliminaRegistro(l);
            Assert.assertFalse(bdd.getRegistros().contiene(l));
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#limpia}.
     */
    @Test public void testLimpia() {
        for (int i = 0; i < total; i++) {
            Libro l = TestLibro.libroAleatorio();
            bdd.agregaRegistro(l);
        }
        Lista registros = bdd.getRegistros();
        Assert.assertFalse(registros.esVacia());
        Assert.assertFalse(registros.getLongitud() == 0);
        bdd.limpia();
        registros = bdd.getRegistros();
        Assert.assertTrue(registros.esVacia());
        Assert.assertTrue(registros.getLongitud() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#guarda}.
     */
    @Test public void testGuarda() {
        for (int i = 0; i < total; i++) {
            Libro l = TestLibro.libroAleatorio();
            bdd.agregaRegistro(l);
        }
        String guardado = "";
        try {
            StringWriter swOut = new StringWriter();
            BufferedWriter out = new BufferedWriter(swOut);
            bdd.guarda(out);
            out.close();
            guardado = swOut.toString();
        } catch (IOException ioe) {
            Assert.fail();
        }
        String[] lineas = guardado.split("\n");
        Assert.assertTrue(lineas.length == total);
        Lista ll = bdd.getRegistros();
        int c = 0;
        Lista.Nodo nodo = ll.getCabeza();
        while (nodo != null) {
            Libro l = (Libro)nodo.get();
            String el = String.format("%s\t%s\t%s\t%d\t%d\t%d\t%2.2f",
                                      l.getTitulo(),
                                      l.getAutor(),
                                      l.getEditorial(),
				      l.getAño(),
				      l.getEdicion(),
				      l.getPaginas(),
                                      l.getPrecio());
            Assert.assertTrue(lineas[c++].equals(el));
            nodo = nodo.getSiguiente();
        }
    }

    /**
     * Prueba unitaria para {@link BaseDeDatos#carga}.
     */
    @Test public void testCarga() {
        double precio = random.nextInt(5000) / 10.0 + 50.0;
        String entrada = "";
        Libro[] libros = new Libro[total];
        for (int i = 0; i < total; i++) {
            libros[i] = TestLibro.libroAleatorio(precio + i);
            entrada += String.format("%s\t%s\t%s\t%d\t%d\t%d\t%2.2f\n",
                                     libros[i].getTitulo(),
                                     libros[i].getAutor(),
                                     libros[i].getEditorial(),
				     libros[i].getAño(),
				     libros[i].getEdicion(),
				     libros[i].getPaginas(),
                                     libros[i].getPrecio());
            bdd.agregaRegistro(libros[i]);
        }
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == total);
        int c = 0;
        Lista ll = bdd.getRegistros();
        Lista.Nodo nodo = ll.getCabeza();
        while (nodo != null) {
            Assert.assertTrue(libros[c++].equals(nodo.get()));
            nodo = nodo.getSiguiente();
        }
        entrada = String.format("%s\t%s\t%s\t%d\t%d\t%d\t%2.2f\n",
                                libros[0].getTitulo(),
                                libros[0].getAutor(),
                                libros[0].getEditorial(),
				libros[0].getAño(),
				libros[0].getEdicion(),
				libros[0].getPaginas(),
                                libros[0].getPrecio());
        entrada += " \n";
        entrada += String.format("%s\t%s\t%s\t%d\t%d\t%d\t%2.2f\n",
                                 libros[1].getTitulo(),
                                 libros[1].getAutor(),
                                 libros[1].getEditorial(),
				 libros[1].getAño(),
				 libros[1].getEdicion(),
				 libros[1].getPaginas(),
                                 libros[1].getPrecio());
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 1);
        entrada = "";
        try {
            StringReader srInt = new StringReader(entrada);
            BufferedReader in = new BufferedReader(srInt, 8192);
            bdd.carga(in);
            in.close();
        } catch (IOException ioe) {
            Assert.fail();
        }
        Assert.assertTrue(bdd.getNumRegistros() == 0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosLibros#creaRegistro}.
     */
    @Test public void testCreaRegistro() {
        Libro l = (Libro)bdd.creaRegistro();
        Assert.assertTrue(l.getTitulo() == null);
	Assert.assertTrue(l.getAutor() == null);
	Assert.assertTrue(l.getEditorial() == null);
        Assert.assertTrue(l.getAño() == 0);
	Assert.assertTrue(l.getEdicion() == 0);
	Assert.assertTrue(l.getPaginas() == 0);
        Assert.assertTrue(l.getPrecio() == 0.0);
    }

    /**
     * Prueba unitaria para {@link BaseDeDatosLibros#buscaRegistros}.
     */
    @Test public void testBuscaRegistros() {
        Libro[] libros = new Libro[total];
	int ini = random.nextInt(500);
        for (int i = 0; i < total; i++) {
            Libro l =  new Libro(String.valueOf(10000+ini+i),
				 String.valueOf(10000+ini+i),
				 String.valueOf(10000+ini+i),
				 1500+ini+i, 1+i, 100+ini+i,
				 ini / 2.0 + 50.0 + i);
            libros[i] = l;
            bdd.agregaRegistro(l);
        }

        Libro libro;
        Lista ll;
        Lista.Nodo nodo;
        int i;

        for (int k = 0; k < total/10 + 3; k++) {
            i = random.nextInt(total);
            libro = libros[i];

            String titulo = libro.getTitulo();
            ll = bdd.buscaRegistros(CampoLibro.TITULO, titulo);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getTitulo().indexOf(titulo) > -1);
                nodo = nodo.getSiguiente();
            }
            int t = titulo.length();
            String bt = titulo.substring(random.nextInt(2),
                                         2 + random.nextInt(t-2));
            ll = bdd.buscaRegistros(CampoLibro.TITULO, bt);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getTitulo().indexOf(bt) > -1);
                nodo = nodo.getSiguiente();
            }

	    String autor = libro.getAutor();
            ll = bdd.buscaRegistros(CampoLibro.AUTOR, autor);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getAutor().indexOf(autor) > -1);
                nodo = nodo.getSiguiente();
            }
            int a = autor.length();
            String ba = autor.substring(random.nextInt(2),
                                         2 + random.nextInt(a-2));
            ll = bdd.buscaRegistros(CampoLibro.AUTOR, ba);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getAutor().indexOf(ba) > -1);
                nodo = nodo.getSiguiente();
            }

	    String editorial = libro.getEditorial();
            ll = bdd.buscaRegistros(CampoLibro.EDITORIAL, editorial);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getEditorial().indexOf(editorial) > -1);
                nodo = nodo.getSiguiente();
            }
            int ed = editorial.length();
            String bed = editorial.substring(random.nextInt(2),
                                         2 + random.nextInt(ed-2));
            ll = bdd.buscaRegistros(CampoLibro.EDITORIAL, bed);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getEditorial().indexOf(bed) > -1);
                nodo = nodo.getSiguiente();
            }

            Integer año = Integer.valueOf(libro.getAño());
            ll = bdd.buscaRegistros(CampoLibro.AÑO, año);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getAño() >= año.intValue());
                nodo = nodo.getSiguiente();
            }
            Integer bañ = Integer.valueOf(año.intValue() - 10);
            ll = bdd.buscaRegistros(CampoLibro.AÑO, bañ);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getAño() >= bañ.intValue());
                nodo = nodo.getSiguiente();
            }

	    Integer edicion = Integer.valueOf(libro.getEdicion());
            ll = bdd.buscaRegistros(CampoLibro.EDICION, edicion);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getEdicion() >= edicion.intValue());
                nodo = nodo.getSiguiente();
            }
            Integer be = Integer.valueOf(edicion.intValue() - 1);
            ll = bdd.buscaRegistros(CampoLibro.EDICION, be);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getEdicion() >= be.intValue());
                nodo = nodo.getSiguiente();
            }

	    Integer paginas = Integer.valueOf(libro.getPaginas());
            ll = bdd.buscaRegistros(CampoLibro.PAGINAS, paginas);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getPaginas() >= paginas.intValue());
                nodo = nodo.getSiguiente();
            }
            Integer bp = Integer.valueOf(paginas.intValue() - 10);
            ll = bdd.buscaRegistros(CampoLibro.PAGINAS, bp);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getPaginas() >= bp.intValue());
                nodo = nodo.getSiguiente();
            }

            Double precio = Double.valueOf(libro.getPrecio());
            ll = bdd.buscaRegistros(CampoLibro.PRECIO, precio);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getPrecio() >= precio.doubleValue());
                nodo = nodo.getSiguiente();
            }
            Double bpr = Double.valueOf(precio.doubleValue() - 5.0);
            ll = bdd.buscaRegistros(CampoLibro.PRECIO, bpr);
            Assert.assertTrue(ll.getLongitud() > 0);
            Assert.assertTrue(ll.contiene(libro));
            nodo = ll.getCabeza();
            while (nodo != null) {
                Libro l = (Libro)nodo.get();
                Assert.assertTrue(l.getPrecio() >= bpr.doubleValue());
                nodo = nodo.getSiguiente();
            }
        }
        
        ll = bdd.buscaRegistros(CampoLibro.TITULO,
                               "xxx-titulo");
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.AUTOR,
                               "xxx-autor");
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.EDITORIAL,
                               "xxx-editorial");
        Assert.assertTrue(ll.esVacia());
        ll = bdd.buscaRegistros(CampoLibro.AÑO,
                               Integer.valueOf(3000));
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.EDICION,
                               Integer.valueOf(3000));
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.PAGINAS,
                               Integer.valueOf(3000));
        Assert.assertTrue(ll.esVacia());
        ll = bdd.buscaRegistros(CampoLibro.PRECIO,
                               Double.valueOf(3000));
			       Assert.assertTrue(ll.esVacia());
					
        ll = bdd.buscaRegistros(CampoLibro.TITULO, "");
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.AUTOR, "");
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.EDITORIAL, "");
        Assert.assertTrue(ll.esVacia());
        ll = bdd.buscaRegistros(CampoLibro.AÑO,
                               Integer.valueOf(Integer.MAX_VALUE));
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.EDICION,
                               Integer.valueOf(Integer.MAX_VALUE));
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.PAGINAS,
                               Integer.valueOf(Integer.MAX_VALUE));
        Assert.assertTrue(ll.esVacia());
        ll = bdd.buscaRegistros(CampoLibro.PRECIO,
                               Double.valueOf(Double.MAX_VALUE));
	Assert.assertTrue(ll.esVacia());

        ll = bdd.buscaRegistros(CampoLibro.TITULO, null);
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.AUTOR, null);
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.EDITORIAL, null);
        Assert.assertTrue(ll.esVacia());
        ll = bdd.buscaRegistros(CampoLibro.AÑO, null);
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.EDICION, null);
        Assert.assertTrue(ll.esVacia());
	ll = bdd.buscaRegistros(CampoLibro.PAGINAS, null);
        Assert.assertTrue(ll.esVacia());
        ll = bdd.buscaRegistros(CampoLibro.PRECIO, null);
        Assert.assertTrue(ll.esVacia());

        try {
            ll = bdd.buscaRegistros(null, null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            ll = bdd.buscaRegistros(X.A, null);
            Assert.fail();
	    } catch (IllegalArgumentException iae) {}
    }
}
