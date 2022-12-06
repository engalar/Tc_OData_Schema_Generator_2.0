package schemagenerator.actions;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.*;
import com.mendix.systemwideinterfaces.core.UserException.ExceptionCategory;
import com.mendix.thirdparty.org.json.JSONArray;
import com.mendix.thirdparty.org.json.JSONObject;

import com.mendix.webui.FeedbackHelper;

import schemagenerator.proxies.ActionParams;
import schemagenerator.proxies.ComplexTypeAttribute;
import schemagenerator.proxies.ContractNameSpaces;
//import schemagenerator.proxies.ODataFullyQualifiedName;
import schemagenerator.proxies.ODataSchema;
import schemagenerator.proxies.OdataActions;
import schemagenerator.proxies.OdataComplexType;
import schemagenerator.proxies.OdataEnum;
import schemagenerator.proxies.OdataObject;
import schemagenerator.proxies.OdataSchemaDocument;
import schemagenerator.proxies.ReturnType;
import schemagenerator.proxies.SelectionTreeNode;
import schemagenerator.proxies.View;
import schemagenerator.proxies.externalReferences;
import schemagenerator.proxies.constants.Constants;

public class TcODataSchemaHelper 
{
	public static String extractSchemaJsonString(IContext context,
			schemagenerator.proxies.OdataSchemaDocument ODataSchemaFile) throws Exception 
	{
		InputStream is = Core.getFileDocumentContent(context, ODataSchemaFile.getMendixObject());
		String jsonString = convertStreamToString(is);
		return jsonString;
	}


	public static String convertStreamToString(InputStream is) throws Exception 
	{
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
		while ((line = bufferReader.readLine()) != null) 
		{
			stringBuilder.append(line);
		}
		return stringBuilder.toString();
	}
	
