package separador_examenes;

import static separador_examenes.Curso.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.TreeSet;

import org.apache.commons.text.similarity.LevenshteinDistance;

public class Alumno {
	private static HashMap<Curso, ArrayList<Alumno>> mapaAlumnos = new HashMap<Curso, ArrayList<Alumno>>();
	
	static {
		mapaAlumnos.put( c2eso,
				new ArrayList<Alumno>(
					Arrays.asList(new Alumno[] {
							new Alumno("Alonso", "Rodríguez", "Alicia"),
							new Alumno("Arrieta", "Aguas", "Dylan Andrés"),
							new Alumno("Cano", "Cantarero", "Adrián"),
							new Alumno("Castro", "Montilla", "Romeo"),
							new Alumno("Chila", "Campo", "Ian Manuel"),
							new Alumno("Fernández", "Alcaide", "Paula"),
							new Alumno("Flores", "Lozano", "Marina"),
							new Alumno("Hernández", "Sánchez", "Karelis Zuleima"),
							new Alumno("Sánchez", "Santos", "Iván"),
							new Alumno("Vásquez", "Cano", "Nahomy Desiré"),
							new Alumno("Carrillo", "Lorente", "Elena"),
							new Alumno("Garrido", "Díaz", "Víctor"),
							new Alumno("Lencina", "Duarte", "Maiara Noemi"),
							new Alumno("Nieto", "Flores", "David"),
							new Alumno("Ochoa", "Quero", "Irene"),
							new Alumno("Ojuez", "Paún", "Cristian-Enrique"),
							new Alumno("Salvatierra", "Toledano", "Darío"),
							new Alumno("Sánchez", "Lázaro", "Susana"),
							new Alumno("Santiago", "Carmona", "Rafael"),
							new Alumno("Vidal", "Martín", "José Ignacio"),
							new Alumno("Villamar", "Naranjo", "Fabián Leonel")
					})
				)
			);
		mapaAlumnos.put( c4eso,
				new ArrayList<Alumno>(
					Arrays.asList(new Alumno[] {
							new Alumno("Alonso", "Cazalla", "Valeria"),
							new Alumno("Alonso", "Pérez", "José Antonio"),
							new Alumno("Anze", "", "Diego"),
							new Alumno("Anze", "", "Nataly Agustina"),
							new Alumno("Blas", "Marín", "Angelo Neymar"),
							new Alumno("Castaño", "Ruiz", "Alba"),
							new Alumno("Castro", "Martínez", "Daniela"),
							new Alumno("Cros", "Bravo", "Daniel"),
							new Alumno("Fernández", "Gómez", "José"),
							new Alumno("Ferry", "Gracia", "Joseph"),
							new Alumno("Ferry", "Gracia", "Lucía"),
							new Alumno("Garrido", "Díaz", "Cayetana"),
							new Alumno("González", "Jiménez", "Raúl"),
							new Alumno("Gutiérrez-Ravé", "Cabezas", "Marcos"),
							new Alumno("Jiménez", "Navarrete", "María"),
							new Alumno("Jiménez", "Navarrete", "Pedro"),
							new Alumno("Jiménez", "Zamorano", "Rubén"),
							new Alumno("López", "Rincón", "Sergio"),
							new Alumno("Navas", "Reyes", "Estrella"),
							new Alumno("Nogales", "Moraño", "Adrián"),
							new Alumno("Patiño", "Pérez", "Dylam de Jesús"),
							new Alumno("Perlaza", "Yamchapaxi", "Randy Luis"),
							new Alumno("Pineda", "Mancheño", "Irene"),
							new Alumno("Raya", "López", "Carlos"),
							new Alumno("Roca", "Soriano", "Israel"),
							new Alumno("Silva", "Sanz", "Antonio Javier")
					})
				)
			);
		mapaAlumnos.put( c1bto,
				new ArrayList<Alumno>(
					Arrays.asList(new Alumno[] {
							new Alumno("Aguilar", "Morales", "Enrique"),
							new Alumno("Amarilla", "Villalba", "Andrea Aylen"),
							new Alumno("Blanco", "Rodríguez", "Andrea"),
							new Alumno("Cañas", "Prieto", "León"),
							new Alumno("Castillo", "Castro", "Valeria"),
							new Alumno("Ceballos", "Alonso", "Lorena María"),
							new Alumno("Cerro", "Plaza", "Adolfo"),
							new Alumno("Díaz", "Espetón", "Grecia"),
							new Alumno("Escoto", "Zelaya", "Jafeth Isai"),
							new Alumno("García", "López", "Bosco"),
							new Alumno("García", "López", "Estrella"),
							new Alumno("Lara", "Lorente", "Eva María"),
							new Alumno("Laredo", "Gay", "Nuria"),
							new Alumno("López", "Rodríguez", "Álvaro"),
							new Alumno("Luque", "Neira", "María"),
							new Alumno("Mateos", "Coiras", "Rafael"),
							new Alumno("Molina", "Azofra", "Laura"),
							new Alumno("Muñoz", "Alba", "Julián"),
							new Alumno("Muñoz", "Jiménez", "Clara"),
							new Alumno("Salmoral", "Galisteo", "Alejandro"),
							new Alumno("Sánchez", "Lecue", "Pablo"),
							new Alumno("Victorio", "Alonso", "Gonzalo")
					})
				)
			);
		mapaAlumnos.put( economia,
				new ArrayList<Alumno>(
					Arrays.asList(new Alumno[] {
							new Alumno("Caballero", "Vargas", "Adrián"),
							new Alumno("Casado", "De la Rubia", "Hugo"),
							new Alumno("Esposito", "", "Francesco"),
							new Alumno("Navarrete", "Lorente", "Héctor"),
							new Alumno("Rivero", "Saltos", "Jonathan David"),
							new Alumno("Martínez", "Cañadillas", "Irene"),
							new Alumno("Perabad", "Torres", "Adrián"),
							new Alumno("Torés", "Carmona", "Román")
					})
				)
			);

	}
	public static ArrayList<Alumno> getAlumnos( Curso curso ) {
		ArrayList<Alumno> alumnos = mapaAlumnos.get(curso);
		if ( alumnos == null )
			alumnos = new ArrayList<Alumno>();
		return alumnos;
	}
	
