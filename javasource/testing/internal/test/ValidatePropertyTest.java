package testing.internal.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;

import schemagenerator.actions.ValidateProperty;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.ODataSchema;
import schemagenerator.proxies.OData_Properties;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.Properties;
import testing.internal.TestingContext;

public class ValidatePropertyTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void validStringProperty()
	{
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setTcName("TestProperty");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		
		try 
		{
			ValidateProperty validProperty = new ValidateProperty(context, property.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidatePropertyTest:validStringProperty failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void validIntegerProperty()
	{
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setTcName("TestProperty");
		property.setODataTypeValue(OData_Properties.Integer);
		property.setProperties_OdataObject(Object);
		
		try 
		{
			ValidateProperty validProperty = new ValidateProperty(context, property.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidatePropertyTest:validIntegerProperty failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void duplicateProperty()
	{
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setTcName("TestProperty");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		
		Properties property1 = new Properties(context);
		property1.setODataName("TestProperty");
		property1.setTcName("TestProperty1");
		property1.setODataTypeValue(OData_Properties.String);
		property1.setProperties_OdataObject(Object);
		
		try 
		{
			ValidateProperty validProperty = new ValidateProperty(context, property1.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidatePropertyTest:duplicateProperty failed:\n");
		}
		catch (Exception e) {
			 Assert.assertEquals("Duplicate property name " + "TestProperty" + " for the Object " + Object.getMendixObject().getValue(context, "ODataName"), e.getMessage());
		}
	}
	
	@Test
	public void validRelationProperty()
	{
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName_Relation");
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setTcName("TestProperty");
		property.setODataTypeValue(OData_Properties.Relation);
		property.setProperties_OdataObject(Object);
		property.setreferenceType("TestNamespace.TestName_Relation");
		
		try 
		{
			ValidateProperty validProperty = new ValidateProperty(context, property.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidatePropertyTest:validRelationProperty failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void validReferenceProperty()
	{
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName_Reference");
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setTcName("TestProperty");
		property.setODataTypeValue(OData_Properties.Reference);
		property.setProperties_OdataObject(Object);
		property.setreferenceType("TestNamespace.TestName_Reference");
		
		try 
		{
			ValidateProperty validProperty = new ValidateProperty(context, property.getMendixObject());
			validProperty.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidatePropertyTest:validReferenceProperty failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidReferenceProperty()
	{
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		
		Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName_Reference");
		
		Properties property = new Properties(context);
		property.setODataName("TestProperty");
		property.setODataTypeValue(OData_Properties.Reference);
		property.setreferenceType("");
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropName");
		
		try 
		{
			ValidateProperty validProperty = new ValidateProperty(context, property.getMendixObject());
			validProperty.executeAction();
			Assert.fail("Test ValidatePropertyTest:invalidReferenceProperty failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Please select valid reference object for property " + "TestPropName", e.getMessage());
		}
	}
}
