package com.bluestone.app.uniware;

import com.unicommerce.uniware.services.Error;
import com.unicommerce.uniware.services.ServiceResponse;
import com.unicommerce.uniware.services.Warning;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Rahul Agrawal
 *         Date: 9/28/12
 */
class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    static void parseResponse(ServiceResponse response, String inputData) {
        parseErrors(response.getErrors(), inputData);
        parseWarnings(response.getWarnings());
    }

    static void parseWarnings(Warning[] warnings) {
        if (warnings != null) {
            log.warn("ErrorHandler.parseWarnings()");
            for (int i = 0; i < warnings.length; i++) {
                Warning eachWarning = warnings[i];
                log.warn("Uniware Warning: Code={} , Message={} , Description={}",
                         eachWarning.getCode(),
                         eachWarning.getDescription(),
                         eachWarning.getMessage());
            }
        }
    }

    static void parseErrors(Error[] errors, String inputMessage) {
        if (errors != null) {
            //log.error("ErrorHandler.parseErrors(): Input Identifier={} ", inputMessage);
            for (int i = 0; i < errors.length; i++) {
                Error eachError = errors[i];
                log.error("Uniware Error details: Code={} Message={} Description={} for identifier={}",
                          eachError.getCode(),
                          eachError.getDescription(),
                          eachError.getMessage(),
                          inputMessage);

            }
        }
    }
}
