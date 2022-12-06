package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.NamingRules;
import testing.internal.TestingContext;

public class NamingRulesTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void valideName()
	{
		// Test for valid name
		NamingRules helper = new NamingRules( context, "TestName");
		try {
			helper.executeAction();
		} catch (Exception e) {
			Assert.fail("Test NamingRulesTest:valideName failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void outOfRangeName()
	{
		// Test for valid name
		String Name = "TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_"
				+ "TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName_TestName";
		NamingRules helper = new NamingRules( context, Name);
		try {
			helper.executeAction();
			Assert.fail("Test NamingRulesTest:outOfRangeName failed\n");
		} catch (Exception e) {
			Assert.assertEquals("Invalid Name " + Name + "\nNames length is out of range.", e.getMessage());
		}
	}
	
	@Test
	public void invalidFirstCharInName()
	{
		// Test for valid name
		String Name = "!TestName";
		NamingRules helper = new NamingRules( context, Name);
		try {
			helper.executeAction();
			Assert.fail("Test NamingRulesTest:invalidFirstCharInName failed\n");
		} catch (Exception e) {
			Assert.assertEquals("Invalid Name " + Name + "\nNames must begin with a letter of the alphabet or number.", e.getMessage());
		}
	}
	
	@Test
	public void specialCharsInName()
	{
		// Test for valid name
		String Name = "Test Name$";
		NamingRules helper = new NamingRules( context, Name);
		try {
			helper.executeAction();
			Assert.fail("Test NamingRulesTest:specialCharsInName failed\n");
		} catch (Exception e) {
			Assert.assertEquals("Invalid Name " + Name + "\nNames can also contain letters, numbers and (_)underscore character. Spaces or special characters in names are not allowed.", e.getMessage());
		}
	}
}
