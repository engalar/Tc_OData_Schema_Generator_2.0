package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.ValidateEntityName;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.OdataObject;
import testing.internal.TestingContext;

public class ValidateEntityNameTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void valideEntityName()
	{
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		try 
		{
			ValidateEntityName validEntityName = new ValidateEntityName(context, contractNS.getMendixObject());
			validEntityName.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateEntityNameTest:valideEntityName failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void sameEntityNameInDifferentNamespace()
	{
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		ContractNameSpaces contractNS1 = new ContractNameSpaces(context);
		contractNS1.setContractNameSpace("TestNamespace1");
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace1.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS1);
		
		try 
		{
			ValidateEntityName validEntityName = new ValidateEntityName(context, contractNS1.getMendixObject());
			validEntityName.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateEntityNameTest:sameEntityNameDifferentNS failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void sameEntityNameInSameNamespace()
	{
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		try 
		{
			ValidateEntityName validEntityName = new ValidateEntityName(context, contractNS.getMendixObject());
			validEntityName.executeAction();
			Assert.fail("Test ValidateEntityNameTest:sameEntityNameInSameNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Same Odata Name for the objects " + "TestNamespace.TestName" + " and "  + "TestNamespace.TestName" + ".\r\n" + "Please give a unique Odata Name.", e.getMessage());
		}
	}
}
