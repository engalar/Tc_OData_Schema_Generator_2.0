package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.ValidateContractNamespace;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.ODataSchema;
import testing.internal.TestingContext;

public class ValidateContractNamespaceTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void valideContractNameSpace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		try 
		{
			ValidateContractNamespace validNS = new ValidateContractNamespace(context, contractNS.getMendixObject());
			validNS.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateContractNamespaceTest:valideContractNameSpace failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidContractNameSpaceName()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("Test !Namespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		try 
		{
			ValidateContractNamespace validNS = new ValidateContractNamespace(context, contractNS.getMendixObject());
			validNS.executeAction();
			Assert.fail("Test ValidateContractNamespaceTest:invalidContractNameSpaceName failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Invalid Name " + "Test !Namespace" + "\nNames can also contain letters, numbers and (_)underscore character. Spaces or special characters in names are not allowed.", e.getMessage());
		}
	}
	
	@Test
	public void duplicateContractNameSpaceName()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		String contractNSName = "TestNamespace";
		ContractNameSpaces contractNSFirst = new ContractNameSpaces(context);
		contractNSFirst.setContractNameSpace(contractNSName);
		contractNSFirst.setContractNameSpaces_ODataSchema(odataSchema);
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace(contractNSName);
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
				
		try 
		{
			ValidateContractNamespace validNS = new ValidateContractNamespace(context, contractNS.getMendixObject());
			validNS.executeAction();
			Assert.fail("Test ValidateContractNamespaceTest:duplicateContractNameSpaceName failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Namespace cannot be created as " + contractNSName + " namespace already exists in the service.", e.getMessage());
		}
	}
}
