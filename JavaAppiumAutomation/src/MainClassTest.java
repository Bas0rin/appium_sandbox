import org.junit.Assert;
import org.junit.internal.runners.statements.Fail;

public class MainClassTest
{
    public int testGetLocalNumber(int number)
    {
        if (number == 14)
        {
            System.out.println("Success getLocalNumber is 14");
        } else if (number != 14)
        {
            System.out.println("Fail getLocalNumber != 14");
        }
        return number;
    }

    public int testGetClassNumber (int number_2)
    {
        if (number_2 > 45)
        {
            System.out.println("classNumber > 45");
        } else if (number_2 < 45)
        {
            System.out.println("classNumber < 45");
        } else if (number_2 == 45) {
            System.out.println("classNumber = 45");
        }
        return number_2;
    }


    public String testGetClassString (String str)
    {
        String substring = "Hello";
        if (str.toLowerCase().contains(substring.toLowerCase()))
        {
            System.out.println("Class_string contains \"hello\" ");
        } else if (str.toUpperCase().contains(substring.toUpperCase()))
        {
            System.out.println("Class_string contains \"Hello\" ");
        } else {
            Assert.fail("Class_string not contains \"Hello\" ");
        }
        return str;
    }
}