	public static String convertCircularStreamToString(InputStream is) throws Exception 
	{
		BufferedReader bufferReader = new BufferedReader(new InputStreamReader(is));
		StringBuilder stringBuilder = new StringBuilder();
		String line = null;
		
		while ((line = bufferReader.readLine()) != null) 
		{
			stringBuilder.append(line);
		}
		
		return stringBuilder.toString();
	}
	/**
	 * 
	 * @param context
	 * @param odataSchemaObjects
	 * @return List of All ComplexTypes connected to these schemas as mendix objects
	 */
	public static List<IMendixObject> getAllComplexTypes(IContext context, List<IMendixObject> odataSchemaObjects) {
		ArrayList<IMendixObject> complexTypes = new ArrayList<>();
		for (IMendixObject schema : odataSchemaObjects) {
			List<IMendixObject> cns = Core.retrieveByPath(context, schema, "SchemaGenerator.ContractNameSpaces_ODataSchema");
			for (IMendixObject namespace : cns) {
				complexTypes.addAll(Core.retrieveByPath(context, namespace, "SchemaGenerator.OdataComplexType_ContractNameSpaces"));
			}
		}
		return complexTypes;
	}
	/**
	 * 
	 * @param context
	 * @param odataSchemaObjects
	 * @return List of All OdataObjects connected to these schemas as mendix objects
	 */
	public static List<IMendixObject> getAllOdataObjects(IContext context, List<IMendixObject> odataSchemaObjects) {
		ArrayList<IMendixObject> odataObjects = new ArrayList<>();
		for (IMendixObject schema : odataSchemaObjects) {
			List<IMendixObject> cns = Core.retrieveByPath(context, schema, "SchemaGenerator.ContractNameSpaces_ODataSchema");
			for (IMendixObject namespace : cns) {
				odataObjects.addAll(Core.retrieveByPath(context, namespace, "SchemaGenerator.OdataObject_ContractNameSpaces"));
			}
		}
		return odataObjects;
	}
	/**
	 * 
	 * @param context
	 * @param odataSchemaObjects
	 * @return List of All OdataActions connected to these schemas as mendix objects
	 */
	public static List<IMendixObject> getAllOdataActions(IContext context, List<IMendixObject> odataSchemaObjects) {
		ArrayList<IMendixObject> odataActions = new ArrayList<>();
		for (IMendixObject schema : odataSchemaObjects) {
			List<IMendixObject> cns = Core.retrieveByPath(context, schema, "SchemaGenerator.ContractNameSpaces_ODataSchema");
			for (IMendixObject namespace : cns) {
				odataActions.addAll(Core.retrieveByPath(context, namespace, "SchemaGenerator.OdataActions_ContractNameSpaces"));
			}
		}
		return odataActions;
	}
	/**
	 * 
	 * @param context
	 * @param derivedSchema
	 * @return List of All base schemas as mendix objects (list also includes the said derived schema)
	 * @throws CoreException
	 */
	public static List<IMendixObject> getAllrefrencedSchemasAsMendixObjects(IContext context, IMendixObject derivedSchema) throws CoreException {
		ArrayList<IMendixObject> schemas = new ArrayList<>();
		List<IMendixObject> externalRefs = Core.retrieveByPath(context, derivedSchema, externalReferences.MemberNames.externalReferences_ODataSchema.toString());
		for (IMendixObject refrencemObj : externalRefs){
			externalReferences refrence = externalReferences.initialize(context, refrencemObj);
			String uri = refrence.geturi(context);
			if (uri.equals(Constants.getCAPABLITIES_URI()))
				continue;
			String uriPart = uri.split("http://localhost/")[1];
			String[] uriServiceInfo = uriPart.split("/");
			String serviceName = uriServiceInfo[1];
			String serviceVersion = uriServiceInfo[2];
			Map<String, String> sortOrder =  new HashMap<>();
			sortOrder.put("minorVersion", "DESC");
			List<IMendixObject> services = Core.retrieveXPathQuery(context, "//" + OdataSchemaDocument.entityName + "[Name = '" + serviceName + "'][majorVersion = '" + serviceVersion + "']", Integer.MAX_VALUE, 0, sortOrder);
			if (services.isEmpty()) {
				throw new UserException(ExceptionCategory.Custom, "Service not found");
			}
			IMendixObject currentService = services.get(0);
			schemas.addAll(getAllrefrencedSchemasAsMendixObjects(context, currentService));
			schemas.add(currentService);
		}
		schemas.add(derivedSchema);
		return schemas;
	}

//	public static List<IMendixObject> getODataFQNFromODataObjects(IContext context, List<IMendixObject> objects) {
//		List<IMendixObject> fqnObjects = new ArrayList<>(objects.size());
//		for (IMendixObject object : objects) {
//			ODataFullyQualifiedName attributeType = new ODataFullyQualifiedName(context);
//			if (Core.isSubClassOf(OdataObject.entityName, object.getType())) {
//				String nameOfOdataObject = object.getMember(
//					context,
//					OdataObject
//						.MemberNames
//						.ODataName
//						.toString()
//				).getValue(context).toString();
//				attributeType.setFQN(context, nameOfOdataObject);
//				fqnObjects.add(attributeType.getMendixObject());
//				continue;
//			}
//			else if (Core.isSubClassOf(OdataComplexType.entityName, object.getType())) {
//				IMendixObject namespace = Core.retrieveByPath(
//					context,
//					object,
//					OdataComplexType
//						.MemberNames
//						.OdataComplexType_ContractNameSpaces
//						.toString()
//				).get(0);
//				String namespaceOfOdataComplex = namespace.getMember(
//					context,
//					ContractNameSpaces
//						.MemberNames
//						.ContractNameSpace
//						.toString()
//				).getValue(context).toString();
//				String nameOfComplexType = object.getMember(
//					context,
//					OdataComplexType
//						.MemberNames
//						.ComplexTypeName
//						.toString()
//				).getValue(context).toString();
//				// attributeType.setFQN(context, String.format("%s.%s", namespaceOfOdataComplex, nameOfComplexType));
//				attributeType.setFQN(context, nameOfComplexType);
//				fqnObjects.add(attributeType.getMendixObject());
//				continue;
//			}
//			else {
//				throw new IllegalArgumentException(String.format("The given object is not a type or subtype of %s or %s", OdataComplexType.entityName, OdataObject.entityName));
//			}
//		}
//		return fqnObjects;
//	}

