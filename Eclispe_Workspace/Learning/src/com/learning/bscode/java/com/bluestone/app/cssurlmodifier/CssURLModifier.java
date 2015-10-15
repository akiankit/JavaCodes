package com.bluestone.app.cssurlmodifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CssURLModifier {
	
	private static final Logger log = LoggerFactory.getLogger(CssURLModifier.class);

	public static void main(String[] args) {		
		String buildMode = args[0];
		log.info("************ build mode {}", buildMode);
		log.info("************ css path {}", args[1]);
		log.info("************ cdnNames {}", args[2]);
		String[] cdnNames = StringUtils.split(args[2],",");
		for (String eachCdnName : cdnNames) {
		    log.info("********* Each CdnName {}, ", eachCdnName);
        }
		if(StringUtils.isNotEmpty(buildMode) && (buildMode.equalsIgnoreCase("test") || buildMode.equalsIgnoreCase("prod"))){
			log.info("################ CSS URL Modification Started ########################");
			String cssFilePath = args[1];
			log.info("CSS File Path: "+cssFilePath);
			if (StringUtils.isNotEmpty(cssFilePath)) {
				BufferedReader bufferedReader = null;
				BufferedWriter bufferedWriter = null;
				String newCssFilePath = cssFilePath +".bak";
				try {
					bufferedReader = new BufferedReader(new FileReader(cssFilePath));
					bufferedWriter = new BufferedWriter(new FileWriter(newCssFilePath));
					String nextLine = "";
					Pattern pattern = Pattern.compile("url\\(\\.\\.(.*?)\\)");
					while ((nextLine = bufferedReader.readLine()) != null) {
					    log.info("***** each line {}" , nextLine);
						Matcher matcher = pattern.matcher(nextLine);
						String newline = nextLine;
						while (matcher.find()) {
							String actualURL = matcher.group(1);
							log.info("***** actualURL {}" , actualURL);
                            String newUrlWithCDNName = getStaticCdnLink("resources/themes/bluestone" + actualURL, cdnNames);
							log.info("***** newUrlWithCDNName {}" , newUrlWithCDNName);
							
							newUrlWithCDNName = "url(" + newUrlWithCDNName + ")";
							log.info("***** newUrlWithCDNName after replacement{} " , newUrlWithCDNName);
							
							String fullStringWithURLNamespace = matcher.group(0);
							log.info("***** fullStringWithURLNamespace {}" , fullStringWithURLNamespace);
                            newline = newline.replace(fullStringWithURLNamespace, newUrlWithCDNName);
                            log.info("***** after replacement {} " , newline);
						}					
						bufferedWriter.write(newline);
					}
				} catch (Exception e) {
					log.error("Error while modifying urls in css file", e);
				} finally {
					if (bufferedReader != null) {
			            try {
			            	bufferedReader.close();
			            } catch (IOException e) {
			                if (log.isErrorEnabled()) {
			                    log.error("Error while closing buffered reader", e);
			                }
			            }
			        }
					if (bufferedWriter != null) {
			            try {
			            	bufferedWriter.close();
			            } catch (IOException e) {
			                if (log.isErrorEnabled()) {
			                    log.error("Error while closing buffered writer", e);
			                }
			            }
			        }
				}

				File oldFile = new File(cssFilePath);			
				oldFile.delete();

				File newFile = new File(newCssFilePath);
				newFile.renameTo(new File(cssFilePath));
				
			} else {
				if (log.isErrorEnabled()) {
					log.info("################ CSS File Path Not Found ########################");
				}
			}
			log.info("################ CSS URL Modification Ended ########################");
		}
	}
	
	private static String getStaticCdnLink(String url, String[] staticCdnHosts) {
		if (log.isDebugEnabled()) {
			log.debug("CssURLModifier.getStaticCdnLink() for {}", url);
		}        
		int hashCode = url.hashCode();
		int mod = Math.abs(hashCode % staticCdnHosts.length);		
		String cdnHostName = staticCdnHosts[mod];
		StringBuilder stringBuilder = new StringBuilder(cdnHostName.trim());
		stringBuilder.append("/").append(url);
        String result = stringBuilder.toString();
		if (log.isDebugEnabled()) {
			log.debug("CssURLModifier.getStaticCdnLink() resulted in {}", result);
		}
        return result;
	}
	
}