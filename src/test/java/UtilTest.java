/**
 * Created by vinaymavi on 19/12/15.
 */
import junit.framework.TestCase;
import org.apache.commons.lang3.text.WordUtils;

public class UtilTest extends TestCase {
    public void testCapitalize(){
        String name = "ABHYUDAYA COOPERATIVE BANK LIMITED";
        String nameSmall = name.toLowerCase();
        String nameCapitalize = WordUtils.capitalize(nameSmall);
        assertEquals("Abhyudaya Cooperative Bank Limited",nameCapitalize);
    }
}
