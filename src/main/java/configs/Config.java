package configs;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

public class Config {

    public static boolean initialized = false;
    public static URL configPath;
    public static Properties properties;

    public static String safewalkMode;

    // General
    public static String androidHome;
    public static String javaHome;
    public static String tokenLicensePath;
    public static String serverLicensePath;
    public static String physicalLicensePath;
    public static String pswResetLicensePath;
    public static String downloadFolder;
    public static String tpackageFolder;
    public static String tmpFolder;
    public static String resourcesFolder;
    public static String resourceLevelLow;
    public static String resourceLevelMed;
    public static String resourceLevelCust;
    public static String testngPath;
    public static String testngPathTenantUI;
    public static String testngPathOCUI;
    public static String testngAPITntPath;
    public static String testngRadiusPath;
    public static String testnPostmanOCPath;
    public static String testnPostmanTNTPath;
    public static int allSuitesCount;

    public static boolean docker;

    public static boolean seleniumGrid;
    public static String hubUrl;
    public static String deviceTypeGP;
    public static String initTenantLicensesCount;

    // Report
    public static String reportPath;
    public static String reportName;


    // SMS
    public static String smsHost;
    public static Integer smsPort;
    public static String smsToken;


    // Browser
    public static String browser;
    public static String driverPath;


    // Mobile
    public static String platform;
    public static String appiumSchema;
    public static String appiumHost;
    public static Integer appiumPort;
    public static String appiumPath;
    public static String appiumLibrary;
    public static String appiumLog;
    public static String appiumLogLevel;
    public static boolean autoStart;
    public static boolean emulated;
    public static String deviceName;
    public static String platformVersion;
    public static String appPath;
    public static boolean noReset;
    public static boolean fullReset;


    // Tenant
    public static String tnHost;
    public static String gtwHost;
    public static Integer tnSuperAdminPort;
    public static Integer tnManagementConsolePort;
    public static Integer tnSelfServicePortalPort;
    public static Integer tnAdminApiPort;
    public static Integer tnAuthApiPort;
    public static Integer tnUserApiPort;
    public static Integer tnMobileApiPort;

    public static String tnUsername;
    public static String tnPassword;
    public static String tnName;
    public static String tnTargetVersion;
    public static String tnTargetVersionSimplified;

    public static String gatewayUrl;
    public static String gatewayName;
    public static String gatewaySshHost;
    public static String gatewayValue;
    public static String samlUrl;
    public static String worpressLoginSSOFalseUrl;
    public static String wordpressSSOConfigUrl;
    public static String samlMetadataUrl;
    public static String samlSSOServiceUrl;
    public static String samlSingleLogoutService;
    public static String samlIntegrationName;
    public static String samlIntegrationType;
    public static String samlMetadataContent;
    public static String tnStableVersion;
    public static String tnApplianceVersion;
    public static String tnServerVersion;
    public static String tnManagementVersion;
    public static String tnRadiusVersion;
    public static String tnRabbitmqVersion;
    public static String tnMemcachedVersion;
    public static String tnSafewalkGatewayVersion;
    public static String tnWebCertificatePath;

    public static String tnApiUsername;
    public static String tnApiPassword;

    public static String tnManagementToken;
    public static String tnAuthenticationToken;
    public static String tnSSPToken;

    public static String tnV3SSHUser;
    public static String tnV3SSHPassword;
    public static String tnV3SSHHost;
    public static Integer tnV3SSHPort;

    public static String RegistrationLinkUri;
    public static String imagePath;
    public static String wordpressMetadata;
    public static String signData;
    public static String signTitle;
    public static String signBody;

    // Orchestrator
    public static String ocHost;
    public static Integer ocSuperAdminPort;
    public static String ocUsername;
    public static String ocPassword;
    public static String ocOauth2Token;
    public static String ocAdminApiPort;

