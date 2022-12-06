package testing.internal.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;

import schemagenerator.actions.ValidateRefNamespaceToDelete;
import schemagenerator.proxies.ContractNameSpaces;
import schemagenerator.proxies.NameSpaces;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.externalReferences;
import testing.internal.TestingContext;

public class ValidateRefNamespaceToDeleteTest 
{
	private IContext context;
	
	@Before 
	public void setUp()
	{
		context   = TestingContext.getTestContext();
	}
	
	@Test
	public void valideRefNamespace()
	{
		ContractNameSpaces contractNS_Ref = new ContractNameSpaces(context);
		contractNS_Ref.setContractNameSpace("TestNamespace");
		contractNS_Ref.setInheritedObject(true);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS_Ref);
		
		externalReferences externalRef = new externalReferences(context);
		
		NameSpaces namespace = new NameSpaces(context);
		namespace.setname("TestNamespace");
		namespace.setNameSpaces_externalReferences(externalRef);
		
		List<IMendixObject> contracNSList = new ArrayList<IMendixObject>();
		contracNSList.add(contractNS_Ref.getMendixObject());
		
		List<IMendixObject> externalRefList = new ArrayList<IMendixObject>();
		externalRefList.add(externalRef.getMendixObject());
		
		try 
		{
			ValidateRefNamespaceToDelete validateRefNamespaceToDelete = new ValidateRefNamespaceToDelete(context, contracNSList, externalRefList);
			validateRefNamespaceToDelete.executeAction();
		}
		catch (Exception e) {
			Assert.fail("Test ValidateRefNamespaceToDeleteTest:valideRefNamespace failed:\n" + e.getMessage());
		}
	}
	
	@Test
	public void invalideRefNamespace()
	{
		ContractNameSpaces contractNS_Ref = new ContractNameSpaces(context);
		contractNS_Ref.setContractNameSpace("TestNamespace");
		contractNS_Ref.setInheritedObject(true);
		
		OdataObject Object = new OdataObject(context);
		Object.setODataName("TestNamespace.TestName");
		Object.setOdataObject_ContractNameSpaces(contractNS_Ref);
		
		ContractNameSpaces contractNS = new ContractNameSpaces(context);
		contractNS.setContractNameSpace("TestNamespace1");
		contractNS.setInheritedObject(false);
		
		OdataObject Object1 = new OdataObject(context);
		Object1.setODataName("TestNamespace1.TestName1");
		Object1.setOdataObject_ContractNameSpaces(contractNS);
		Object1.setBaseType("TestNamespace.TestName");
		
		externalReferences externalRef = new externalReferences(context);
		
		NameSpaces namespace = new NameSpaces(context);
		namespace.setname("TestNamespace");
		namespace.setNameSpaces_externalReferences(externalRef);
		
		List<IMendixObject> contracNSList = new ArrayList<IMendixObject>();
		contracNSList.add(contractNS_Ref.getMendixObject());
		contracNSList.add(contractNS.getMendixObject());
		
		List<IMendixObject> externalRefList = new ArrayList<IMendixObject>();
		externalRefList.add(externalRef.getMendixObject());
		
		try 
		{
			ValidateRefNamespaceToDelete validateRefNamespaceToDelete = new ValidateRefNamespaceToDelete(context, contracNSList, externalRefList);
			validateRefNamespaceToDelete.executeAction();
			Assert.fail("Test ValidateRefNamespaceToDeleteTest:invalideRefNamespace failed:\n");
		}
		catch (Exception e) {
			Assert.assertEquals("Reference cannot be deleted because the object "+ "TestNamespace.TestName" + " is refered in current service.", e.getMessage());
		}
	}
}
