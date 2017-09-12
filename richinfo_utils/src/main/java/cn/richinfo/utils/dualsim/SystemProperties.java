package cn.richinfo.utils.dualsim;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SystemProperties {

	public static String getProperty(String propertyKey) throws IOException, InterruptedException {
		String execResult = execCommandGetLine("getprop " + propertyKey);
		return execResult;
	}

	private static String execCommandGetLine(String command) throws IOException, InterruptedException {
		String resultStr = null;

		Runtime runtime = Runtime.getRuntime();
		Process proc = runtime.exec(command);

		int exit = proc.waitFor();
		if (exit == 0) {
			InputStream inputStream = proc.getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
			try {
				resultStr = br.readLine();
			} finally {
				inputStream.close();
			}
		}
		return resultStr;
	}
}
