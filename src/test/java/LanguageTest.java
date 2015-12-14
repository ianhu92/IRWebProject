import com.memetix.mst.language.Language;

public class LanguageTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Language language = Language.valueOf("ENGLISH");
		Language language2 = Language.valueOf("CHINESE_SIMPLIFIED");
		System.out.println(language);
		System.out.println(language2);
	}

}
