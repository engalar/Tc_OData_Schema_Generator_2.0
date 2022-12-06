package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.ValidateNamespace;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.ODataSchema;
import schemagenerator.proxies.OdataObject;
import testing.internal.TestingContext;

public class ValidateNamespaceTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void validNamespace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);

		try 
		{
			ValidateNamespace validNS = new ValidateNamespace(context, odataSchema.getMendixObject());
			validNS.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateNamespaceTest:validNamespace failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidNamespace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		try 
		{
			ValidateNamespace validNS = new ValidateNamespace(context, odataSchema.getMendixObject());
			validNS.executeAction();
			Assert.fail("Test ValidateNamespaceTest:invalidNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please add atlease one new namespace in the service.", e.getMessage());
		}
	}
	
	@Test
	public void invalidNamespaceOnlyInheritedNamespace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace_Base");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(true);

		try 
		{
			ValidateNamespace validNS = new ValidateNamespace(context, odataSchema.getMendixObject());
			validNS.executeAction();
			Assert.fail("Test ValidateNamespaceTest:invalidNamespaceOnlyInheritedNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please add atlease one new namespace in the service.", e.getMessage());
		}
	}
}
