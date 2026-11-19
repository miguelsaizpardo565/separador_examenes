package separador_examenes;

import java.awt.Point;
import java.awt.image.BufferedImage;

public class EsquemaPDF implements Comparable<EsquemaPDF>{
	// OJO, inicio y fin se basan en 0
	private int inicio, fin;
	private String ocrNombre;
	private int distanciaOCRNombre;
	private String ocrCalificacion;
	private Alumno alumnoPresunto;
	private BufferedImage imagenPortada;
	private Point puntoNombre = new Point();
	private Point puntoCalificacion = new Point();

	public EsquemaPDF(int inicio) {
		super();
		this.inicio = inicio;
		fin = 0;
		ocrNombre = "";
		ocrCalificacion = "";
		distanciaOCRNombre = 99999;
		alumnoPresunto = null;
		imagenPortada = null;
	}
	
	public int getInicio() {
		return inicio;
	}
	public void setInicio(int inicio) {
		this.inicio = inicio;
	}
	public int getFin() {
		return fin;
	}
	public void setFin(int fin) {
		this.fin = fin;
	}
	
	public String getOcrCalificacion() {
		return ocrCalificacion;
	}

	public void setOcrCalificacion(String unaCalificacion) {
		String strCalificacion = unaCalificacion.replaceAll( "[^0-9,]", "");
		try {
			float calificacion = Float.parseFloat( strCalificacion.replace(",", ".") );
			if ( calificacion > 10 ) {
				strCalificacion = strCalificacion.replace( ".", "");
				strCalificacion = strCalificacion.substring( 0, 1 ) + "," + strCalificacion.substring( 1 );
			}
		} catch ( NumberFormatException ne ) {}
		this.ocrCalificacion = strCalificacion;
				
	}

	public String getOcrNombre() {
		return ocrNombre;
	}
	public void setOcrNombre(String ocrNombre) {
		this.ocrNombre = ocrNombre;
	}
	public int getDistanciaOCRNombre() {
		return distanciaOCRNombre;
	}
	public void setDistanciaOCRNombre(int distanciaOCRNombre) {
		this.distanciaOCRNombre = distanciaOCRNombre;
	}
	public Alumno getAlumnoPresunto() {
		return alumnoPresunto;
	}
	public void setAlumnoPresunto(Alumno alumnoPresunto) {
		this.alumnoPresunto = alumnoPresunto;
	}

	public BufferedImage getImagenPortada() {
		return imagenPortada;
	}

	public void setImagenPortada(BufferedImage imagenPortada) {
		this.imagenPortada = imagenPortada;
	}

	
	
	public Point getPuntoNombre() {
		return puntoNombre;
	}

	public void setPuntoNombre( int x, int y) {
		this.puntoNombre = new Point(x,y);
	}

	public Point getPuntoCalificacion() {
		return puntoCalificacion;
	}

	public void setPuntoCalificacion( int x, int y) {
		this.puntoCalificacion = new Point(x,y);
	}

	@Override
	public int compareTo(EsquemaPDF otroEsquema) {
		int resultado = getDistanciaOCRNombre() - otroEsquema.getDistanciaOCRNombre();
		if ( resultado == 0 ) {
			resultado = getInicio() - otroEsquema.getInicio();
		}
		return resultado;
	}
	
	@Override
	public boolean equals(Object obj) {
		return obj instanceof EsquemaPDF && ((EsquemaPDF)obj).getInicio() == getInicio();
	}
	
	@Override
	public String toString() {
		return "Esquema " + getInicio() + " - " + getFin() + ": " + getOcrNombre() + " distancia " + getDistanciaOCRNombre() + " de " + getAlumnoPresunto().toString(); 
	}
}
