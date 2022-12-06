package testing.internal.test;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import schemagenerator.actions.SplitString;
import testing.internal.TestingContext;

public class SplitStringTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void getSplitString() throws CoreException
	{
		try
		{
			SplitString splitStr = new SplitString(context, "Test_Name_String", "_");
			List<IMendixObject> result = splitStr.executeAction();
			Assert.assertEquals("Test", result.get(0).getMember(context, "Value").getValue(context));
			Assert.assertEquals("Name", result.get(1).getMember(context, "Value").getValue(context));
			Assert.assertEquals("String", result.get(2).getMember(context, "Value").getValue(context));
			
		}catch (Exception e) {
			Assert.fail("Test SplitStringTest:getSplitString failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void getSplitStringUsingBrackets() throws CoreException
	{
		try
		{
			SplitString splitStr = new SplitString(context, "Test.Name.String", "[.]");
			List<IMendixObject> result = splitStr.executeAction();
			Assert.assertEquals("Test", result.get(0).getMember(context, "Value").getValue(context));
			Assert.assertEquals("Name", result.get(1).getMember(context, "Value").getValue(context));
			Assert.assertEquals("String", result.get(2).getMember(context, "Value").getValue(context));
			
		}catch (Exception e) {
			Assert.fail("Test SplitStringTest:getSplitStringUsingBrackets failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidString() throws CoreException
	{
		try
		{
			SplitString splitStr = new SplitString(context, "", "_");
			splitStr.executeAction();
			Assert.fail("Test SplitStringTest:getSplitStringUsingBrackets failed:\n");
			
		}catch (Exception e) {
			Assert.assertEquals("The string provided to SplitString action cannot be blank or empty.", e.getMessage());
		}
	}
}