    // SMTP
    public static String smtpType;
    public static String smtpHost;
    public static String smtpPort;
    public static String smtpUser;
    public static String smtpPassword;
    public static String smtpName;
    public static String smtpLink;


    //Agent
    public static String agentHost;
    public static Integer agentPort;
    public static String agentPassword;
    public static String agentUsername;
    public static String agentVersion;
    public static String agentConnectorName;
    public static String agentMaxConnections;


    //LDAP
    public static String ldapName;
    public static String ldapType;
    public static String ldapHost;
    public static Integer ldapPort;
    public static String ldapDomain;
    public static String ldapBindDn;
    public static String ldapBindPassword;
    public static String ldapRootBaseDn;
    public static String ldapUserBaseDn;
    public static String ldapPriority;


    //Radius
    public static String radiusBrokerHost;
    public static String radiusSharedSecret;
    public static String radiusServerClientName;
    public static String radiusBrokerClientName;

    public static String radiusServerClientAddress;
    public static String radiusDefaultTimeout;
    public static String radiusServerHost;
    public static String radiusBrokerClientAddress;
    public static String radiusBrokerPort;
    public static String radiusServerPort;

    //Postman
    public static String postmanEnvironmentPath;
    public static String postmanOcCollectionPath;
    public static String postmanTenantCollectionPath;

    //System Backup
    public static String systemBackupHost;
    public static String systemBackupUsername;
    public static String systemBackupPassword;
    public static String systemBackupDestination;

    public URL getPropertiesPath() {
        if (Config.configPath == null) {
            Config.configPath = getClass().getClassLoader().getResource("tom.config.properties");
        }
        return Config.configPath;
    }

    public static void setFsProperty(String key, String value) throws IOException {
        FileWriter writer = new FileWriter(Config.tmpFolder + "/" + key);
        writer.write(value);
        writer.close();
    }

