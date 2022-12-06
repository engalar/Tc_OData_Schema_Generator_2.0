package testing.internal.test;

import java.io.FileNotFoundException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import schemagenerator.actions.ValidateSchema;
import schemagenerator.proxies.SubType;
import testing.internal.TestingContext;

public class ValidateSchemaTest 
{
	private IContext context;
	
	@Before 
	public void setUp() throws FileNotFoundException, CoreException
	{
		context   = TestingContext.getTestContext();
		TestUtils.teamcenterLogin(context);
	}
	
	@After
	public void tearDown()
	{
		TestUtils.teamcenterLogout(context);
	}
	
	@Test
	public void validSchema() throws Exception
	{
		SubType subType = new SubType(context);
		subType.setsubTypeNames("ItemRevision");
		subType.setsubTypeDisplayName("Item Revision");
		subType.setisDataset(false);
				
		Core.commit(context, subType.getMendixObject());
		
		try 
		{
			ValidateSchema validSchema = new ValidateSchema(context, "{\r\n" + 
					"    \"externalReferences\": [\r\n" + 
					"        {\r\n" + 
					"            \"uri\": \"http://localhost/Org.OData.Capabilities.V1/v4.0/Org.OData.Capabilities.V1.xml\",\r\n" + 
					"            \"namespaces\": [\r\n" + 
					"                {\r\n" + 
					"                    \"name\": \"Org.OData.Capabilities.V1\",\r\n" + 
					"                    \"alias\": \"Capabilities\"\r\n" + 
					"                }\r\n" + 
					"            ]\r\n" + 
					"        }\r\n" + 
					"    ],\r\n" + 
					"    \"dataServices\": [\r\n" + 
					"        {\r\n" + 
					"            \"enumInfo\": [],\r\n" + 
					"            \"schemaNameSpace\": \"NS1\",\r\n" + 
					"            \"entityInfo\": [\r\n" + 
					"                {\r\n" + 
					"                    \"teamcenterTypeName\": \"ItemRevision\",\r\n" + 
					"                    \"attributeInfo\": [\r\n" + 
					"                        {\r\n" + 
					"                            \"teamcenterAttrName\": \"object_name\",\r\n" + 
					"                            \"isReadOnly\": false,\r\n" + 
					"                            \"inherited\": false,\r\n" + 
					"                            \"autoAdded\": false,\r\n" + 
					"                            \"isCollection\": false,\r\n" + 
					"                            \"attributeName\": \"Name\",\r\n" + 
					"                            \"autoReadOnly\": false,\r\n" + 
					"                            \"isKeyProp\": false,\r\n" + 
					"                            \"isRuntime\": false,\r\n" + 
					"                            \"attributeDataType\": \"String\"\r\n" + 
					"                        }\r\n" + 
					"                    ],\r\n" + 
					"                    \"oDataEntityName\": \"NS1.Item_Revision\",\r\n" + 
					"                    \"stream\": false,\r\n" + 
					"                    \"autoAdded\": false,\r\n" + 
					"                    \"actionInfo\": [],\r\n" + 
					"                    \"exposeObject\": true\r\n" + 
					"                }\r\n" + 
					"            ],\r\n" + 
					"            \"actionInfo\": [],\r\n" + 
					"            \"complexTypeInfo\": []\r\n" + 
					"        }\r\n" + 
					"    ]\r\n" + 
					"}");
			validSchema.executeAction();
			subType.delete();
		}
		catch (Exception e) {
			subType.delete();
			Assert.fail("Test ValidateSchemaTest:validSchema failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalidTeamcenterObject()
	{
		try 
		{
			ValidateSchema validSchema = new ValidateSchema(context, "{\r\n" + 
					"    \"externalReferences\": [\r\n" + 
					"        {\r\n" + 
					"            \"uri\": \"http://localhost/Org.OData.Capabilities.V1/v4.0/Org.OData.Capabilities.V1.xml\",\r\n" + 
					"            \"namespaces\": [\r\n" + 
					"                {\r\n" + 
					"                    \"name\": \"Org.OData.Capabilities.V1\",\r\n" + 
					"                    \"alias\": \"Capabilities\"\r\n" + 
					"                }\r\n" + 
					"            ]\r\n" + 
					"        }\r\n" + 
					"    ],\r\n" + 
					"    \"dataServices\": [\r\n" + 
					"        {\r\n" + 
					"            \"enumInfo\": [],\r\n" + 
					"            \"schemaNameSpace\": \"NS1\",\r\n" + 
					"            \"entityInfo\": [\r\n" + 
					"                {\r\n" + 
					"                    \"teamcenterTypeName\": \"TestItemRevision\",\r\n" + 
					"                    \"attributeInfo\": [\r\n" + 
					"                        {\r\n" + 
					"                            \"teamcenterAttrName\": \"object_name\",\r\n" + 
					"                            \"isReadOnly\": false,\r\n" + 
					"                            \"inherited\": false,\r\n" + 
					"                            \"autoAdded\": false,\r\n" + 
					"                            \"isCollection\": false,\r\n" + 
					"                            \"attributeName\": \"Name\",\r\n" + 
					"                            \"autoReadOnly\": false,\r\n" + 
					"                            \"isKeyProp\": false,\r\n" + 
					"                            \"isRuntime\": false,\r\n" + 
					"                            \"attributeDataType\": \"String\"\r\n" + 
					"                        }\r\n" + 
					"                    ],\r\n" + 
					"                    \"oDataEntityName\": \"NS1.TestItem_Revision\",\r\n" + 
					"                    \"stream\": false,\r\n" + 
					"                    \"autoAdded\": false,\r\n" + 
					"                    \"actionInfo\": [],\r\n" + 
					"                    \"exposeObject\": true\r\n" + 
					"                }\r\n" + 
					"            ],\r\n" + 
					"            \"actionInfo\": [],\r\n" + 
					"            \"complexTypeInfo\": []\r\n" + 
					"        }\r\n" + 
					"    ]\r\n" + 
					"}");
			validSchema.executeAction();
			Assert.fail("Test ValidateSchemaTest:invalidTeamcenterObject failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("The teamcenter object " + "TestItemRevision" + " does not exists", e.getMessage());
		}
	}
	
	@Test
	public void invalidTeamcenterProperty() throws CoreException
	{
		SubType subType = new SubType(context);
		subType.setsubTypeNames("ItemRevision");
		subType.setsubTypeDisplayName("Item Revision");
		subType.setisDataset(false);
				
		Core.commit(context, subType.getMendixObject());
		
		try 
		{
			ValidateSchema validSchema = new ValidateSchema(context, "{\r\n" + 
					"    \"externalReferences\": [\r\n" + 
					"        {\r\n" + 
					"            \"uri\": \"http://localhost/Org.OData.Capabilities.V1/v4.0/Org.OData.Capabilities.V1.xml\",\r\n" + 
					"            \"namespaces\": [\r\n" + 
					"                {\r\n" + 
					"                    \"name\": \"Org.OData.Capabilities.V1\",\r\n" + 
					"                    \"alias\": \"Capabilities\"\r\n" + 
					"                }\r\n" + 
					"            ]\r\n" + 
					"        }\r\n" + 
					"    ],\r\n" + 
					"    \"dataServices\": [\r\n" + 
					"        {\r\n" + 
					"            \"enumInfo\": [],\r\n" + 
					"            \"schemaNameSpace\": \"NS1\",\r\n" + 
					"            \"entityInfo\": [\r\n" + 
					"                {\r\n" + 
					"                    \"teamcenterTypeName\": \"ItemRevision\",\r\n" + 
					"                    \"attributeInfo\": [\r\n" + 
					"                        {\r\n" + 
					"                            \"teamcenterAttrName\": \"test_object_name\",\r\n" + 
					"                            \"isReadOnly\": false,\r\n" + 
					"                            \"inherited\": false,\r\n" + 
					"                            \"autoAdded\": false,\r\n" + 
					"                            \"isCollection\": false,\r\n" + 
					"                            \"attributeName\": \"Name\",\r\n" + 
					"                            \"autoReadOnly\": false,\r\n" + 
					"                            \"isKeyProp\": false,\r\n" + 
					"                            \"isRuntime\": false,\r\n" + 
					"                            \"attributeDataType\": \"String\"\r\n" + 
					"                        }\r\n" + 
					"                    ],\r\n" + 
					"                    \"oDataEntityName\": \"NS1.Item_Revision\",\r\n" + 
					"                    \"stream\": false,\r\n" + 
					"                    \"autoAdded\": false,\r\n" + 
					"                    \"actionInfo\": [],\r\n" + 
					"                    \"exposeObject\": true\r\n" + 
					"                }\r\n" + 
					"            ],\r\n" + 
					"            \"actionInfo\": [],\r\n" + 
					"            \"complexTypeInfo\": []\r\n" + 
					"        }\r\n" + 
					"    ]\r\n" + 
					"}");
			validSchema.executeAction();
			subType.delete();
			Assert.fail("Test ValidateSchemaTest:invalidTeamcenterProperty failed:\n");
		}
		catch (Exception e) {
			subType.delete();
			Assert.assertEquals("The property " + "test_object_name" + " for the teamcenter object " + "ItemRevision" + " does not exists", e.getMessage());
		}
	}
}
