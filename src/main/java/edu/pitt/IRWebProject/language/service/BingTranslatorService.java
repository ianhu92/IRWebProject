package edu.pitt.IRWebProject.language.service;

import org.springframework.stereotype.Service;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * @JavaVersion 1.8
 * @ClassName: bingTranslator
 * @Description: TODO
 * @author Siwei Liu
 * @date Dec 6, 2015 12:46:04 PM
 *
 */

@Service
public class BingTranslatorService {
	private Language originLan;
	private Language destLan;
	final int MAXBYTES = 10240;
	int bytesPerChar;
	String delimeter;

	public BingTranslatorService() {
		Translate.setClientId("ianir");
		Translate.setClientSecret("dUap2bXo4ZYJ2JMxiHmQzA/CI54e55lNnGY+Gpx+Lr8=");
	}

	public BingTranslatorService(String fromName, String toName) {
		Translate.setClientId("ianir");
		Translate.setClientSecret("dUap2bXo4ZYJ2JMxiHmQzA/CI54e55lNnGY+Gpx+Lr8=");
		this.setOriginLan(Language.valueOf(fromName));
		this.setDestLan(Language.valueOf(toName));
	}

	public Language getOriginLan() {
		return originLan;
	}

	public void setOriginLan(Language originLan) {
		this.originLan = originLan;
		if (originLan == Language.CHINESE_SIMPLIFIED) {
			bytesPerChar = 6;
			delimeter = "。";
		} else {
			bytesPerChar = 1;
			delimeter = ".";
		}
	}

	public Language getDestLan() {
		return destLan;
	}

	public void setDestLan(Language destLan) {
		this.destLan = destLan;
		if (destLan == Language.CHINESE_SIMPLIFIED) {
			bytesPerChar = 6;
			delimeter = "。";
		} else {
			bytesPerChar = 1;
			delimeter = ".";
		}
	}

	/**
	 * translate given text
	 * 
	 * @param line
	 * @return
	 * @throws Exception
	 */
	public String processLargeText(String line) throws Exception {
		StringBuilder output = new StringBuilder();
		while (line != null) {
			int pos = line.lastIndexOf(delimeter, MAXBYTES / bytesPerChar);
			String tmp = Translate.execute(line.substring(0, pos + 1), originLan, destLan);
			output.append(tmp);
			if (pos + 1 < line.length()) {
				line = line.substring(pos + 1);
			} else {
				line = null;
			}
		}
		return output.toString();
	}

	/*
	 * public void translate() throws Exception {
	 * String inputFile = "data//questions.txt";
	 * String outputFile = "data//questions_trans.txt";
	 * File file = new File(inputFile);
	 * File dest = new File(outputFile);
	 * try {
	 * BufferedReader reader = new BufferedReader(
	 * new InputStreamReader(new FileInputStream(file), "utf-8"));
	 * BufferedWriter writer = new BufferedWriter(
	 * new OutputStreamWriter(new FileOutputStream(dest), "utf-8"));
	 * String line = "";
	 * while ((line = reader.readLine()) != null) {
	 * String string = "";
	 * if (line.getBytes().length < MAXBYTES) {
	 * string = Translate.execute(line, originLan, destLan);
	 * } else {
	 * string = processLargeText(line);
	 * }
	 * writer.write(string + "\r\n");
	 * writer.flush();
	 * }
	 * writer.flush();
	 * reader.close();
	 * writer.close();
	 * } catch (FileNotFoundException e) {
	 * e.printStackTrace();
	 * } catch (IOException e) {
	 * e.printStackTrace();
	 * }
	 * }
	 */

	public String translateQuery(String query) {
		String str = null;
		if (query != null && query.length() != 0) {
			try {
				if (query.getBytes().length < MAXBYTES / bytesPerChar) {
					str = Translate.execute(query, originLan, destLan);
				} else {
					str = processLargeText(query);
				}
			} catch (Exception e) {
				System.out.println("Translate Query Error" + e.getMessage());
			}
		}
		return str;
	}

	public static void main(String[] args) throws Exception {
		BingTranslatorService trans = new BingTranslatorService("CHINESE_SIMPLIFIED", "ENGLISH");
		String str = "你好";
		String translatedStr = trans.translateQuery(str);
		System.out.println(translatedStr);
		// trans.translate();
	}

}