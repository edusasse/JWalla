package com.edusasse.jwalla.server.corba;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class CorbaUtils {
	private static String convertStreamToString(InputStream is) {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return sb.toString();
	}

	private static final boolean isCorbaRunning() throws IOException {
		boolean isWindows = true;
		if (isWindows) {
			final String myReturn = convertStreamToString(Runtime
					.getRuntime().exec("TASKLIST /SVC").getInputStream());
			if (myReturn.toLowerCase().contains("orbd.exe"))
				return true;
		} else {
			// TODO linux
		}
		return false;
	}
}
