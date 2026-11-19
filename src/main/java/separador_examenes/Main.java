package separador_examenes;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import com.google.protobuf.ByteString;

import net.sourceforge.tess4j.ITessAPI.TessPageIteratorLevel;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.Word;

public class Main {

	private int numDesconocido = 0;
	
	private final String RUTA_ORIGEN = "C:\\Users\\yo\\Documents\\1 bto Economía\\unidad 2\\examen unidad 2\\resultado\\z_resultado_examen_unidad_2.pdf";
//	private final String RUTA_ORIGEN = "C:\\Users\\yo\\Documents\\borrame\\prueba.pdf";
	
	
	private String RUTA_BASE_DESTINO = "";
	private final Curso CURSO = Curso.economia;
	private final String PATRON_BUSQUEDA_PORTADA = "Saavedra";
	private final String PATRON_BUSQUEDA_NOMBRE = "Nombre";
	private final String PATRON_BUSQUEDA_CALIFICACION = "Calificaci";
	// ubicación del nombre
	private final int 	X_NOMBRE_DEFAULT = 225,
						Y_NOMBRE_DEFAULT = 588,
						ANCHO_NOMBRE = 1300,
						ALTO_NOMBRE = 217,
						ANCHO_PATRON_NOMBRE = 193,
						ALTO_PATRON_NOMBRE = 71;
	private final int 	X_CALIFICACION_DEFAULT = 1752,
						Y_CALIFICACION_DEFAULT = 372,
						ANCHO_CALIFICACION = 500,
						ALTO_CALIFICACION = 300,
						ANCHO_PATRON_CALIFICACION = 224,
						ALTO_PATRON_CALIFICACION = 71;
	
	private int sufijo = 1;
	private int totalPaginasGeneradas = 0;
	private int totalFicherosGenerados = 0;
	
	private final ITesseract mitesseract = new Tesseract();
	private PDDocument docOrigen;
	private PDFRenderer pdfRenderer;
	
//	private ArrayList<PDPage> listaPaginas = new ArrayList<PDPage>();
	
	private ArrayList<EsquemaPDF> listaEsquemas = new ArrayList<EsquemaPDF>();
	private HashMap<Alumno, ArrayList<EsquemaPDF>> mapaEsquemas = new HashMap<Alumno, ArrayList<EsquemaPDF>>();
	
	public static void main(String[] args) throws IOException {
		new Main().init();
		//new Main().analizar();
		
	}
	
	public Main() throws IOException {
		 mitesseract.setLanguage("spa");
		 mitesseract.setDatapath("C:\\Program Files\\Tesseract-OCR\\tessdata");
		 docOrigen = Loader.loadPDF( new File( RUTA_ORIGEN ) );
		 if ( RUTA_BASE_DESTINO.isBlank() )
			 RUTA_BASE_DESTINO = new File( RUTA_ORIGEN ).getParent() + "\\";
	}

	private void init() {
		System.out.println( "generando esquemas" );
		generarEsquemasNumerosImagen();
		System.out.println( "completando esquemas" );
		completarEsquemas();
		System.out.println( "generando pdfs" );
		generarPDFs();
		System.out.printf( "%d paginas generadas en %d ficheros pdf con éxito", totalPaginasGeneradas, totalFicherosGenerados );
		System.out.println( "fin" );
	}