	public static List<IMendixObject> getObjectSelectionTreeForSchemaGenreation(IContext context, IMendixObject schema, boolean addComplexTypes, boolean addOdataObjects, boolean addEnums, IMendixIdentifier ignoreObject, View view) throws CoreException {
		if (!addComplexTypes && !addOdataObjects && !addEnums) {
			return Collections.emptyList();
		}
		List<IMendixObject> tree = new ArrayList<>();
		List<IMendixObject> refrencedSchemas = getAllrefrencedSchemasAsMendixObjects(context, schema);
		for (IMendixObject refrencedSchema : refrencedSchemas) {
			List<IMendixObject> contractNameSpaces = Core.retrieveByPath(context, refrencedSchema, ContractNameSpaces.MemberNames.ContractNameSpaces_ODataSchema.toString());
			List<SelectionTreeNode> contractNameSpaceTreeNodes = new LinkedList<>();
			for (IMendixObject cns : contractNameSpaces) {
				List<SelectionTreeNode> children = new LinkedList<>();
				SelectionTreeNode newNodeCns = new SelectionTreeNode(context);
				if (addComplexTypes) {
					List<IMendixObject> complexTypeList = Core.retrieveByPath(context, cns, OdataComplexType.MemberNames.OdataComplexType_ContractNameSpaces.toString());
					for (IMendixObject complexType : complexTypeList) {
						if (ignoreObject != null && ignoreObject.toLong() == complexType.getId().toLong()) {
							continue;
						}
						OdataComplexType odataComplexType = OdataComplexType.initialize(context, complexType);
						SelectionTreeNode newNodeComplexType = new SelectionTreeNode(context);
						newNodeComplexType.setMendixGUID(context, complexType.getId().toLong());
						newNodeComplexType.setMendixType(context, odataComplexType.entityName);
						String name = odataComplexType.getComplexTypeName();
						if (name == null || name.split("[.]").length <= 1)
							continue;
						newNodeComplexType.setName(context, name.split("[.]")[1]);
						newNodeComplexType.setOdataFullyQuallifiedName(context, name);
						newNodeComplexType.setRoot(false);
						newNodeComplexType.setSelectionTreeNode_View(view);
						children.add(newNodeComplexType);
						tree.add(newNodeComplexType.getMendixObject());
					}
				}
				if (addEnums) {
					List<IMendixObject> enumList = Core.retrieveByPath(context, cns, OdataEnum.MemberNames.OdataEnum_ContractNameSpaces.toString());
					for (IMendixObject odataEnumMxObj : enumList) {
						if (ignoreObject != null && ignoreObject.toLong() == odataEnumMxObj.getId().toLong()) {
							continue;
						}
						OdataEnum odataEnum = OdataEnum.initialize(context, odataEnumMxObj);
						SelectionTreeNode newNodeOdataEnum = new SelectionTreeNode(context);
						newNodeOdataEnum.setMendixGUID(context, odataEnumMxObj.getId().toLong());
						newNodeOdataEnum.setMendixType(context, odataEnum.entityName);
						String name = odataEnum.getName();
						if (name == null || name.split("[.]").length <= 1)
							continue;
						newNodeOdataEnum.setName(context, name.split("[.]")[1]);
						newNodeOdataEnum.setOdataFullyQuallifiedName(context, name);
						newNodeOdataEnum.setRoot(false);
						newNodeOdataEnum.setSelectionTreeNode_View(view);
						children.add(newNodeOdataEnum);
						tree.add(newNodeOdataEnum.getMendixObject());
					}
				}
				if (addOdataObjects) {
					List<IMendixObject> odataObjectsList = Core.retrieveByPath(context, cns, OdataObject.MemberNames.OdataObject_ContractNameSpaces.toString());
					for (IMendixObject odataObjectMxObj : odataObjectsList) {
						OdataObject odataObject = OdataObject.initialize(context, odataObjectMxObj);
						SelectionTreeNode newNodeOdataObject = new SelectionTreeNode(context);
						newNodeOdataObject.setMendixGUID(context, odataObjectMxObj.getId().toLong());
						newNodeOdataObject.setMendixType(context, odataObject.entityName);
						newNodeOdataObject.setName(context, odataObject.getODataName().split("[.]")[1]);
						newNodeOdataObject.setOdataFullyQuallifiedName(context, odataObject.getODataName());
						newNodeOdataObject.setRoot(false);
						newNodeOdataObject.setSelectionTreeNode_View(view);
						children.add(newNodeOdataObject);
						tree.add(newNodeOdataObject.getMendixObject());
					}
				}
				if (children.isEmpty())
					continue;
				ContractNameSpaces namespace = ContractNameSpaces.initialize(context, cns);
				newNodeCns.setMendixGUID(context, cns.getId().toLong());
				newNodeCns.setMendixType(context, namespace.entityName);
				newNodeCns.setName(context, namespace.getContractNameSpace(context));
				newNodeCns.setOdataFullyQuallifiedName(context, namespace.getContractNameSpace(context));
				newNodeCns.setRoot(false);
				newNodeCns.setSelectionTreeNode_Children(context, children);
				newNodeCns.setSelectionTreeNode_View(view);
				contractNameSpaceTreeNodes.add(newNodeCns);
				tree.add(newNodeCns.getMendixObject());
			}
			if (contractNameSpaceTreeNodes.isEmpty()) 
				continue;

			ODataSchema schemaObj = ODataSchema.initialize(context, refrencedSchema);
			SelectionTreeNode newNodeSchema = new SelectionTreeNode(context);
			newNodeSchema.setMendixGUID(context, refrencedSchema.getId().toLong());
			newNodeSchema.setMendixType(context, schemaObj.entityName);
			String schemaName = schemaObj.getContractName(context);
			 if (refrencedSchema.getId().toLong() == schema.getId().toLong()) {
				if (schemaName == null || schemaName.isBlank())
					schemaName = "[Current Service]";
				else
					schemaName += " - [Current Schema]";
			 }
			newNodeSchema.setName(context, schemaName);
			newNodeSchema.setRoot(true);
			newNodeSchema.setSelectionTreeNode_Children(context, contractNameSpaceTreeNodes);
			newNodeSchema.setSelectionTreeNode_View(context, view);
			tree.add(newNodeSchema.getMendixObject());
		}
		return tree;
	}

