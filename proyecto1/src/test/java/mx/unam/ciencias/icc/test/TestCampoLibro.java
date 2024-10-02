package mx.unam.ciencias.icc.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Random;
import mx.unam.ciencias.icc.CampoLibro;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * Clase para pruebas unitarias de la enumeración {@link CampoLibro}.
 */
public class TestCampoLibro {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /**
     * Prueba unitaria para {@link CampoLibro#toString}.
     */
    @Test public void testToString() {
        String s = CampoLibro.TITULO.toString();
        Assert.assertTrue(s.equals("Titulo"));
        s = CampoLibro.AUTOR.toString();
        Assert.assertTrue(s.equals("Autor"));
        s = CampoLibro.EDITORIAL.toString();
        Assert.assertTrue(s.equals("Editorial"));
        s = CampoLibro.AÑO.toString();
        Assert.assertTrue(s.equals("Año"));
	s = CampoLibro.EDICION.toString();
        Assert.assertTrue(s.equals("Edicion"));
	s = CampoLibro.PAGINAS.toString();
        Assert.assertTrue(s.equals("Paginas"));
	s = CampoLibro.PRECIO.toString();
        Assert.assertTrue(s.equals("Precio"));
    }
}