	public void abrirVentana( BufferedImage imagen, String titulo ) {
		JFrame frame = new JFrame( titulo );
		JPanel panel = new JPanel();
		panel.add(new JLabel( new ImageIcon(imagen)));
		frame.setContentPane(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public String googleOCR( BufferedImage bImagen ) throws IOException {
		String resultado = "";
		List<AnnotateImageRequest> requests = new ArrayList<>();

		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ImageIO.write(bImagen, "jpeg", os);
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		ByteString imgBytes = ByteString.readFrom( is );

		Image img = Image.newBuilder().setContent(imgBytes).build();
		Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
		AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
		requests.add(request);
		
		GoogleCredentials credentials = GoogleCredentials.fromStream(
		        new FileInputStream("C:\\Users\\yo\\Documents\\software\\mi-ocr-google-auth.json")
		    );
		ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
		        .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
		        .build();

		// Initialize client that will be used to send requests. This client only needs
		// to be created
		// once, and can be reused for multiple requests. After completing all of your
		// requests, call
		// the "close" method on the client to safely clean up any remaining background
		// resources.
		try (ImageAnnotatorClient client = ImageAnnotatorClient.create(settings)) {
			BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					System.out.format("Error: %s%n", res.getError().getMessage());
				} else {
					List<EntityAnnotation> lista = res.getTextAnnotationsList();
					if ( lista.size() > 0 ) {
						resultado = res.getTextAnnotationsList().getFirst().getDescription();
					} else {
						resultado = "DESCONOCIDO " + numDesconocido++;
					}
				}
			}
		}
		abrirVentana(bImagen, resultado);
		return resultado;
	}

	
	private void generarPDFs() {
		TreeSet<EsquemaPDF> cjtoEsquemas = new TreeSet<EsquemaPDF>(listaEsquemas);
		ArrayList<Alumno> alumnos = Alumno.getAlumnos(CURSO);
		while ( !cjtoEsquemas.isEmpty() ) {
			EsquemaPDF esquemaTop = cjtoEsquemas.removeFirst();
			generarPDF( esquemaTop );
			
			ArrayList<EsquemaPDF> listaEsquemasDeAlumno = mapaEsquemas.get(esquemaTop.getAlumnoPresunto());
			listaEsquemasDeAlumno.remove(esquemaTop);
			alumnos.remove(esquemaTop.getAlumnoPresunto());
			mapaEsquemas.remove(esquemaTop.getAlumnoPresunto());
			for ( EsquemaPDF esquema : listaEsquemasDeAlumno ) {
				cjtoEsquemas.remove(esquema);
				Alumno.getClosestAlumno( alumnos, esquema );
				cjtoEsquemas.add(esquema);
				insertarEnMapaEsquemas(esquema);
			}
		}
	}
	
	private void generarPDF( EsquemaPDF esquema ) {
		try ( PDDocument doc = new PDDocument() ) {
			int numPaginas = esquema.getFin() - esquema.getInicio() + 1;

			IntStream.range( esquema.getInicio(), esquema.getFin() + 1 ).forEachOrdered((pageNum) -> {doc.addPage( docOrigen.getPage( pageNum )); } );

			String strPdf = RUTA_BASE_DESTINO + esquema.getAlumnoPresunto().toString() + " calificación " + esquema.getOcrCalificacion() + " páginas " + numPaginas + ".pdf"; 
			Path f = Path.of( strPdf );
			if ( Files.exists( f )) {
				System.out.println("El fichero " + f.getFileName().toString() + " ya existe");
				System.exit(1);
			} else {
				try {
					doc.save( f.toFile() );
					totalPaginasGeneradas += numPaginas;
					totalFicherosGenerados++;
					System.out.println( String.format( "%s generado con %d paginas", strPdf, numPaginas ));
					sufijo++;
				} catch (Exception e) {
					System.out.println( "El fichero " + f.getFileName().toString() + " no se ha podido guardar");
					e.printStackTrace();
					System.exit(1);
				}
			}
		} catch ( IOException e ) {
			e.printStackTrace();
		}
	}
	
	private void insertarEnMapaEsquemas( EsquemaPDF esquema ) {
		ArrayList<EsquemaPDF> esquemasDeAlumno = mapaEsquemas.get( esquema.getAlumnoPresunto() ); 
		if ( esquemasDeAlumno == null ) {
			esquemasDeAlumno = new ArrayList<EsquemaPDF>();
			mapaEsquemas.put( esquema.getAlumnoPresunto(), esquemasDeAlumno);
		}
		esquemasDeAlumno.add( esquema );
	}
	
	private void completarEsquemas() {
		for ( EsquemaPDF esquema : listaEsquemas ) {
			try {
				esquema.setOcrNombre( 
				  googleOCR( 
				    esquema.getImagenPortada().getSubimage( 
				      esquema.getPuntoNombre().x - 100, 
				      esquema.getPuntoNombre().y - 100, 
				      ANCHO_NOMBRE, 
				      ALTO_NOMBRE) ).trim() ); 
				
				esquema.setOcrCalificacion( 
				  googleOCR( 
				    esquema.getImagenPortada().getSubimage( 
				      esquema.getPuntoCalificacion().x - 100, 
				      esquema.getPuntoCalificacion().y - 100, 
				      ANCHO_CALIFICACION + 100, 
				      ALTO_CALIFICACION + 100) ).trim() ); 
				Alumno.getClosestAlumno(CURSO, esquema);
				insertarEnMapaEsquemas( esquema );
			} catch ( Throwable t ) { t.printStackTrace(); }
		}
	}

	private Word findConTesseract( BufferedImage imagen, String patron ) {
	    List<Word> frases = mitesseract.getWords( imagen, TessPageIteratorLevel.RIL_TEXTLINE);
	    Word fraseEncontrada = null;
	    Iterator<Word> iterFrases = frases.iterator();
	    while ( fraseEncontrada == null && iterFrases.hasNext() ) {
	    	Word frase = iterFrases.next();
	    	if ( frase.getText().contains( patron ) ) {
	    		fraseEncontrada = frase;
	    	}
	    }
	    return fraseEncontrada;
	}
	