	public static List<IMendixObject> getTreeForOperationMappingParameters(IContext context, IMendixObject action, View view) throws CoreException {
		List<IMendixObject> tree = new ArrayList<>();

		IMendixObject cns = null;
		List<IMendixObject>cnsList = Core.retrieveByPath(context, action, OdataActions.MemberNames.OdataActions_ContractNameSpaces.toString());
		if (cnsList.isEmpty()) {
			IMendixObject odataObject = Core.retrieveByPath(context, action, OdataActions.MemberNames.OdataActions_OdataObject.toString()).get(0);
			cns = Core.retrieveByPath(context, odataObject, OdataObject.MemberNames.OdataObject_ContractNameSpaces.toString()).get(0);
		}
		else {
			cns = cnsList.get(0);
		}
		IMendixObject schema = Core.retrieveByPath(context, cns, ContractNameSpaces.MemberNames.ContractNameSpaces_ODataSchema.toString()).get(0);
		List<IMendixObject> refSchemas = getAllrefrencedSchemasAsMendixObjects(context, schema);
		List<IMendixObject> allComplexTypes = getAllComplexTypes(context, refSchemas);
		Map<String, IMendixObject> complexTypeMap = new HashMap<>();
		for (IMendixObject cplxType : allComplexTypes) {
			complexTypeMap.put(cplxType.getValue(context, OdataComplexType.MemberNames.ComplexTypeName.toString()), cplxType);
		}
		// List<IMendixObject> allOdataObjects = getAllOdataObjects(context, refSchemas);
		// for (IMendixObject refSchema : refSchemas) {
		// 	List<IMendixObject> namespaces = Core.retrieveByPath(context, refSchema, ContractNameSpaces.MemberNames.ContractNameSpaces_ODataSchema.toString());
		// 	for (IMendixObject namespace : namespaces) {
		// 		String contractName = namespace.getValue(context, ContractNameSpaces.MemberNames.ContractNameSpace.toString());
		// 		nameSpaceMap.put(contractName, namespace);
		// 	}
		// }

		List<IMendixObject> actionParamMxObjs = Core.retrieveByPath(context, action, ActionParams.MemberNames.ActionParams_OdataActions.toString());

		for (IMendixObject actionParamMxObj : actionParamMxObjs) {
			ActionParams actionParam = ActionParams.initialize(context, actionParamMxObj);
			
			//Skip if primitive type
			String dataType = actionParam.getParameterDataType(context);
			String[] dataTypeParts =  dataType.split("[.]");
			if (dataTypeParts.length < 2)
				continue;

			
			// IMendixObject contractNameSpace = nameSpaceMap.get(dataTypeParts[0]);
			//  = Core.retrieveByPath(context, contractNameSpace, OdataObject.MemberNames.OdataObject_ContractNameSpaces.toString());
			// Long guid = actionParam.getParameterDataTypeGUID(context);
			// IMendixIdentifier actionMendixId = Core.createMendixIdentifier(guid);
			// IMendixObject parameterType = Core.retrieveId(context, actionMendixId);

			SelectionTreeNode parameterNode = new SelectionTreeNode(context);
			parameterNode.setMendixGUID(context, actionParamMxObj.getId().toLong());
			parameterNode.setMendixType(context, actionParam.entityName);
			parameterNode.setName(context, actionParam.getName());
			parameterNode.setOdataFullyQuallifiedName(context, actionParam.getName());
			parameterNode.setSelectionTreeNode_View(view);
			parameterNode.setRoot(true);
			tree.add(parameterNode.getMendixObject());
			
			//Ignore if not ComplexType
			if (complexTypeMap.containsKey(dataType)) {
				List<IMendixObject> complexTypeAttributes = Core.retrieveByPath(context, complexTypeMap.get(dataType), ComplexTypeAttribute.MemberNames.ComplexTypeAttribute_OdataComplexType.toString());
				List<SelectionTreeNode> complexTypeAttrNodes = new ArrayList<>(complexTypeAttributes.size());
				for (IMendixObject complexTypeAttrMxObj : complexTypeAttributes) {
					ComplexTypeAttribute complexTypeAttr = ComplexTypeAttribute.initialize(context, complexTypeAttrMxObj);
					SelectionTreeNode newNodeComplexTypeAttr = new SelectionTreeNode(context);
					newNodeComplexTypeAttr.setMendixGUID(context, complexTypeAttrMxObj.getId().toLong());
					newNodeComplexTypeAttr.setMendixType(context, complexTypeAttr.entityName);
					newNodeComplexTypeAttr.setName(context, complexTypeAttr.getAttributeName());
					newNodeComplexTypeAttr.setOdataFullyQuallifiedName(context, complexTypeAttr.getAttributeName());
					newNodeComplexTypeAttr.setRoot(false);
					complexTypeAttrNodes.add(newNodeComplexTypeAttr);
					tree.add(newNodeComplexTypeAttr.getMendixObject());	
				}
				parameterNode.setSelectionTreeNode_Children(complexTypeAttrNodes);
			}
		}

		return tree;
	}

//	public static View getTreeFromJson(IContext context, String json) throws CoreException {
//		View view = new View(context);
//		JSONObject rootObj = new JSONObject(json);
//		SelectionTreeNode requestNode = new SelectionTreeNode(context);
//		requestNode.setRoot(true);
//		requestNode.setName("Request");
//		requestNode.setSelectionTreeNode_View(view);
//		requestNode.setSelectionTreeNode_Children(getRecusrivetree(context, view, rootObj.getJSONObject("OperationInput").toString()));
//		requestNode.commit();
//		SelectionTreeNode responseNode = new SelectionTreeNode(context);
//		responseNode.setRoot(true);
//		responseNode.setName("Response");
//		responseNode.setSelectionTreeNode_View(view);
//		responseNode.setSelectionTreeNode_Children(getRecusrivetree(context, view, rootObj.getJSONObject("OperationResponse").toString()));
//		responseNode.commit();
//		return view;
//	}

