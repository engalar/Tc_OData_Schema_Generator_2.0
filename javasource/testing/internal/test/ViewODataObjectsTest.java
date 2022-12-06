package testing.internal.test;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import schemagenerator.actions.ViewODataObjects;
import schemagenerator.proxies.ContractNameSpaceDropDown;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.ODataSchema;
import schemagenerator.proxies.OData_Properties;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.Properties;
import schemagenerator.proxies.SubType;
import testing.internal.TestingContext;

public class ViewODataObjectsTest 
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
	public void getOdataObjects() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(false);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("TestName");
		
		SubType subType = new SubType(context);
		subType.setsubTypeNames("TestName");
		subType.setsubTypeDisplayName("Test Display");
		subType.setisDataset(false);
		Core.commit(context, subType.getMendixObject());
		
		Properties property = new Properties(context);
		property.setODataName("TestPropertyStr");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropertyNameStr");
		property.setTcDisplayName("Test Property NameStr");
		
		property = new Properties(context);
		property.setODataName("TestPropertyInt");
		property.setODataTypeValue(OData_Properties.Integer);
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropertyNameInt");
		property.setTcDisplayName("Test Property NameInt");
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			
			List<IMendixObject> expecteds_objects = Core.retrieveByPath(context, contractNS.getMendixObject(), "SchemaGenerator.OdataObject_ContractNameSpaces");
			Assert.assertEquals(expecteds_objects, actuals_objects);
			subType.delete();
		}
		catch (Exception e) {
			subType.delete();
			Assert.fail("Test ViewODataObjectsTest:getOdataObjects failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void getOdataObjectsInheritance() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS_Base = new ContractNameSpaces(context);
		contractNS_Base.setContractNameSpace("TestNamespace_Base");
		contractNS_Base.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS_Base.setInheritedObject(true);
		
		OdataObject Object_Base= new OdataObject(context);
		Object_Base.setODataName("TestNamespace_Base.WorkspaceObject");
		Object_Base.setOdataObject_ContractNameSpaces(contractNS_Base);
		Object_Base.setTcName("WorkspaceObject");
		
		Properties property_Base = new Properties(context);
		property_Base.setODataName("Name");
		property_Base.setODataTypeValue(OData_Properties.String);
		property_Base.setProperties_OdataObject(Object_Base);
		property_Base.setTcName("object_name");
		property_Base.setTcDisplayName("Name");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(false);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.ItemRevision");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("ItemRevision");
		Object.setBaseType("TestNamespace_Base.WorkspaceObject");
		
		SubType subType = new SubType(context);
		subType.setsubTypeNames("ItemRevision");
		subType.setsubTypeDisplayName("Item Revision");
		subType.setisDataset(false);
		Core.commit(context, subType.getMendixObject());
		
		Properties property = new Properties(context);
		property.setODataName("Revision");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("item_revision_id");
		property.setTcDisplayName("Revision");
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			int actual_object_propSize = Core.retrieveByPath(context, actuals_objects.get(0), "SchemaGenerator.Properties_OdataObject").size();
			Assert.assertEquals(2, actual_object_propSize);
			subType.delete();
		}
		catch (Exception e) {
			subType.delete();
			Assert.fail("Test ViewODataObjectsTest:getOdataObjectsInheritance failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void getOdataObjects_Dataset() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(false);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.Dataset");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("Dataset");
		
		SubType subType = new SubType(context);
		subType.setsubTypeNames("Dataset");
		subType.setsubTypeDisplayName("Dataset");
		subType.setisDataset(false);
		Core.commit(context, subType.getMendixObject());
		
		OdataObject Object_ImanFile = new OdataObject(context);
		Object_ImanFile.setODataName("TestNamespace.File");
		Object_ImanFile.setOdataObject_ContractNameSpaces(contractNS);
		Object_ImanFile.setTcName("ImanFile");
		
		Properties property = new Properties(context);
		property.setODataName("Reference");
		property.setODataTypeValue(OData_Properties.Reference);
		property.setProperties_OdataObject(Object);
		property.setTcName("ref_list");
		property.setTcDisplayName("Reference");
		property.setreferenceType("TestNamespace.File");
		
		property = new Properties(context);
		property.setODataName("original_file_name");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object_ImanFile);
		property.setTcName("original_file_name");
		
		property = new Properties(context);
		property.setODataName("file_ext");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object_ImanFile);
		property.setTcName("file_ext");
		
		property = new Properties(context);
		property.setODataName("byte_size");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object_ImanFile);
		property.setTcName("byte_size");
		
		property = new Properties(context);
		property.setODataName("mime_type");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object_ImanFile);
		property.setTcName("mime_type");
		
		property = new Properties(context);
		property.setODataName("last_mod_date");
		property.setODataTypeValue(OData_Properties.Date);
		property.setProperties_OdataObject(Object_ImanFile);
		property.setTcName("last_mod_date");
		
		property = new Properties(context);
		property.setODataName("creation_date");
		property.setODataTypeValue(OData_Properties.Date);
		property.setProperties_OdataObject(Object_ImanFile);
		property.setTcName("creation_date");
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			
			List<IMendixObject> expecteds_objects = Core.retrieveByPath(context, contractNS.getMendixObject(), "SchemaGenerator.OdataObject_ContractNameSpaces");
			Assert.assertEquals(expecteds_objects, actuals_objects);
			
			for(IMendixObject actuals_object: actuals_objects)
			{
				if(actuals_object.getMember(context, "TcName").getValue(context).equals("ImanFile"))
				{
					List<IMendixObject> props = Core.retrieveByPath(context, actuals_object, "SchemaGenerator.Properties_OdataObject");
					for(IMendixObject prop: props)
					{
						Assert.assertEquals(true, prop.getMember(context, "isNotEditable").getValue(context));
					}
				}
			}
			
			subType.delete();
		}
		catch (Exception e) {
			subType.delete();
			Assert.fail("Test ViewODataObjectsTest:getOdataObjects_Dataset failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void getOdataObjects_ProcessedPreviously() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(false);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("TestName");
		Object.setTcDisplayName("Test Display");
		
		Properties property = new Properties(context);
		property.setODataName("TestPropertyStr");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropertyNameStr");
		property.setTcDisplayName("Test Property NameStr");
		
		property = new Properties(context);
		property.setODataName("TestPropertyInt");
		property.setODataTypeValue(OData_Properties.Integer);
		property.setProperties_OdataObject(Object);
		property.setTcName("TestPropertyNameInt");
		property.setTcDisplayName("Test Property NameInt");
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			
			List<IMendixObject> expecteds_objects = Core.retrieveByPath(context, contractNS.getMendixObject(), "SchemaGenerator.OdataObject_ContractNameSpaces");
			Assert.assertEquals(expecteds_objects, actuals_objects);
		}
		catch (Exception e) {
			Assert.fail("Test ViewODataObjectsTest:getOdataObjects_ProcessedPreviously failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void getOdataObjects_uidProp() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(false);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("TestName");
		
		Properties property = new Properties(context);
		property.setODataName("uid");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("puid");
		property.setTcDisplayName("uid");
		
		SubType subType = new SubType(context);
		subType.setsubTypeNames("TestName");
		subType.setsubTypeDisplayName("Test Name");
		subType.setisDataset(false);
		Core.commit(context, subType.getMendixObject());
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			int actual_object_propSize = Core.retrieveByPath(context, actuals_objects.get(0), "SchemaGenerator.Properties_OdataObject").size();
			Assert.assertEquals(0, actual_object_propSize);
			subType.delete();
		}
		catch (Exception e) {
			subType.delete();
			Assert.fail("Test ViewODataObjectsTest:getOdataObjects_uidProp failed:\n" + e.getMessage());
		}
	}

	@Test
	public void getOdataObjectsInheritance_InheritedAttributePresent() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS_Base = new ContractNameSpaces(context);
		contractNS_Base.setContractNameSpace("TestNamespace_Base");
		contractNS_Base.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS_Base.setInheritedObject(true);
		
		OdataObject Object_Base= new OdataObject(context);
		Object_Base.setODataName("TestNamespace_Base.WorkspaceObject");
		Object_Base.setOdataObject_ContractNameSpaces(contractNS_Base);
		Object_Base.setTcName("WorkspaceObject");
		
		Properties property_Base1 = new Properties(context);
		property_Base1.setODataName("Name");
		property_Base1.setODataTypeValue(OData_Properties.String);
		property_Base1.setProperties_OdataObject(Object_Base);
		property_Base1.setTcName("object_name");
		property_Base1.setTcDisplayName("Name");
		
		Properties property_Base2 = new Properties(context);
		property_Base2.setODataName("Description");
		property_Base2.setODataTypeValue(OData_Properties.String);
		property_Base2.setProperties_OdataObject(Object_Base);
		property_Base2.setTcName("object_desc");
		property_Base2.setTcDisplayName("Description");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(false);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.ItemRevision");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("ItemRevision");
		Object.setBaseType("TestNamespace_Base.WorkspaceObject");
		
		SubType subType = new SubType(context);
		subType.setsubTypeNames("ItemRevision");
		subType.setsubTypeDisplayName("Item Revision");
		subType.setisDataset(false);
		Core.commit(context, subType.getMendixObject());
		
		Properties property = new Properties(context);
		property.setODataName("Revision");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("item_revision_id");
		property.setTcDisplayName("Revision");
		
		Properties property_Parent = new Properties(context);
		property_Parent.setODataName("Description");
		property_Parent.setODataTypeValue(OData_Properties.String);
		property_Parent.setProperties_OdataObject(Object);
		property_Parent.setTcName("object_desc");
		property_Parent.setTcDisplayName("Description");
		property_Parent.setInheritedProperties(true);
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			int actual_object_propSize = Core.retrieveByPath(context, actuals_objects.get(0), "SchemaGenerator.Properties_OdataObject").size();
			Assert.assertEquals(3, actual_object_propSize);
			subType.delete();
		}
		catch (Exception e) {
			subType.delete();
			Assert.fail("Test ViewODataObjectsTest:getOdataObjectsInheritance_InheritedAttributePresent failed:\n" + e.getMessage());
		}
	}

	@Test
	public void getOdataObjectsInheritance_MultiLevel() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace_Child");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS_Base = new ContractNameSpaces(context);
		contractNS_Base.setContractNameSpace("TestNamespace_Base");
		contractNS_Base.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS_Base.setInheritedObject(true);
		
		OdataObject Object_Base= new OdataObject(context);
		Object_Base.setODataName("TestNamespace_Base.WorkspaceObject");
		Object_Base.setOdataObject_ContractNameSpaces(contractNS_Base);
		Object_Base.setTcName("WorkspaceObject");
		
		Properties property_Base = new Properties(context);
		property_Base.setODataName("Name");
		property_Base.setODataTypeValue(OData_Properties.String);
		property_Base.setProperties_OdataObject(Object_Base);
		property_Base.setTcName("object_name");
		property_Base.setTcDisplayName("Name");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(true);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.ItemRevision");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("ItemRevision");
		Object.setBaseType("TestNamespace_Base.WorkspaceObject");
		
		Properties property = new Properties(context);
		property.setODataName("Revision");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("item_revision_id");
		property.setTcDisplayName("Revision");
		
		ContractNameSpaces contractNS_Child = new ContractNameSpaces(context);
		contractNS_Child.setContractNameSpace("TestNamespace_Child");
		contractNS_Child.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS_Child.setInheritedObject(false);
		
		OdataObject Object_Child = new OdataObject(context);
		Object_Child.setODataName("TestNamespace_Base.PartRevision");
		Object_Child.setOdataObject_ContractNameSpaces(contractNS_Child);
		Object_Child.setTcName("PartRevision");
		Object_Child.setBaseType("TestNamespace.ItemRevision");
		
		Properties property_Child = new Properties(context);
		property_Child.setODataName("ID");
		property_Child.setODataTypeValue(OData_Properties.String);
		property_Child.setProperties_OdataObject(Object_Child);
		property_Child.setTcName("item_id");
		property_Child.setTcDisplayName("ID");
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			int actual_object_propSize = Core.retrieveByPath(context, actuals_objects.get(0), "SchemaGenerator.Properties_OdataObject").size();
			Assert.assertEquals(3, actual_object_propSize);
		}
		catch (Exception e) {
			Assert.fail("Test ViewODataObjectsTest:getOdataObjectsInheritance_MultiLevel failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void getOdataObjectsInheritance_AlreadyProcessed() throws CoreException
	{
		ContractNameSpaceDropDown CNDropDown = new ContractNameSpaceDropDown(context);
		CNDropDown.setContractNameSpace("TestNamespace_Child");
		
		ODataSchema odataSchema = new ODataSchema(context);
		odataSchema.setContractName("TestSchema");
		odataSchema.setmajorVersion("v1");
		
		ContractNameSpaces contractNS_Base = new ContractNameSpaces(context);
		contractNS_Base.setContractNameSpace("TestNamespace_Base");
		contractNS_Base.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS_Base.setInheritedObject(true);
		
		OdataObject Object_Base= new OdataObject(context);
		Object_Base.setODataName("TestNamespace_Base.WorkspaceObject");
		Object_Base.setOdataObject_ContractNameSpaces(contractNS_Base);
		Object_Base.setTcName("WorkspaceObject");
		Object_Base.setTcDisplayName("Workspace Object");
		
		Properties property_Base = new Properties(context);
		property_Base.setODataName("Name");
		property_Base.setODataTypeValue(OData_Properties.String);
		property_Base.setProperties_OdataObject(Object_Base);
		property_Base.setTcName("object_name");
		property_Base.setTcDisplayName("Name");
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace");
		contractNS.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS.setInheritedObject(true);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.ItemRevision");
		Object.setOdataObject_ContractNameSpaces(contractNS);
		Object.setTcName("ItemRevision");
		Object.setBaseType("TestNamespace_Base.WorkspaceObject");
		Object.setTcDisplayName("Item Revision");
		
		Properties property = new Properties(context);
		property.setODataName("Revision");
		property.setODataTypeValue(OData_Properties.String);
		property.setProperties_OdataObject(Object);
		property.setTcName("item_revision_id");
		property.setTcDisplayName("Revision");
		
		ContractNameSpaces contractNS_Child = new ContractNameSpaces(context);
		contractNS_Child.setContractNameSpace("TestNamespace_Child");
		contractNS_Child.setContractNameSpaces_ODataSchema(odataSchema);
		contractNS_Child.setInheritedObject(false);
		
		OdataObject Object_Child = new OdataObject(context);
		Object_Child.setODataName("TestNamespace_Base.PartRevision");
		Object_Child.setOdataObject_ContractNameSpaces(contractNS_Child);
		Object_Child.setTcName("PartRevision");
		Object_Child.setBaseType("TestNamespace.ItemRevision");
		Object_Child.setTcDisplayName("Part Revision");
		
		Properties property_Child = new Properties(context);
		property_Child.setODataName("ID");
		property_Child.setODataTypeValue(OData_Properties.String);
		property_Child.setProperties_OdataObject(Object_Child);
		property_Child.setTcName("item_id");
		property_Child.setTcDisplayName("ID");
		
		try
		{
			ViewODataObjects viewODataObjects = new ViewODataObjects(context, CNDropDown.getMendixObject(), odataSchema.getMendixObject());
			List<IMendixObject> actuals_objects = viewODataObjects.executeAction();
			int actual_object_propSize = Core.retrieveByPath(context, actuals_objects.get(0), "SchemaGenerator.Properties_OdataObject").size();
			Assert.assertEquals(3, actual_object_propSize);
		}
		catch (Exception e) {
			Assert.fail("Test ViewODataObjectsTest:getOdataObjectsInheritance_AlreadyProcessed failed:\n" + e.getMessage());
		}
	}
}
