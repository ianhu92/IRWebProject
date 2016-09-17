package edu.pitt.IRWebProject.language.service;

import org.springframework.stereotype.Service;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;

/**
 * @author Siwei Liu
 * @JavaVersion 1.8
 * @ClassName: bingTranslator
 * @Description: TODO
 * @date Dec 6, 2015 12:46:04 PM
 */

@Service
public class BingTranslatorService {
    private Language originLan;
    private Language destLan;
    final int MAXBYTES = 10240;
    int bytesPerChar;
    String delimeter;

    public BingTranslatorService() {
        /*
		 * Translate.setClientId("irweb");
		 * Translate.setClientSecret("+V7O+j5pSITT5yW14yMDpc3fOV6mMO/ryfFes6mPVdM=");
		 */

        Translate.setClientId("ianir");
        Translate.setClientSecret("dUap2bXo4ZYJ2JMxiHmQzA/CI54e55lNnGY+Gpx+Lr8=");

		/*
		 * Translate.setClientId("irproject");
		 * Translate.setClientSecret("XACoemwSVv/FBKYaSY7UJI48yAWi75wQKmdDZl5Rxt8=");
		 */
		/*
		 * Translate.setClientId("siweiliu42");
		 * Translate.setClientSecret("DmPmwMFLun2qPQL7JoZ3YjocpK0REDnnnJ81oOTW1s8=");
		 */
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
            if (pos == -1) {
                pos = Math.min(MAXBYTES / bytesPerChar, line.length() - 1);
            }
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

    public void printLineNum(int num) {
        if (num % 100 == 0) {
            System.out.println(num);
        }
    }

    public String translateQuery(String query) {
        String str = null;
        if (query != null && query.length() != 0) {
            while (str == null) {// keep translating if failed
                try {
                    if (query.getBytes().length < MAXBYTES / bytesPerChar) {
                        str = Translate.execute(query, originLan, destLan);
                    } else {
                        str = processLargeText(query);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }
}