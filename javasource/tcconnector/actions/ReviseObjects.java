// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package tcconnector.actions;

import java.util.Map;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.systemwideinterfaces.core.IMendixObjectMember;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONObject;
import com.mendix.webui.CustomJavaAction;
import tcconnector.foundation.BusinessObjectMappings;
import tcconnector.foundation.JModelObject;
import tcconnector.foundation.JPolicy;
import tcconnector.foundation.TcConnection;
import tcconnector.internal.foundation.Constants;
import tcconnector.internal.servicehelper.DeepCopyDataHelper;
import tcconnector.proxies.ModelObject;
import tcconnector.proxies.ReviseObjectsResponse;
import tcconnector.proxies.ReviseOutputResponse;
import tcconnector.proxies.ReviseTreesResponse;

/**
 * SOA URL:
 * Core-2015-07-DataManagement/getDeepCopyData
 * Core-2013-05-DataManagement/reviseObjects
 * 
 * Description: 
 * This is a generic revise operation for revisable business objects. This operation revises the given objects and copies or creates new objects using the data for the property values and deep copy data, assuming the reviseInput for the object is provided. The input for revise object is passed through reviseInput entity.
 * 
 * Returns:
 * An entity of type ReviseObjectsResponse. Revised objects can be retrieved using association TcConnector.revise_output and the revise tree can be retrieved using TcConnector.reviseTrees. Partial errors can be retrieved using association TcConnector.ResponseData/TcConnector.PartialErrors.
 */
public class ReviseObjects extends CustomJavaAction<IMendixObject>
{
	private IMendixObject __objectToRevise;
	private tcconnector.proxies.WorkspaceObject objectToRevise;
	private IMendixObject __reviseInput;
	private tcconnector.proxies.ReviseInputs reviseInput;
	private java.lang.String businessObjectMapping;
	private java.lang.String ConfigurationName;

	public ReviseObjects(IContext context, IMendixObject objectToRevise, IMendixObject reviseInput, java.lang.String businessObjectMapping, java.lang.String ConfigurationName)
	{
		super(context);
		this.__objectToRevise = objectToRevise;
		this.__reviseInput = reviseInput;
		this.businessObjectMapping = businessObjectMapping;
		this.ConfigurationName = ConfigurationName;
	}

	@java.lang.Override
	public IMendixObject executeAction() throws Exception
	{
		this.objectToRevise = this.__objectToRevise == null ? null : tcconnector.proxies.WorkspaceObject.initialize(getContext(), __objectToRevise);

		this.reviseInput = this.__reviseInput == null ? null : tcconnector.proxies.ReviseInputs.initialize(getContext(), __reviseInput);

		// BEGIN USER CODE
		boolean before = true;
		try
		{
			IContext context = getContext();
			// Prepare the request body
			JSONObject reqBodyJson = prepareReviseInputBody(context);
			
			BusinessObjectMappings boMappings = new BusinessObjectMappings(businessObjectMapping,ConfigurationName);
			JPolicy policy = new JPolicy(boMappings);
			// Call the ReviseObjects service
			JSONObject response = TcConnection.callTeamcenterService(context, Constants.OPERATION_REVISEOBJECTS, reqBodyJson, policy, ConfigurationName);
			
			before = false;
			ReviseObjectsResponse reponseEntity = new ReviseObjectsResponse(context);

			// Update the Response entity with revised objects.
			//Update the output container from response
			JSONArray outputArray = response.getJSONArray(KEY_OUTPUT);
			ReviseOutputResponse  revieseOutput = new ReviseOutputResponse(context);
			revieseOutput.setoutput(reponseEntity);
			for(int outputIndex=0; outputIndex < outputArray.length(); ++outputIndex) 
			{
				JSONObject output = outputArray.getJSONObject(outputIndex);
				
				//Update the objects container from output container from response
				JSONArray objectArray = output.getJSONArray(KEY_OBJECTS);
				for(int objIndex=0; objIndex < objectArray.length(); ++objIndex)
				{
					JModelObject jmo = (JModelObject)objectArray.get(objIndex);
					IMendixObject objectsEntity = jmo.instantiateEntity(getContext(), null, boMappings,ConfigurationName);
					ModelObject mo = ModelObject.initialize(getContext(), objectsEntity);
					mo.setrevise_objects(revieseOutput);
				}
			}
			
			//Update the revise trees container from response
			JSONArray reviseTreeArray = response.getJSONArray(KEY_REVISE_TREES);
			for(int reviseTreeIndex = 0; reviseTreeIndex < reviseTreeArray.length(); ++reviseTreeIndex) 
			{
				ReviseTreesResponse reviseTree = new ReviseTreesResponse(context);
				reviseTree.setreviseTrees(reponseEntity);
				instantiateReviseTree(reviseTreeArray.getJSONObject(reviseTreeIndex), reviseTree, boMappings);
			}
			return reponseEntity.getMendixObject();
		}
		catch(Exception e)
		{
			String message = (before)? "Failed to marshall the the service operation " +
                    Constants.SERVICE_DATAMANAGEMENT_1305+"/"+ Constants.SERVICE_DATAMANAGEMENT_1305 + 
                    " input argument.":
                    "Failed to marshall the the service operation " +
                    Constants.SERVICE_DATAMANAGEMENT_1305+"/"+ Constants.SERVICE_DATAMANAGEMENT_1305 +
                    " response data.";
			Constants.LOGGER.error(message + e.getMessage());
			message += "Please contact your system administrator for further assistance.";
			throw e;
		}	
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "ReviseObjects";
	}

