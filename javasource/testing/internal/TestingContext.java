package testing.internal;

import org.junit.Assert;

import com.mendix.core.Core;
import com.mendix.core.CoreException;
import com.mendix.systemwideinterfaces.core.IContext;
import com.mendix.systemwideinterfaces.core.ISession;
import com.mendix.systemwideinterfaces.core.IUser;

public class TestingContext 
{
	private static IContext CONTEXT = null;
		
		public static IContext getTestContext()
		{
			// Execute the Tests using user for which Access rules are defined
			// Creating context using 'MxAdmin' user for which Access Rules are
			// already defined.
			if(CONTEXT == null)
			{
				IContext systemContext   = Core.createSystemContext();
				IUser user = null;
				try {
					user = Core.getUser(systemContext, "MxAdmin");
					ISession session = Core.initializeSession(user, null);
					CONTEXT = session.createContext();
				} catch (CoreException e) {
					Assert.fail("Failed to create a Context. "+ e.getMessage());
				}
			}
			return CONTEXT;
		}
}
