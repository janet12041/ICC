package mx.unam.ciencias.icc.proyecto2;

import java.util.Scanner;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.InputStreamReader;
import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.text.Collator;
import mx.unam.ciencias.icc.Lista;

/**
 * Proyecto 2: Ordenador lexicográfico.
 */
public class Proyecto2 {

    /* Guarda la salida del programa en un archivo llamado como el identificador. */
    private static void escritura(Lista<String> lineas, String identificador) {
	File archivo = new File(identificador);

	try {
            FileOutputStream fileOut = new FileOutputStream(identificador);
            OutputStreamWriter osOut = new OutputStreamWriter(fileOut);
            BufferedWriter out = new BufferedWriter(osOut);
	    
            for (String linea : lineas)
		out.write(linea + "\n");
	    
            out.close();
        } catch (IOException ioe) {
            System.out.printf("No pude guardar en el archivo \"%s\".\n",
                              identificador);
            System.exit(1);
        }

        System.out.printf("\nOrdenación lexicográfica guardada exitosamente en " +
			  "\"%s\".\n", identificador);
    }

    /* Lee líneas de un archivo cargándolo del disco duro y las agrega como 
       elementos de una lista. */
    private static void lectura(Lista<String> lineas, String nombreArchivo) {
	try {
	    InputStreamReader isIn;
	    
	    if (nombreArchivo == null) 
		isIn = new InputStreamReader(System.in);
	    else
		isIn = new InputStreamReader(new FileInputStream(nombreArchivo));
	   
            BufferedReader in = new BufferedReader(isIn);
	    String linea;

	    if (!in.ready())
		uso();

	    while ((linea = in.readLine()) != null) 
		lineas.agregaFinal(linea);

            in.close();
        } catch (IOException ioe) {
            System.out.printf("No pude cargar el archivo \"%s\".\n",
                              nombreArchivo);
            System.exit(1);
        }
    }

    /* Imprime el uso del programa y termina. */
    private static void uso() {
        System.err.println("Uso: \n" +
	"java -jar target/proyecto2.jar [-r][-o <identificador>] <archivo>... \n"+
	"o cat <archivo> | java -jar target/proyecto2.jar [-r][-o <identificador>]");
        System.exit(1);
    }

    public static void main(String[] args) {

	Boolean banderaR = false;
	Boolean banderaO = false;
	String identificador = "";

	Lista<String> lineas = new Lista<String>();

        for (int i = 0; i < args.length; i++) {
	    if (args[i].equals("-r"))
		banderaR= true;
	    
	    else if (args[i].equals("-o")) {
		if (i+1 == args.length)
		    uso();
		banderaO= true;
		identificador = args[i+1];
		
	    } else if (i == 0 || !args[i-1].equals("-o"))
	        lectura(lineas, args[i]);
	}

	if(lineas.esVacia())
	    lectura(lineas, null);

	Collator comparador = Collator.getInstance();
	comparador.setStrength(Collator.PRIMARY);

	if(banderaR) 
	    lineas = lineas.mergeSort((l1, l2) -> {
		    l1= l1.replaceAll("[^\\p{L}\\p{Z}]", "");
		    l2= l2.replaceAll("[^\\p{L}\\p{Z}]", "");
		    if(comparador.compare(l1, l2) < 0)
			return 1;
		    else if (comparador.compare(l1, l2) > 0)
			return -1;
		    else
			return 0;
	    });
	 else 
	    lineas = lineas.mergeSort((l1, l2) -> {
		    l1= l1.replaceAll("[^\\p{L}\\p{Z}]", "");
		    l2= l2.replaceAll("[^\\p{L}\\p{Z}]", "");
		    return comparador.compare(l1, l2);
	    });
	
        for (String linea : lineas)
            System.out.println(linea);

	if(banderaO)
	    escritura(lineas, identificador);
    }
}