	// BEGIN EXTRA CODE
	
	private void instantiateReviseTree(JSONObject reviseTree, ReviseTreesResponse responseTreeEntity, BusinessObjectMappings boMappings)
	{
		
		//Update the original object container from output container from response
		Object originalObj = reviseTree.get(KEY_ORIGINAL_OBJ);
		if(!originalObj.equals(null))
		{
			Object originalObjProps = null;
			if(originalObj instanceof JSONObject)
			{
				originalObjProps = ((JSONObject)originalObj).optString("props");
			}
			if(originalObjProps != null)
			{
				JModelObject jmo = (JModelObject)originalObj;
				IMendixObject originalObjectEntity = jmo.instantiateEntity(getContext(), null, boMappings,ConfigurationName);
				ModelObject mo = ModelObject.initialize(getContext(), originalObjectEntity);
				mo.setoriginalObject(responseTreeEntity);
			}
		}
		
		//Update the object copy container from output container from response
		Object objectCopy = reviseTree.get(KEY_OBJECT_COPY);
		if(!objectCopy.equals(null))
		{
			Object objectCopyProps = null;
			if(objectCopy instanceof JSONObject)
			{
				objectCopyProps = ((JSONObject)objectCopy).optString("props");
			}
			if(objectCopyProps != null)
			{
				JModelObject jmo = (JModelObject)objectCopy;
				IMendixObject objectCopyEntity = jmo.instantiateEntity(getContext(), null, boMappings,ConfigurationName);
				ModelObject mo = ModelObject.initialize(getContext(), objectCopyEntity);
				mo.setobjectCopy(responseTreeEntity);
			}
		}
		
		//Update the child revise nodes container from output container from response
		JSONArray childReviseNodeArray = reviseTree.getJSONArray(KEY_CHILD_REVISE_NODE);
		for(int childReviseNodeIndex=0; childReviseNodeIndex < childReviseNodeArray.length(); ++childReviseNodeIndex)
		{
			instantiateReviseTree(childReviseNodeArray.getJSONObject(childReviseNodeIndex), responseTreeEntity, boMappings);
		}
	}
	
	private JSONObject prepareReviseInputBody(IContext context) throws Exception
	{
		JSONObject getDeepCopyDataResponse = DeepCopyDataHelper.callGetDeepCopyDataSOA(context, objectToRevise, KEY_REVISE, null, ConfigurationName);
		
		JSONObject revise = new JSONObject();
		JSONObject tgtObject = new JSONObject();
		if(objectToRevise != null)
		{
			tgtObject.put(KEY_UID, objectToRevise.getUID());
			tgtObject.put(KEY_TYPE, objectToRevise.get_Type());
		}
		revise.put(KEY_TARGET_OBJECT, tgtObject);
		
		JSONObject reviseInputData = new JSONObject();
		
		Map<String, ? extends IMendixObjectMember<?>> mendixObjMembers = reviseInput.getMendixObject().getMembers(context);
		
		for(String memberName: mendixObjMembers.keySet())
		{
			Object memberValue = mendixObjMembers.get(memberName).getValue(context);
			if(memberValue != null)
			{
				JSONArray attrValues = new JSONArray();
				attrValues.put(memberValue.toString());
				reviseInputData.put(memberName, attrValues);
			}
		}

		revise.put(KEY_REVISE_INPUTS, reviseInputData);
		
		JSONArray deepCopyDatas = new JSONArray();
		JSONArray responseDeepCopyDatas = getDeepCopyDataResponse.getJSONArray(KEY_DEEP_COPY_DATAS);
		
		for(int index = 0; index < responseDeepCopyDatas.length(); index++)
		{
			JSONObject responseDeepCopyData = responseDeepCopyDatas.getJSONObject(index);
			JSONObject deepCopyData = DeepCopyDataHelper.processDeepCopyDatas(responseDeepCopyData);
			deepCopyDatas.put(deepCopyData);
		}
		
		revise.put(KEY_DEEP_COPY_DATAS, deepCopyDatas);
		
		JSONArray reviseIn = new JSONArray();
		reviseIn.put(revise);
		JSONObject reviseData = new JSONObject();
		reviseData.put(KEY_REVISE_IN, reviseIn);
		return reviseData;
	}
	
	private static final String KEY_REVISE										 	 = "Revise";
	private static final String KEY_UID											  	 = "uid";
	private static final String KEY_TYPE										  	 = "type";
	private static final String KEY_REVISE_IN		  							  	 = "reviseIn";
	private static final String KEY_TARGET_OBJECT									 = "targetObject";
	private static final String KEY_DEEP_COPY_DATAS   							 	 = "deepCopyDatas";
	private static final String KEY_REVISE_INPUTS   								 = "reviseInputs";
	private static final String KEY_OUTPUT			  								 = "output";
	private static final String KEY_OBJECTS			   								 = "objects";
	private static final String KEY_REVISE_TREES	  								 = "reviseTrees";
	private static final String KEY_ORIGINAL_OBJ	  								 = "originalObject";
	private static final String KEY_OBJECT_COPY 	  								 = "objectCopy";
	private static final String KEY_CHILD_REVISE_NODE  								 = "childReviseNodes";

	// END EXTRA CODE
}