    public static String getFsProperty(String key) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(Config.tmpFolder + "/" + key));
        String line = reader.readLine();
        reader.close();
        return line.trim();
    }

    public static String getProperty(String key, String defaultValue) throws IOException, URISyntaxException {
        if (Optional.ofNullable(System.getenv("docker")).orElse("false").equals("true")) {
            return Optional.ofNullable(System.getenv(key)).orElse(defaultValue);
        } else {
            if (Config.properties == null) {
                URL resource = new Config().getPropertiesPath();
                String propertiesPath = Paths.get(resource.toURI()).toFile().getAbsolutePath();
                FileInputStream fileInputStream = new FileInputStream(propertiesPath);

                Config.properties = new Properties();
                Config.properties.load(fileInputStream);
            }
            return Config.properties.getProperty(key, defaultValue);
        }

    }

    public static void load() throws IOException, URISyntaxException {
        if (Config.initialized) {
            return;
        }


        // General
        Config.androidHome = (String) Config.getProperty("androidHome", "/home/safewalk/Android/Sdk/");
        Config.javaHome = (String) Config.getProperty("javaHome", "/usr/lib/jvm/java-8-openjdk-amd64");
        Config.tokenLicensePath = (String) Config.getProperty("tokenLicensePath", "/app/licenses/ServerLicense.lic");
        Config.serverLicensePath = (String) Config.getProperty("serverLicensePath", "/app/licenses/ServerLicense.lic");
        Config.physicalLicensePath = (String) Config.getProperty("physicalLicensePath", "/app/licenses/PhysicalLicense.lic");
        Config.pswResetLicensePath = (String) Config.getProperty("pswResetLicensePath", "/app/licenses/PswdResetLicense.lic");
        Config.downloadFolder = (String) Config.getProperty("downloadFolder", "/app/safewalk-testing/");
        Config.tmpFolder = (String) Config.getProperty("tmpFolder", "/app/tmp");
        Config.resourcesFolder = (String) Config.getProperty("resourcesFolder", "/app/safewalk-testing/src/main/resources");
        Config.testngPath = (String) Config.getProperty("testngPath", "/app/safewalk-testing/src/main/resources/testng.xml");
        Config.testngPathTenantUI = (String) Config.getProperty("testngPathTenantUI", "/app/safewalk-testing/src/main/resources/testngTenantUI.xml");
        Config.testngPathOCUI = (String) Config.getProperty("testngPathOCUI", "/app/safewalk-testing/src/main/resources/testngOCUi.xml");
        Config.testnPostmanOCPath = (String) Config.getProperty("testnPostmanOCPath", "/app/safewalk-testing/src/main/resources/testngOCPostman.xml");
        Config.testnPostmanTNTPath = (String) Config.getProperty("testnPostmanTNTPath", "/app/safewalk-testing/src/main/resources/testngTenantPostman.xml");
        Config.testngAPITntPath = (String) Config.getProperty("testngAPITntPath", "/app/safewalk-testing/src/main/resources/testngAuthApi.xml");
        Config.testngRadiusPath = (String) Config.getProperty("testngRadiusPath", "/app/safewalk-testing/src/main/resources/testngAuthRadius.xml");
        Config.safewalkMode = (String) Config.getProperty("safewalkMode", "v5");
        Config.hubUrl    = (String) Config.getProperty("hubUrl", "http://localhost:4444/wd/hub");
        Config.seleniumGrid   =  Boolean.parseBoolean((String) Config.getProperty("seleniumGrid", "true"));

    Config.docker = Boolean.parseBoolean((String) Config.getProperty("docker", "false"));
        Config.deviceTypeGP = (String) Config.getProperty("deviceTypeGP", "GeneralPurpose");
        Config.initTenantLicensesCount = (String) Config.getProperty("initTenantLicensesCount", "300");
        Config.imagePath = (String) Config.getProperty("imagePath", "/app/safewalk-testing/src/main/resources/swiss.png");
        Config.wordpressMetadata = (String) Config.getProperty("wordpressMetadata", "/app/download/");

        // Report
        Config.reportPath = (String) Config.getProperty("reportPath", "/app/safewalk-testing/");
        Config.reportName = (String) Config.getProperty("reportName", "mtReport");

        // SMS
        Config.smsHost = (String) Config.getProperty("smsHost", "sms.qa.safewalk.info");
        Config.smsPort = Integer.parseInt((String) Config.getProperty("smsPort", "443"));
        Config.smsPort = Integer.parseInt((String) Config.getProperty("smsPort", "443"));


        Config.smsToken = (String) Config.getProperty("smsToken", "Zo3hPVJQEdXSdw2mumL6fX2W6TI2XXOs");


        // Browser
        Config.browser = (String) Config.getProperty("browser", "chrome");
        Config.driverPath = (String) Config.getProperty("driverPath", "/usr/bin/chromedriver");


        // Mobile
        Config.platform = (String) Config.getProperty("platform", "ANDROID");
        Config.appiumSchema = (String) Config.getProperty("appiumSchema", "http");
        Config.appiumHost = (String) Config.getProperty("appiumHost", "127.0.0.1");
        Config.appiumPort = Integer.parseInt((String) Config.getProperty("appiumPort", "4723"));
        Config.appiumPath = (String) Config.getProperty("appiumPath", "/wd/hub");
        Config.appiumLibrary = (String) Config.getProperty("appiumLibrary", "/usr/lib/node_modules/appium/build/lib/main.js");
        Config.appiumLog = (String) Config.getProperty("appiumLog", "/var/logs/appium.log");
        Config.appiumLogLevel = (String) Config.getProperty("appiumLogLevel", "INFO");
        Config.autoStart = Boolean.parseBoolean((String) Config.getProperty("autoStart", "false"));
        Config.emulated = Boolean.parseBoolean((String) Config.getProperty("emulated", "true"));
        Config.deviceName = (String) Config.getProperty("deviceName", "");
        Config.platformVersion = (String) Config.getProperty("platformVersion", "");
        Config.appPath = (String) Config.getProperty("appPath", "");
        Config.noReset = Boolean.parseBoolean((String) Config.getProperty("reset", "true"));
        Config.fullReset = Boolean.parseBoolean((String) Config.getProperty("fullReset", "false"));
        Config.resourceLevelLow = (String) Config.getProperty("resourceLevelLow", "Low");
        Config.resourceLevelMed = (String) Config.getProperty("resourceLevelMed", "Medium");
        Config.resourceLevelCust = (String) Config.getProperty("resourceLevelCust", "Low-Med");


        // Tenant
        Config.tnHost = (String) Config.getProperty("tnHost", "testsuite.dev.safewalk.info");
        Config.gtwHost = (String) Config.getProperty("gtwHost", "testsuite.dev.safewalk.info");
        Config.tnSuperAdminPort = Integer.parseInt((String) Config.getProperty("tnSuperAdminPort", "8448"));
        Config.tnManagementConsolePort = Integer.parseInt((String) Config.getProperty("tnManagementConsolePort", "9444"));
        Config.tnSelfServicePortalPort = Integer.parseInt((String) Config.getProperty("tnSelfServicePortalPort", "9445"));
        Config.tnAdminApiPort = Integer.parseInt((String) Config.getProperty("tnAdminApiPort", "8449"));
        Config.tnAuthApiPort = Integer.parseInt((String) Config.getProperty("tnAuthApiPort", "8449"));
        Config.tnUserApiPort = Integer.parseInt((String) Config.getProperty("tnUserApiPort", "8449"));
        Config.tnMobileApiPort = Integer.parseInt((String) Config.getProperty("tnMobileApiPort", "8446"));
        Config.tnName = (String) Config.getProperty("tnName", "testsuite");
        Config.tnUsername = (String) Config.getProperty("tnUsername", "admin");
        Config.tnPassword = (String) Config.getProperty("tnPassword", "Safewalk1");
        Config.tnTargetVersion = (String) Config.getProperty("tnTargetVersion", "4.10.17 (0-STABLE)");
        Config.tnTargetVersionSimplified = (String) Config.getProperty("tnTargetVersionSimp", "4.11.9 (Beta)");
        Config.tpackageFolder = (String) Config.getProperty("tpackageFolder", "/Users/tom/Downloads/troubleshooting/");
        Config.gatewayUrl = (String) Config.getProperty("gatewayUrl", "https://fast.dev.safewalk.info");
        Config.gatewayName = (String) Config.getProperty("gatewayName", "__mt__");
        Config.gatewaySshHost = (String) Config.getProperty("gatewaySshHost", "127.0.0.1");
        Config.gatewayValue = (String) Config.getProperty("gatewayValue", "1");
        Config.samlUrl = (String) Config.getProperty("samlUrl", "https://wordpress.dev.safewalk.info");
        Config.worpressLoginSSOFalseUrl = (String) Config.getProperty("worpressLoginSSOFalseUrl", "/wp-login.php?use_sso=false");
        Config.wordpressSSOConfigUrl = (String) Config.getProperty("wordpressSSOConfigUrl", "/wp-admin/options-general.php?page=sso_general.php");
        Config.samlMetadataUrl = (String) Config.getProperty("samlMetadataUrl", "/saml/saml2/idp/metadata.php");
        Config.samlSSOServiceUrl = (String) Config.getProperty("samlSSOServiceUrl", "/saml/saml2/idp/SSOService.php");
        Config.samlSingleLogoutService = (String) Config.getProperty("samlSingleLogoutService", "/saml2/idp/SingleLogoutService.php");
        Config.samlMetadataContent = (String) Config.getProperty("samlMetadataContent", "/wp-content/plugins/saml20sso-plugin/saml/www/module.php/saml/sp/metadata.php/1");
        Config.samlIntegrationName = (String) Config.getProperty("samlIntegrationName", "Wordpress Integration");
        Config.samlIntegrationType = (String) Config.getProperty("samlIntegrationType", "Wordpress");

        Config.tnStableVersion = (String) Config.getProperty("tnStableVersion", "4.10.17 (0-STABLE)");
        Config.tnWebCertificatePath = (String) Config.getProperty("tnWebCertificatePath", "/app/assets/certs/cert.pem");
        Config.tnApplianceVersion = (String) Config.getProperty("tnApplianceVersion", "4.10.23");
        Config.tnServerVersion = (String) Config.getProperty("tnServerVersion", "2.10.12");
        Config.tnManagementVersion = (String) Config.getProperty("tnManagementVersion", "2.9.12");
        Config.tnRadiusVersion = (String) Config.getProperty("tnRadiusVersion", "4.2");
        Config.tnRabbitmqVersion = (String) Config.getProperty("tnRabbitmqVersion", "3.9");
        Config.tnMemcachedVersion = (String) Config.getProperty("tnMemcachedVersion", "1");
        Config.tnSafewalkGatewayVersion = (String) Config.getProperty("tnSafewalkGatewayVersion", "4.9.2");

        Config.tnApiUsername = (String) Config.getProperty("tnApiUsername", "testng");
        Config.tnApiPassword = (String) Config.getProperty("tnApiPassword", "Safewalk1");

        Config.tnManagementToken = (String) Config.getProperty("tnManagementToken", "");
        Config.tnAuthenticationToken = (String) Config.getProperty("tnAuthenticationToken", "");
        Config.tnSSPToken = (String) Config.getProperty("tnSSPToken", "");

        Config.tnV3SSHUser = (String) Config.getProperty("userRemote", "root");
        Config.tnV3SSHPassword = (String) Config.getProperty("passwordRemote", "Safewalk1");
        Config.tnV3SSHHost = (String) Config.getProperty("hostRemote", "192.168.140.206");
        Config.tnV3SSHPort = Integer.parseInt((String) Config.getProperty("portRemote", "22"));

        Config.RegistrationLinkUri = (String) Config.getProperty("RegistrationLinkUri", "altipeak://safewalkfastauth/reg/?url=https://testsuite.dev.safewalk.info&session=%(session)s&code=%(code)s");


        // Orchestrator
        Config.ocHost = (String) Config.getProperty("ocHost", "admin");
        Config.ocSuperAdminPort = Integer.parseInt((String) Config.getProperty("ocSuperAdminPort", "22"));
        Config.ocUsername = (String) Config.getProperty("ocUsername", "admin");
        Config.ocPassword = (String) Config.getProperty("ocPassword", "Safewalk1");
        Config.ocOauth2Token = (String) Config.getProperty("ocOauth2Token", "877b799fcb588a6ac0e62b30fb11d4f72f7e945b");
        Config.ocAdminApiPort = (String) Config.getProperty("ocAdminApiPort", "8443");


        // SMTP
        Config.smtpType = (String) Config.getProperty("smtpType", "SMTP");
        Config.smtpHost = (String) Config.getProperty("smtpHost", "smtp.zoho.com");
        Config.smtpPort = (String) Config.getProperty("smtpPort", "587");
        Config.smtpUser = (String) Config.getProperty("smtpUser", "automation@altipeaksecurity.com");
        Config.smtpPassword = (String) Config.getProperty("smtpPassword", "NBLzzyF9aerd");
        Config.smtpName = (String) Config.getProperty("smtpName", "SMTPGTW");
        Config.smtpLink = (String) Config.getProperty("smtpLink", "SMTP");


        // Agent

        Config.agentHost = (String) Config.getProperty("agentHost", "192.168.140.195");
        Config.agentPort = Integer.parseInt((String) Config.getProperty("agentPort", "22"));
        Config.agentUsername = (String) Config.getProperty("agentUsername", "root");
        Config.agentPassword = (String) Config.getProperty("agentPassword", "Safewalk1");
        Config.agentVersion = (String) Config.getProperty("agentVersion", "4.2.10");
        Config.agentConnectorName = (String) Config.getProperty("agentConnectorName", "Agent");
        Config.agentMaxConnections = (String) Config.getProperty("agentMaxConnections", "4");


        //Radius
        Config.radiusServerClientName = (String) Config.getProperty("radiusServerClientName", "RadiusServerClient");
        Config.radiusBrokerClientName = (String) Config.getProperty("radiusBrokerClientName", "RadiusBrokerClient");
        Config.radiusBrokerHost = (String) Config.getProperty("radiusBrokerHost", "192.168.140.195");
        Config.radiusServerHost = (String) Config.getProperty("radiusServerHost", "192.168.140.207");
        Config.radiusBrokerPort = (String) Config.getProperty("radiusBrokerPort", "1812");
        Config.radiusServerPort = (String) Config.getProperty("radiusServerPort", "60000");
        Config.radiusBrokerClientAddress = (String) Config.getProperty("radiusBrokerClientAddress", "192.168.160.6");
        Config.radiusServerClientAddress = (String) Config.getProperty("radiusServerClientAddress", "192.168.160.5");
        Config.radiusSharedSecret = (String) Config.getProperty("radiusSharedSecret", "Safewalk1");
        Config.radiusDefaultTimeout = (String) Config.getProperty("radiusDefaultTimeout", "50000");


        // LDAP
        Config.ldapName = (String) Config.getProperty("ldapName", "LDAPTesting");
        Config.ldapType = (String) Config.getProperty("ldapType", "AD");
        Config.ldapDomain = (String) Config.getProperty("ldapDomain", "safewalk.training");
        Config.ldapRootBaseDn = (String) Config.getProperty("ldapRootDn", "DC=safewalk,DC=training");
        Config.ldapHost = (String) Config.getProperty("ldapHost", "192.168.140.247");
        Config.ldapBindDn = (String) Config.getProperty("ldapBindDn", "CN=administrator,CN=Users,DC=safewalk,DC=training");
        Config.ldapBindPassword = (String) Config.getProperty("ldapBindPassword", "Safewalk1");
        Config.ldapPort = Integer.parseInt((String) Config.getProperty("ldapPort", "389"));
        Config.ldapUserBaseDn = (String) Config.getProperty("ldapUserBaseDn", "DC=safewalk,DC=training");
        Config.ldapPriority = (String) Config.getProperty("ldapPriority", "1");
        Config.signData = (String) Config.getProperty("signData", "Data to sign");
        Config.signTitle = (String) Config.getProperty("signTitle", "Transactioon Titlte");
        Config.signBody = (String) Config.getProperty("signBody", "Body of the transaction");

        //Postman
        Config.postmanOcCollectionPath = (String) Config.getProperty("postmanOcCollectionPath", "");
        Config.postmanTenantCollectionPath = (String) Config.getProperty("postmanTenantCollectionPath", "");
        Config.postmanEnvironmentPath = (String) Config.getProperty("postmanEnvironmentPath", "");
        Config.allSuitesCount = Integer.parseInt((String) Config.getProperty("allSuitesCount", "6"));

        //System Backup
        Config.systemBackupHost = (String) Config.getProperty("systemBackupHost", "192.168.160.3");
        Config.systemBackupUsername = (String) Config.getProperty("systemBackupUsername", "simple");
        Config.systemBackupPassword = (String) Config.getProperty("systemBackupPassword", "Safewalk1");
        Config.systemBackupDestination = (String) Config.getProperty("systemBackupDestination", "ftp/simple");
    }

    public static String getAppiumUrl() {
        return Config.appiumSchema + "://" + Config.appiumHost + ":" + Integer.toString(Config.appiumPort) + Config.appiumPath;
    }

}

