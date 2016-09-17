import edu.pitt.IRWebProject.language.service.BingTranslatorService;

/**
 * @author Ian
 * @JavaVersion 1.8
 * @ClassName: BingTranslatorTest
 * @date Dec 6, 2015 12:46:04 PM
 */
public class BingTranslatorTest {
    public static void main(String[] args) throws Exception {
        BingTranslatorService trans = new BingTranslatorService("CHINESE_SIMPLIFIED", "ENGLISH");
        String str = "你好";
        String translatedStr = trans.translateQuery(str);
        System.out.println(translatedStr);
        // trans.translate();
    }

}