	private void generarEsquemasNumerosImagen() {
		pdfRenderer = new PDFRenderer(docOrigen);
		for (int pageNum = 0; pageNum < docOrigen.getNumberOfPages(); ++pageNum) {
//		for (int pageNum = 0; pageNum < 30; ++pageNum) {
		    BufferedImage imagen = null;
		    try {
		    	imagen = pdfRenderer.renderImageWithDPI(pageNum, 300, ImageType.RGB);
		    } catch ( Throwable t ) {} 
		    if ( imagen != null ) {
		    	if ( findConTesseract(imagen.getSubimage(192, 156, 1065, 228), PATRON_BUSQUEDA_PORTADA) != null ) {
			    	if ( listaEsquemas.size() > 0 ) {
			    		listaEsquemas.getLast().setFin(pageNum - 1);
			    	}
			    	listaEsquemas.add( new EsquemaPDF(pageNum) );
			    	listaEsquemas.getLast().setImagenPortada(imagen);
			    	/*
			    	if ( imagen.getWidth() > 800 && imagen.getHeight() > 600 ) {
				    	Word wEncontrado = findConTesseract(imagen.getSubimage( 0, 0, 615, 800 ), PATRON_BUSQUEDA_NOMBRE);
				    	if ( wEncontrado != null )
				    		listaEsquemas.getLast().setRectanguloNombre( wEncontrado.getBoundingBox() );
			    	}			    		
			    	else
			    		listaEsquemas.getLast().setRectanguloNombre( new Rectangle( X_NOMBRE_DEFAULT, Y_NOMBRE_DEFAULT, ANCHO_PATRON_NOMBRE, ALTO_NOMBRE) );
			    	*/
			    	Word wEncontrado = findConTesseract(imagen.getSubimage( 0, 0, 615, 800 ), PATRON_BUSQUEDA_NOMBRE);
			    	if ( wEncontrado != null ) {
				    	listaEsquemas.getLast().setPuntoNombre( 
		    				wEncontrado.getBoundingBox().x + wEncontrado.getBoundingBox().width - ANCHO_PATRON_NOMBRE,
		    				wEncontrado.getBoundingBox().y + wEncontrado.getBoundingBox().height - ALTO_PATRON_NOMBRE
				    	);
			    	}
			    	else
			    		listaEsquemas.getLast().setPuntoNombre( X_NOMBRE_DEFAULT, Y_NOMBRE_DEFAULT );
	
			    	wEncontrado = findConTesseract(imagen.getSubimage( 0, 0, 2200, 1800 ), PATRON_BUSQUEDA_CALIFICACION);
			    	if ( wEncontrado != null ) {
			    		// no se puede usar getBundingBox directamente, porque el cuadro no sólo contiene a la palabra
				    	listaEsquemas.getLast().setPuntoCalificacion( 
		    				wEncontrado.getBoundingBox().x + wEncontrado.getBoundingBox().width - ANCHO_PATRON_CALIFICACION,
		    				wEncontrado.getBoundingBox().y + wEncontrado.getBoundingBox().height - ALTO_PATRON_CALIFICACION
				    	);
			    	}
			    	else
			    		listaEsquemas.getLast().setPuntoCalificacion( X_CALIFICACION_DEFAULT, Y_CALIFICACION_DEFAULT );
			    	
			    	System.out.println( "Nuevo esquema en " + pageNum + ": " );
			    }
		    }
		}
		listaEsquemas.getLast().setFin( docOrigen.getNumberOfPages() - 1);
	}
	

	private void analizar() {
		pdfRenderer = new PDFRenderer(docOrigen);
		try {
			BufferedImage imagen = pdfRenderer.renderImageWithDPI(16,  300, ImageType.RGB);
			imagen = imagen.getSubimage(0, 0, 1700, 500);
			mitesseract.getWords(imagen, TessPageIteratorLevel.RIL_TEXTLINE);
			JOptionPane.showMessageDialog(null, "", "", JOptionPane.PLAIN_MESSAGE, new ImageIcon(imagen));
		    List<Word> frases = mitesseract.getWords( imagen, TessPageIteratorLevel.RIL_TEXTLINE);
		    System.out.println( frases.stream().map(Word::getText).collect(Collectors.joining("\n")) );
		} catch (Exception e) {e.printStackTrace();}
	}
}
