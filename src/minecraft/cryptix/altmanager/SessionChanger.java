package cryptix.altmanager;

import cryptix.altmanager.auth.MicrosoftAuthResult;
import cryptix.altmanager.auth.MicrosoftAuthenticationException;
import cryptix.altmanager.auth.MicrosoftAuthenticator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Session;

public class SessionChanger {
	private MicrosoftAuthenticator auth;
	private static SessionChanger instance;
	private final Minecraft mc = Minecraft.getMinecraft();
	public void loginCracked(String n) {
		mc.setSession(new Session(n, n, "0", "legacy"));
    }
	public void loginMicrosoft(String email, String password) {
		MicrosoftAuthenticator authenticator = new MicrosoftAuthenticator();
		try {
			MicrosoftAuthResult acc = authenticator.loginWithCredentials(email, password);
			Minecraft.getMinecraft().setSession(new Session(acc.getProfile().getName(), acc.getProfile().getId(), acc.getAccessToken(), "legacy"));

		} catch (MicrosoftAuthenticationException e) {

		}
    }
	
	public static SessionChanger instance() {
		if (instance == null) {
			instance = new SessionChanger();
		}

		return instance;
	}
	
}