	public static void eliminarAlumno( Curso curso, Alumno a ) { getAlumnos(curso).remove(a); }
	
	private String nombre, ape1, ape2;
	public String[] getCadenas() {
		return new String[] {nombre, ape1, ape2 };
	}

	public Alumno(String ape1, String ape2, String nombre) {
		this.ape1 = ape1;
		this.ape2 = ape2;
		this.nombre = nombre;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof Alumno && 
				((Alumno)obj).getNombre().equals(getNombre()) &&
				((Alumno)obj).getApe1().equals(getApe1()) &&
				((Alumno)obj).getApe2().equals(getApe2());
	}
	
	/*
	public static Alumno getClosestAlumnoAntiguo( Curso curso, String cadena, boolean esBorrar ) {
		Alumno a = getClosestAlumno( curso, cadena );
		if ( esBorrar )
			eliminarAlumno( curso, a);
		return a;
	}
*/
	public static void getClosestAlumno( Curso curso, EsquemaPDF esquemaPDF ) {
		getClosestAlumno( getAlumnos( curso ), esquemaPDF);
	}

	public static void getClosestAlumno( ArrayList<Alumno> alumnos, EsquemaPDF esquemaPDF ) {
		int distanciaResultado = -1;
		Alumno alumnoResultado = null;
		for ( Alumno alumno : alumnos ) {
			ArrayList<Integer> distanciasAlumno = new ArrayList<Integer>();
			for ( String cadenaAlumno : alumno.getCadenas() ) {
				TreeSet<Integer> setDistanciasCadena = new TreeSet<Integer>();
				for ( String cadenaObjetivo : esquemaPDF.getOcrNombre().split("[\\s]+") ) {
					setDistanciasCadena.add( LevenshteinDistance.getDefaultInstance().apply( cadenaObjetivo, cadenaAlumno ) );
				}
				distanciasAlumno.add(setDistanciasCadena.first());
			}
			//System.out.println( distanciasAlumno );
			distanciasAlumno.sort(null);
			int distanciaAlumno = distanciasAlumno.removeFirst();
			while ( !distanciasAlumno.isEmpty() ) {
				distanciaAlumno = 10 * distanciaAlumno + distanciasAlumno.removeFirst();
			}

			//System.out.println( alumno + " " + distanciaAlumno );
			if ( alumnoResultado == null || distanciaAlumno < distanciaResultado ) {
				alumnoResultado = alumno;
				distanciaResultado = distanciaAlumno;
			}
		}
		
		esquemaPDF.setAlumnoPresunto(alumnoResultado);
		esquemaPDF.setDistanciaOCRNombre(distanciaResultado);
		
	}

	/*
	public static Alumno getClosestAlumnoAntiguo( Curso curso, String cadena ) {
		int distanciaResultado = -1;
		Alumno alumnoResultado = null;
		for ( Alumno alumno : getAlumnos( curso ) ) {
			ArrayList<Integer> distanciasAlumno = new ArrayList<Integer>();
			for ( String cadenaAlumno : alumno.getCadenas() ) {
				TreeSet<Integer> setDistanciasCadena = new TreeSet<Integer>();
				for ( String cadenaObjetivo : cadena.split("[\\s]+") ) {
					setDistanciasCadena.add( LevenshteinDistance.getDefaultInstance().apply( cadenaObjetivo, cadenaAlumno ) );
				}
				distanciasAlumno.add(setDistanciasCadena.first());
			}
			
			//System.out.println( distanciasAlumno );
			distanciasAlumno.sort(null);
			int distanciaAlumno = distanciasAlumno.removeFirst();
			while ( !distanciasAlumno.isEmpty() ) {
				distanciaAlumno = 10 * distanciaAlumno + distanciasAlumno.removeFirst();
			}

			//System.out.println( alumno + " " + distanciaAlumno );
			if ( alumnoResultado == null || distanciaAlumno < distanciaResultado ) {
				alumnoResultado = alumno;
				distanciaResultado = distanciaAlumno;
			}
		}
		//System.out.println(distanciaResultado);
		return alumnoResultado;
	}
	*/
	
	@Override
	public String toString() {
		return new StringBuffer().append( getApe1() ).append(" ").append( getApe2() ).append(" ").append( getNombre() ).toString();
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApe1() {
		return ape1;
	}

	public void setApe1(String ape1) {
		this.ape1 = ape1;
	}

	public String getApe2() {
		return ape2;
	}

	public void setApe2(String ape2) {
		this.ape2 = ape2;
	}

	
	
}
