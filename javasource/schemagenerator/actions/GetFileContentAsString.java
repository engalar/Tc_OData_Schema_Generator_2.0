// This file was generated by Mendix Studio Pro.
//
// WARNING: Only the following code will be retained when actions are regenerated:
// - the import list
// - the code between BEGIN USER CODE and END USER CODE
// - the code between BEGIN EXTRA CODE and END EXTRA CODE
// Other code you write will be lost the next time you deploy the project.
// Special characters, e.g., é, ö, à, etc. are supported in comments.

package schemagenerator.actions;

import com.mendix.core.Core;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.webui.CustomJavaAction;
import org.apache.commons.io.IOUtils;
import com.mendix.systemwideinterfaces.core.IMendixObject;

public class GetFileContentAsString extends CustomJavaAction<java.lang.String>
{
	private IMendixObject __fileDocument;
	private system.proxies.FileDocument fileDocument;

	public GetFileContentAsString(IContext context, IMendixObject fileDocument)
	{
		super(context);
		this.__fileDocument = fileDocument;
	}

	@java.lang.Override
	public java.lang.String executeAction() throws Exception
	{
		this.fileDocument = this.__fileDocument == null ? null : system.proxies.FileDocument.initialize(getContext(), __fileDocument);

		// BEGIN USER CODE
		return IOUtils.toString(Core.getFileDocumentContent(getContext(), __fileDocument));
		// END USER CODE
	}

	/**
	 * Returns a string representation of this action
	 * @return a string representation of this action
	 */
	@java.lang.Override
	public java.lang.String toString()
	{
		return "GetFileContentAsString";
	}

	// BEGIN EXTRA CODE
	// END EXTRA CODE
}
