package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.ValidateService;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.ODataSchema;
import schemagenerator.proxies.OData_Properties;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.Properties;
import testing.internal.TestingContext;

public class ValidateServiceTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void validService()
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
		property.setODataName("TestPropertyStr");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		
		property = new Properties(context);
		property.setODataName("TestPropertyInt");
		property.setODataTypeValue(OData_Properties.Integer);
		property.setProperties_OdataObject(Object);
		
		
		property = new Properties(context);
		property.setODataName("TestPropertyRelation");
		property.setODataTypeValue(OData_Properties.Relation);
		property.setProperties_OdataObject(Object);
		property.setreferenceType("TestNamespace.TestName_Relation");
		property.setTcName("TestPropertyRelation");
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateServiceTest:validService failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidServiceName()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("Test !Schema");
		odataSchema.setmajorVersion("v1");
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidateServiceTest:invalidServiceName failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Invalid Name " + "Test !Schema" + "\nNames can also contain letters, numbers and (_)underscore character. Spaces or special characters in names are not allowed.", e.getMessage());
		}
	}
	
	@Test
	public void invalidService_NoNamespace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidateServiceTest:invalidService_NoNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please add at lease one new namespace in the service.", e.getMessage());
		}
	}
	
	@Test
	public void invalidService_NoObjectInNamespace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidateServiceTest:invalidService_NoNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please add at least one Odata object.", e.getMessage());
		}
	}
	
	@Test
	public void invalidService_duplicateNamespace()
	{
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidateServiceTest:invalidService_duplicateNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Duplicate namespace " + "TestNamespace" + " exists in the service.", e.getMessage());
		}
	}
	
	@Test
	public void invalidService_duplicateEntityName()
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
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidateEntityNameTest:invalidService_duplicateEntityName failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Same Odata Name for the objects " + "TestNamespace.TestName" + " and "  + "TestNamespace.TestName" + ".\r\n" + "Please give a unique Odata Name.", e.getMessage());
		}
	}
	
	@Test
	public void invalidService_duplicateProperty()
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
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		
		property = new Properties(context);
		property.setODataName("TestProperty");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidatePropertyTest:invalidService_duplicateProperty failed:\n");
		}
		catch (Exception e) {
			 Assert.assertEquals("Duplicate property name " + "TestProperty" + " for the Object " +  "TestNamespace.TestName", e.getMessage());
		}
	}
	
	@Test
	public void invalidService_invalidReferenceProp()
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
		property.setreferenceType("");
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropName");
		
		try 
		{
			ValidateService validProperty = new ValidateService(context, odataSchema.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidatePropertyTest:invalidService_invalidReferenceProp failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please select valid reference object for property " + "TestPropName", e.getMessage());
		}
	}
}
