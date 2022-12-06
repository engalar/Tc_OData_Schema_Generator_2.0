package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.ValidateReferenceType;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.ODataSchema;
import schemagenerator.proxies.OData_Properties;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.Properties;
import testing.internal.TestingContext;

public class ValidateReferenceTypeTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void validRelationProperty()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName_Relation");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setODataTypeValue(OData_Properties.Relation);
		property.setProperties_OdataObject(Object);
		property.setreferenceType("TestNamespace.TestName_Relation");
		
		try 
		{
			ValidateReferenceType validProperty = new ValidateReferenceType(context, odataSchema.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateReferenceTypeTest:validRelationProperty failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void validReferenceProperty()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName_Reference");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setODataTypeValue(OData_Properties.Reference);
		property.setProperties_OdataObject(Object);
		property.setreferenceType("TestNamespace.TestName_Reference");
		
		try 
		{
			ValidateReferenceType validProperty = new ValidateReferenceType(context, odataSchema.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateReferenceTypeTest:validReferenceProperty failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidRelationProperty()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName_Reference");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setODataTypeValue(OData_Properties.Relation);
		property.setreferenceType("");
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropName");
		
		try 
		{
			ValidateReferenceType validProperty = new ValidateReferenceType(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidateReferenceTypeTest:invalidRelationProperty failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please select valid reference object for property " + property.getMendixObject().getMember(context, "TcName"), e.getMessage());
		}
	}
}
