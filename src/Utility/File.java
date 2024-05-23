package Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Paths;
import javax.servlet.http.Part;

import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import net.jcip.annotations.ThreadSafe;

@ThreadSafe
public class File {

	public static String fileType(String file) {
		try {
			BodyContentHandler handler = new BodyContentHandler();
			Metadata metadata = new Metadata();
			FileInputStream content = new FileInputStream(file);
			Parser parser = new AutoDetectParser();
			parser.parse(content, handler, metadata, new ParseContext());

			// Ottenere il tipo di contenuto dai metadati
			String contentType = metadata.get(Metadata.CONTENT_TYPE);
			System.out.println("file type: " + contentType);

			return contentType;
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error: file type analysis");
			return null;
		}
	}

	public static String getFilePath(Part filePart) {
		String contentDisposition = filePart.getHeader("content-disposition");
		String[] tokens = contentDisposition.split(";");
		for (String token : tokens) {
			if (token.trim().startsWith("filename")) {
				return token.substring(token.indexOf("=") + 2, token.length() - 1);
			}
		}
		return null;
	}

	public static String getFileNameToPath(String filePath) {
		return Paths.get(filePath).getFileName().toString();
	}

	public static boolean containsScript(Part filePart) throws IOException {
		InputStream inputStream = filePart.getInputStream();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			String line;
			while ((line = reader.readLine()) != null) {
				// Cerca la presenza di script all'interno di ogni riga
				if (line.contains("<script>") || line.contains("</script>")) {
					return true;
				}
			}
		}
		return false;
	}

}