	public static List<SelectionTreeNode> getRecursiveTree(IContext context, View view, String jsonString) {
		List<SelectionTreeNode> tree = new ArrayList<>();
		JSONObject input = new JSONObject(jsonString);
		Iterator<String> keys = input.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			if(key.toLowerCase().contains("servicedata"))
				continue;
			Object jsonObj = input.get(key);
			SelectionTreeNode jsonNode = new SelectionTreeNode(context);
			if (jsonObj instanceof JSONObject) {
				jsonNode.setName(key);
				List<SelectionTreeNode> children = getRecursiveTree(context, view, jsonObj.toString());
				jsonNode.setSelectionTreeNode_Children(context, children);
			}
			else if (jsonObj instanceof JSONArray) {
				boolean isImodelMap = false;
				int i = 0;
				for (i = 0; i < ((JSONArray)jsonObj).length(); i++) {
					Object elementOfArray = ((JSONArray)jsonObj).get(i);
					if (elementOfArray instanceof JSONArray) {
						for (int j = 0;  j < ((JSONArray) elementOfArray).length(); j++) {
							if (((JSONArray)elementOfArray).get(j).toString().equalsIgnoreCase("SampleKeyIModelObject")) {
								isImodelMap = true;
								break;
							}
						}
					}
				}
				i = i == 0 ? i : --i ;
				jsonNode.setName(key + "[]");
				if (isImodelMap) {
					JSONObject jObj = new JSONObject();
					jObj.put("SampleKeyIModelObject", ((JSONArray)jsonObj).getJSONArray(i).get(0));
					jsonNode.setSelectionTreeNode_Children(context, getRecursiveTree(context, view, jObj.toString()));;
				}
				else{
					jsonNode.setSelectionTreeNode_Children(context, getRecursiveTree(context, view, ((JSONArray)jsonObj).get(0).toString()));
				}
			}
			else {
				if (((String)jsonObj).startsWith("$")) {
					jsonNode.setAddAsParameter(true);
				}
				jsonNode.setName(context, key + ": " + jsonObj);
			}
			jsonNode.setSelectionTreeNode_View(view);
			tree.add(jsonNode);
		}
		Core.commit(context, tree.stream().map(SelectionTreeNode::getMendixObject).collect(Collectors.toList()));
		return tree;
	}

	public static List<SelectionTreeNode> getRecursiveTreeEdit(IContext context, View view, String originalJson, String modifiedJson) {
		// TODO:
		List<SelectionTreeNode> originalTree = getRecursiveTree(context, view, originalJson);
		List<SelectionTreeNode> modifiedTree = getRecursiveTree(context, view, modifiedJson);

		for (int i = 0; i < originalTree.size(); i++) {
			SelectionTreeNode originalNode = originalTree.get(i);
			SelectionTreeNode modifiedNode = modifiedTree.get(i);
		}
		return originalTree;
	}

	public static JSONObject getJsonFromTree(IContext context, View view) throws CoreException {
		List<IMendixObject> tree = Core.retrieveByPath(context, view.getMendixObject(), SelectionTreeNode.MemberNames.SelectionTreeNode_View.toString());
		for (IMendixObject nodeMxObj : tree) {
			SelectionTreeNode node = SelectionTreeNode.initialize(context, nodeMxObj);
			if (node.getRoot()) {
				if (node.getName().equals("Request")) {
					return getRecursiveJsonForRequest(context, node);
				}
				else if (node.getName().equals("Response")) {
					return getRecursiveJsonForResponse(context, node);
				}
			}
		}
		return null;
	}

	private static JSONObject getRecursiveJsonForRequest(IContext context, SelectionTreeNode rootNode) throws CoreException {
		JSONObject rootJsonNode = new JSONObject();
		List<SelectionTreeNode> children = rootNode.getSelectionTreeNode_Children();
		for (SelectionTreeNode child : children) {
			if (child.getAddAsParameter()) {
				IMendixIdentifier childId = Core.createMendixIdentifier(child.getMendixGUID());
				if (childId.getObjectType().equalsIgnoreCase(ActionParams.getType())){
					ActionParams param = ActionParams.load(context, childId);
					String childName = child.getName();
					if (childName.endsWith("[]")) {
						childName = childName.substring(0, childName.length() - "[]".length());
                    }
                    rootJsonNode.put(childName.split("[:]")[0], "$" + param.getName());
                }
				else if (childId.getObjectType().equalsIgnoreCase(ReturnType.getType())) {
					String childName = child.getName();
					if (childName.endsWith("[]")) {
						childName = childName.substring(0, childName.length() - "[]".length());
                    }
                    rootJsonNode.put(childName.split("[:]")[0], "$" + "response");
                }
			}
			else {
				List<SelectionTreeNode> childsChildren = child.getSelectionTreeNode_Children();
                if (childsChildren.isEmpty()) {
                	String childName = child.getName();//vin
                	/*if (childName.contains("SampleKeyString") || childName.contains("SampleKeyIModelObject") || childName.contains("SampleKeyInt"))
                	{
                		//JSONObject j = new JSONObject();
                		//rootJsonNode.put("",j);
                		//rootJsonNode = new JSONObject();
                		//child.setName("");
                		
                		rootJsonNode.put(childName.split("[:]")[0],"");

                		rootJsonNode.remove(childName.split("[:]")[0]);
                	}
                	else*/ 
                	if (childName.endsWith("[]"))
                	{
                		JSONArray temp = new JSONArray();
                		rootJsonNode.put(childName.split("[:]")[0], temp);
                	}
                	else if (childName.endsWith("String") || childName.endsWith("Date") || childName.endsWith("IModelObject"))
                	{
                		
                		rootJsonNode.put(childName.split("[:]")[0],"");
                	}
                	else if (childName.endsWith("int") || childName.endsWith("double") || childName.endsWith("float"))
                	{
                		int tempInt = 0;
                		rootJsonNode.put(childName.split("[:]")[0],tempInt);
                	}
                	else if (childName.endsWith("bool"))
                	{
                		boolean tempBool = false;
                		rootJsonNode.put(childName.split("[:]")[0],tempBool);
                	}
                	else
                	{
                		if(childName.contains("::"))
						{
                			rootJsonNode.put(childName.split("[:]")[0],"");
						}
                		else if((childName.split("[:]")[1]).split("[ ]").length > 1)
						{
                			String enumValues = childName.split("[:]")[1];
                			rootJsonNode.put(childName.split("[:]")[0],enumValues.trim().split("[ ]")[0]);
						}

                	}
                	continue;
                }
				JSONObject childTree = getRecursiveJsonForRequest(context, child);
				if (!childTree.keys().hasNext()) {
					continue;
				}
                String childName = child.getName();
                if (childName.endsWith("[]")) {
                    childName = childName.substring(0, childName.length() - "[]".length());
                    JSONArray childJsonArray = new JSONArray();
                    childJsonArray.put(childTree);
                    rootJsonNode.put(childName, childJsonArray);
                }
                else {
                    rootJsonNode.put(childName, childTree);
                }
			}
		}
		return rootJsonNode;
	}

	private static JSONObject getRecursiveJsonForResponse(IContext context, SelectionTreeNode rootNode) throws CoreException {
		JSONObject rootJsonNode = new JSONObject();
		List<SelectionTreeNode> children = rootNode.getSelectionTreeNode_Children();
		for (SelectionTreeNode child : children) {
			if (child.getAddAsParameter()) {
				IMendixIdentifier childId = Core.createMendixIdentifier(child.getMendixGUID());
				if (childId.getObjectType().equalsIgnoreCase(ActionParams.getType())){
					ActionParams param = ActionParams.load(context, childId);
					String childName = child.getName();
					if (childName.endsWith("[]")) {
						childName = childName.substring(0, childName.length() - "[]".length());
                    }
                    rootJsonNode.put(childName.split("[:]")[0], "$" + param.getName());
                }
				else if (childId.getObjectType().equalsIgnoreCase(ReturnType.getType())) {
					String childName = child.getName();
					if (childName.endsWith("[]")) {
						childName = childName.substring(0, childName.length() - "[]".length());
                    }
                    rootJsonNode.put(childName.split("[:]")[0], "$" + "response");
                }
			}
			else {
				List<SelectionTreeNode> childsChildren = child.getSelectionTreeNode_Children();
                if (childsChildren.isEmpty()) {
                	continue;
                }
				JSONObject childTree = getRecursiveJsonForResponse(context, child);
				if (!childTree.keys().hasNext()) {
					continue;
				}
                String childName = child.getName();
                if (childName.endsWith("[]")) {
                    childName = childName.substring(0, childName.length() - "[]".length());
                    JSONArray childJsonArray = new JSONArray();
                    childJsonArray.put(childTree);
                    rootJsonNode.put(childName, childJsonArray);
                }
                else {
                    rootJsonNode.put(childName, childTree);
                }
			}
		}
		return rootJsonNode;
	}
	
	public static OdataComplexType mapChildren(IContext context, SelectionTreeNode node, ContractNameSpaces cns, ActionParams actionParams) throws CoreException {
		return mapChildrenInternal(context, node, cns, actionParams, null);
	}
	public static OdataComplexType mapChildren(IContext context, SelectionTreeNode node, ContractNameSpaces cns, ReturnType returnType) throws CoreException {
		return mapChildrenInternal(context, node, cns, null, returnType);
	}
	
	private static String getDataType(String dataType)
    {
    	switch (dataType) {
		case "int":
			return "Int64";
		case "bool":
			return "Boolean";
		case "double":
			return "Double";
		case "float":
			return "Double";
		case "Date" :
			return "Date";
		default:
			return "String";
		}
    }
	
	public static OdataComplexType mapChildrenInternal(IContext context, SelectionTreeNode node, ContractNameSpaces cns, ActionParams actionParams, ReturnType returnType) throws CoreException {
		List<SelectionTreeNode> children = node.getSelectionTreeNode_Children();
		OdataComplexType complexType = new OdataComplexType(context);
		
		List<IMendixObject> allComplexType = Core.retrieveByPath(context, cns.getMendixObject(), "SchemaGenerator.OdataComplexType_ContractNameSpaces");
		
		String parentName = node.getName();
		if (parentName.endsWith("[]")) {
			parentName = parentName.substring(0, parentName.length() - "[]".length());
		}
		
		String uniqueComplexTypeName = parentName;
		
		if(allComplexType.size()>0)
		{
			List<String> complexTypeNames = new ArrayList<String>();
			for(int nameCount=0; nameCount<allComplexType.size(); ++nameCount)
			{
				IMendixObjectMember<?> ename = allComplexType.get(nameCount).getMember(context, "ComplexTypeName");
				complexTypeNames.add((ename.getValue(context).toString()));
			}
			
			int countToAppend=0;
			while(true)
			{
				if(complexTypeNames.contains(cns.getContractNameSpace()+"."+uniqueComplexTypeName))
				{
					countToAppend++;
					uniqueComplexTypeName = parentName + countToAppend;
				}
				else
				{
					break;
				}
			}
		}
		
		complexType.setOdataComplexType_ContractNameSpaces(cns);
		if (actionParams != null) {
			complexType.setOdataComplexType_ActionParams(actionParams);
		}
		if (returnType != null) {
			complexType.setOdataComplexType_ReturnType(returnType);
		}
		/*String parentName = node.getName();
		if (parentName.endsWith("[]")) {
			parentName = parentName.substring(0, parentName.length() - "[]".length());
		}*/
		
		//complexType.setComplexTypeName(cns.getContractNameSpace() + "." + parentName);
		complexType.setComplexTypeName(cns.getContractNameSpace() + "." + uniqueComplexTypeName);
		for (SelectionTreeNode child : children) {
			String name = child.getName();
			boolean childIsArray = false;
			if (name.endsWith("[]")) {
				name = name.substring(0, name.length() - "[]".length());
				childIsArray = true;
			}
			if (name.contains("SampleKeyString")) {
				if (name.split("[:]")[1].trim().equals("String") && childIsArray) {
					complexType.delete();
					return addMapComplexType(context, cns, actionParams, "NameValuePair");
				}
				else {
					FeedbackHelper.addTextMessageFeedback(
							context,
							IFeedback.MessageType.INFO,
							"Types Other than String[] not supported for map",
							true
					);
					return null;
				}
			}
			String[] nameParts = name.split("[:]");

			ComplexTypeAttribute complexTypeAttribute = new ComplexTypeAttribute(context);
			complexTypeAttribute.setAttributeName(nameParts[0].trim());
			complexTypeAttribute.setComplexTypeAttribute_OdataComplexType(complexType);
			complexTypeAttribute.setIsCollection(childIsArray);
			complexTypeAttribute.setIsRequired(false);
			complexTypeAttribute.setInherited(false);
			if (nameParts.length >= 2) {
				if (nameParts[1].trim().equals("IModelObject")) {
					complexTypeAttribute.setReferenceType("Select Type");
					complexTypeAttribute.setOriginalAttributeDataType("Reference");
				}
				else {
					String dataType = nameParts[1].trim();
					complexTypeAttribute.setAttributeDataType("Edm." + getDataType(dataType));
					complexTypeAttribute.setOriginalAttributeDataType("Edm." + dataType);
				}
			}
			else {
				complexTypeAttribute.setAttributeDataType(mapChildrenInternal(context, child, cns, actionParams, returnType).getComplexTypeName());
			}
			complexTypeAttribute.commit();
		}
		return complexType;
	}

	public static OdataComplexType addMapComplexType(IContext context, ContractNameSpaces cns,ActionParams actionParams, String complexTypeName) {
		OdataComplexType complexType = findIfExists(context, cns, complexTypeName);
		if (complexType == null) {
			// Create ComplexType according to requirement (i.e. according to value of complexTypeName)
			if (complexTypeName.equals("NameValuePair")) {
				complexType = new OdataComplexType(context);
				complexType.setComplexTypeName(cns.getContractNameSpace() + "." + complexTypeName);
				complexType.setOdataComplexType_ActionParams(actionParams);
				complexType.setOdataComplexType_ContractNameSpaces(cns);

				ComplexTypeAttribute propertyName = new ComplexTypeAttribute(context);
				propertyName.setComplexTypeAttribute_OdataComplexType(complexType);
				propertyName.setAttributeName("propertyName");
				propertyName.setAttributeDataType("String");
				propertyName.setInherited(false);
				propertyName.setIsCollection(true);
				propertyName.setIsRequired(true);
				ComplexTypeAttribute propertyValue = new ComplexTypeAttribute(context);
				propertyValue.setComplexTypeAttribute_OdataComplexType(complexType);
				propertyValue.setAttributeName("propertyValue");
				propertyValue.setAttributeDataType("String");
				propertyValue.setInherited(false);
				propertyValue.setIsCollection(true);
				propertyValue.setIsRequired(true);

				// Commit all three objects
				Core.commit(context, Arrays.asList(complexType.getMendixObject(), propertyName.getMendixObject(), propertyValue.getMendixObject()));
			}
		}
		return complexType;
	}

	public static OdataComplexType findIfExists(IContext context, ContractNameSpaces cns, String complexTypeName) {
		List<IMendixObject> allComplexTypes = Core.retrieveByPath(context, cns.getMendixObject(), OdataComplexType.MemberNames.OdataComplexType_ContractNameSpaces.toString());
		String contractName = cns.getContractNameSpace();
		for(IMendixObject complexType : allComplexTypes) {
			String name = complexType.<String>getValue(context, OdataComplexType.MemberNames.ComplexTypeName.toString());
			if (name.equals(contractName + "." + complexTypeName)) {
				return OdataComplexType.initialize(context, complexType);
			}
		}
		return null;
	}

	public static boolean areChildrenAddedAsParams(SelectionTreeNode node) throws CoreException {
		List<SelectionTreeNode> children = node.getSelectionTreeNode_Children();
		for (SelectionTreeNode child : children) {
			if (areChildrenAddedAsParams(child)) {
				return true;
			}
			if (child.getAddAsParameter()) {
				return true;
			}
		}
		return false;
	}

	public static boolean areParentsAddedAsParams(IContext context, SelectionTreeNode node) throws CoreException {
	    List<IMendixObject> parents = Core.retrieveByPath(context, node.getMendixObject(), SelectionTreeNode.MemberNames.SelectionTreeNode_Children.toString(), true);
        while(!parents.isEmpty()) {
            if (parents.get(0).<Boolean>getValue(context, SelectionTreeNode.MemberNames.AddAsParameter.toString())) {
                return true;
            }
            parents = Core.retrieveByPath(context, parents.get(0), SelectionTreeNode.MemberNames.SelectionTreeNode_Children.toString(), true);
        }
        return false;
    }

	private TcODataSchemaHelper() {}

}
