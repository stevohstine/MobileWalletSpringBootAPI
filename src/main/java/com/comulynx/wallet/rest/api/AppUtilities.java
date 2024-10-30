package com.comulynx.wallet.rest.api;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author user
 *
 */
@Configuration
@EnableAutoConfiguration
public class AppUtilities {
	public AppUtilities() {

	}

	/**
	 * Get exception string stack trace
	 *
	 * @param ex
	 * @return
	 */
	public static String getExceptionStacktrace(Exception ex) {
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		return sw.toString();
	}

}
