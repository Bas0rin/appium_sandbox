import org.junit.Assert;
import org.junit.Test;

public class MainClass extends MainClassTest
{
    private int class_number = 45;
    private String class_string = "world";

    @Test
    public void getLocalNumber()
    {
        int a = testGetLocalNumber(14);
        System.out.println(a);
    }

    @Test
    public void getClassNumber()
    {
        int b = testGetClassNumber(this.class_number);

    }

    @Test
    public void getClassString()
    {
        String c = testGetClassString(this.class_string);

    }

}