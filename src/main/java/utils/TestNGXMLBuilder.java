package utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestNGXMLBuilder {
	
    public static String getTestSuiteXML(String[] args) throws Exception {
       
    	// Create DocumentBuilder
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		// Create root element
		Document doc = docBuilder.newDocument();
		Element suiteElement = doc.createElement("suite");
		suiteElement.setAttribute("name", "Suite");
		suiteElement.setAttribute("thread-count", "1");
		suiteElement.setAttribute("parallel", "none");
		suiteElement.setAttribute("data-provider-thread-count", "20");
		suiteElement.setAttribute("time-out", "10000000");
		doc.appendChild(suiteElement);


		for(String arg: args) {
			if( arg.equals("oc-ui")) {
			      // Add test elements
		        addTestElement(doc, suiteElement, "OrchestratorUI", "none",
		                "tests.orchestrator.DeleteCustomerTest", 
		                "tests.orchestrator.AddCustomerTest",
		                "tests.orchestrator.AddInternalUsersTest",
		                "tests.orchestrator.ChangeCustomerDomain",
		                "tests.orchestrator.ImportLicensesTest", 
		                "tests.orchestrator.ResourcesManagerTest",
		                "tests.orchestrator.SynchronizeTenantLicensesTest",
		                "tests.orchestrator.TroubleshootingPackageTest",
		                "tests.orchestrator.TroubleshootingLogTest",
		                "tests.orchestrator.DeleteCustomerTest",
		                "tests.orchestrator.UpdateApplianceVersionTest",
		                "tests.orchestrator.TroubleshootingLogTest",
		                "tests.orchestrator.SystemBackupTest",
		                "tests.orchestrator.TroubleshootingPackageTest"
		                );
			}

			if( arg.equals("tnt-ui")) {
				  addTestElement(doc, suiteElement, "TenantUI", "none",
						  "tests.orchestrator.DeleteCustomerTest", 
						    "tests.orchestrator.AddCustomerTest",
		                    "tests.tenant.superadmin.ImportPhysicalTokensTest", 
		                    "tests.tenant.superadmin.InternalUsersTest",
		                    "tests.tenant.superadmin.LDAPConfigurationTest", 
		                    "tests.tenant.superadmin.AgentConnectorTest",
		                    "tests.tenant.superadmin.DefaultSettingTest", 
		                    "tests.tenant.superadmin.MessageDeliveryGatewayTest",
		                    "tests.tenant.superadmin.OrganizationLogoTest", 
		                    "tests.tenant.superadmin.RadiusClientTest",
		                    "tests.tenant.superadmin.SAMLIntegrationTest", 
		                    "tests.tenant.selfserviceportal.SelfServicePortalTest",
		                    "tests.tenant.managementconsole.ManagementConsoleTest",
		                    "tests.tenant.superadmin.TroubleshootingLogTest",
		                    "tests.tenant.superadmin.TroubleshootingPackageTest"
		                    
						    );
			}
			if( arg.equals("auth-api")) {
				  addTestElement(doc, suiteElement, "AuthAPI", "none", 
						  "tests.orchestrator.DeleteCustomerTest", 
						    "tests.orchestrator.AddCustomerTest",
		                    "tests.tenant.authentication.AuthenticationTestSetup",
		                    "tests.tenant.authentication.TestUsersCleanup",
		                    "tests.tenant.authentication.api.APISingleStepStaticPassword",
		                    "tests.tenant.authentication.api.APISingleStepBackupCode",
		                    "tests.tenant.authentication.api.APISingleStepTOTP", 
		                    "tests.tenant.authentication.api.API2StepTOTP",
		                    "tests.tenant.authentication.api.API2StepBackupCode",
		                    "tests.tenant.authentication.api.APISingleStepPush",
		                    "tests.tenant.authentication.api.APIPushFallbackTest", 
		                    "tests.tenant.authentication.api.API2StepVirtualOTP",
		                    "tests.tenant.authentication.api.APISingleStepPasswordTOTP",
		                    "tests.tenant.superadmin.TroubleshootingPackageTest"

		                    
		                    );
			 
				}
					
			if( arg.equals("auth-radius")) {
				 addTestElement(doc, suiteElement, "AuthRadius", "none", 
						  "tests.orchestrator.DeleteCustomerTest", 
						  "tests.orchestrator.AddCustomerTest" , 
					      "tests.tenant.authentication.AuthenticationTestSetup",  
					      "tests.tenant.authentication.TestUsersCleanup"  ,
					      "tests.tenant.authentication.radius.RadiusSingleStepStaticPassword" , 
					      "tests.tenant.authentication.radius.RadiusSingleStepBackupCode", 
					      "tests.tenant.authentication.radius.RadiusSingleStepPush"  ,
					      "tests.tenant.authentication.radius.Radius2StepBackupCode"  ,
					      "tests.tenant.authentication.radius.RadiusSingleStepTOTP"  ,
					      "tests.tenant.authentication.radius.RadiusSingleStepPush"  ,
					      "tests.tenant.authentication.radius.Radius2StepTOTP"  ,
					      "tests.tenant.authentication.radius.Radius2StepBackupCode",    
					      "tests.tenant.authentication.radius.RadiusSingleStepPasswordTOTP",
					      "tests.tenant.superadmin.TroubleshootingPackageTest"
						 );
		}

			if( arg.equals("postman-oc")) {
				  addTestElement(doc, suiteElement, "PostmanOC", "none", 
			      "tests.orchestrator.postman.PostmanAPITest"
						  );
				  
			}
			if( arg.equals("postman-tnt")) {
				  addTestElement(doc, suiteElement, "PostmanTNT", "none", 
					      "tests.tenant.postman.PostmanAPITest"
								  );				
			}
		}
		
		addTestElement(doc, suiteElement, "Cleanup", "none",
                "tests.tenant.superadmin.TroubleshootingPackageTest",
                "tests.orchestrator.TroubleshootingPackageTest"
                );

		return getStringFromDocument(doc);


    }

    // Helper method to add test elements
    private static void addTestElement(Document doc, Element suiteElement, String testName, String parallel,
            String... classNames) {
        Element testElement = doc.createElement("test");
        testElement.setAttribute("name", testName);
        testElement.setAttribute("thread-count", "1");
        testElement.setAttribute("parallel", parallel);

        Element classesElement = doc.createElement("classes");
        for (String className : classNames) {
            Element classElement = doc.createElement("class");
            classElement.setAttribute("name", className);
            classesElement.appendChild(classElement);
        }

        testElement.appendChild(classesElement);
        suiteElement.appendChild(testElement);
    }

    // Helper method to convert Document to String
    private static String getStringFromDocument(Document document) {
        try {
            javax.xml.transform.TransformerFactory transformerFactory = javax.xml.transform.TransformerFactory
                    .newInstance();
            javax.xml.transform.Transformer transformer = transformerFactory.newTransformer();
            javax.xml.transform.dom.DOMSource source = new javax.xml.transform.dom.DOMSource(document);
            java.io.StringWriter writer = new java.io.StringWriter();
            javax.xml.transform.stream.StreamResult result = new javax.xml.transform.stream.StreamResult(writer);
            transformer.transform(source, result);
            return writer.toString();
        } catch (javax.xml.transform.TransformerException e) {
            e.printStackTrace();
            return null;
        }
    }
}
