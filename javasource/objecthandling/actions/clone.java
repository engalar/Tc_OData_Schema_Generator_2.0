// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package objecthandling.actions;

import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.IMendixObject;
import com.mendix.webui.CustomJavaAction;
import objecthandling.ORM;

/**
 * Clones objects
 * 
 * - Source: the original object to copy
 * - Target: the object to copy it into (should be of the same type, or a specialization)
 * - includeAssociations: whether to clone associations. 
 * 
 * If associated objects need to be cloned as well, use deepClone, this function only copies the references, not the reffered objects. Target is not committed automatically.
 */
public class clone extends CustomJavaAction<java.lang.Boolean>
{
	private IMendixObject source;
	private IMendixObject target;
	private java.lang.Boolean withAssociations;

	public clone(IContext context, IMendixObject source, IMendixObject target, java.lang.Boolean withAssociations)
	{
		super(context);
		this.source = source;
		this.target = target;
		this.withAssociations = withAssociations;
	}

	@java.lang.Override
	public java.lang.Boolean executeAction() throws Exception
	{
		// BEGIN USER CODE
		return ORM.cloneObject(this.getContext(), source, target, withAssociations);
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "clone";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
