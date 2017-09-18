package ch.fhnw.imvs.automation.java.api;

import java.io.IOException;
import org.apache.catalina.webresources.TomcatURLStreamHandlerFactory;
import org.json.simple.parser.ParseException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ch.fhnw.imvs.automation.java.AutomationConfiguration;
import ch.fhnw.imvs.automation.java.Processor;
import ch.fhnw.imvs.automation.java.ScanWizard;

@SpringBootApplication
public class AutomationApiApplication {
	
	
	public static void main(String[] args) throws Exception {
	    
	 // Load the wizard
        try {
            if (args[0] != null && args[0].toLowerCase().equals("wizard")){
                ScanWizard.main(null);
                return;
            }
        
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

		// Load the config
		AutomationConfiguration.init(args[0]);
		Processor.main(null);
		
		// Disabled, to avoid SONOS upnp problems
		TomcatURLStreamHandlerFactory.disable();
		SpringApplication.run(AutomationApiApplication.class, args);
		
		
			
	}
}
