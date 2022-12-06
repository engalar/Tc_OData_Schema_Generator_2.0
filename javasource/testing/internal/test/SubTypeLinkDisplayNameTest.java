package testing.internal.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import schemagenerator.actions.SubTypeLinkDisplayName;
import schemagenerator.proxies.SubTypeEntity;
import schemagenerator.proxies.SubTypeEntityDisplayName;
import testing.internal.TestingContext;

public class SubTypeLinkDisplayNameTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void valideSubTypeAndDisplayName()
	{
		List<IMendixObject> subTypesList = new ArrayList<IMendixObject>();
		List<IMendixObject> subTypesDisplayList = new ArrayList<IMendixObject>();
		
		SubTypeEntity subType = new SubTypeEntity(context);
		subType.setsubTypeName("TestSubType");
		subTypesList.add(subType.getMendixObject());
		
		SubTypeEntityDisplayName displayName = new SubTypeEntityDisplayName(context);
		displayName.setdisplayableSubTypeName("Test Display");
		subTypesDisplayList.add(displayName.getMendixObject());
		
		try
		{
			SubTypeLinkDisplayName subTypeLink = new SubTypeLinkDisplayName(context, subTypesList, subTypesDisplayList);
			subTypeLink.executeAction();
			
			List<IMendixObject> expecteds_objects = Core.retrieveByPath(context, subType.getMendixObject(), "SchemaGenerator.SubTypeEntity_SubTypeEntityDisplayName");
			Assert.assertEquals(displayName.getMendixObject(), expecteds_objects.get(0));
			
		}catch (Exception e) {
			Assert.fail("Test SubTypeLinkDisplayNameTest:valideSubTypeAndDisplayName failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalideSubTypeList()
	{
		List<IMendixObject> subTypesDisplayList = new ArrayList<IMendixObject>();
		
		SubTypeEntityDisplayName displayName = new SubTypeEntityDisplayName(context);
		displayName.setdisplayableSubTypeName("Test Display");
		subTypesDisplayList.add(displayName.getMendixObject());
		
		try
		{
			SubTypeLinkDisplayName subTypeLink = new SubTypeLinkDisplayName(context, null, subTypesDisplayList);
			subTypeLink.executeAction();
			Assert.fail("Test SubTypeLinkDisplayNameTest:invalideSubTypeList failed:\n");
			
		}catch (Exception e) {
			Assert.assertEquals("Input Parameters should not be null or empty", e.getMessage());
		}
	}
	
	@Test
	public void invalideSubTypesDisplay()
	{
		List<IMendixObject> subTypesList = new ArrayList<IMendixObject>();
		
		SubTypeEntity subType = new SubTypeEntity(context);
		subType.setsubTypeName("TestSubType");
		subTypesList.add(subType.getMendixObject());
		
		try
		{
			SubTypeLinkDisplayName subTypeLink = new SubTypeLinkDisplayName(context, subTypesList, null);
			subTypeLink.executeAction();
			Assert.fail("Test SubTypeLinkDisplayNameTest:invalideSubTypesDisplay failed:\n");
			
		}catch (Exception e) {
			Assert.assertEquals("Input Parameters should not be null or empty", e.getMessage());
		}
	}
}
