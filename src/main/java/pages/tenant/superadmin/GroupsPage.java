package pages.tenant.superadmin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import configs.Config;
import pages.BasePage;

public class GroupsPage extends BasePage {
	By addGroupLink;
	By groupNameInput;
	By priorityInput;
	By saveButton;

	
	public GroupsPage(WebDriver driver, WebDriverWait wait) {
		super(driver, wait);
	
		this.addGroupLink            		= By.linkText("Add group");
		this.groupNameInput                 = By.id("id_name");	
		this.priorityInput                 	= By.id("id_groupsettings-0-priority");	
		this.saveButton                 	= By.name("_save");	
		}
	
	public WebElement getAddGroupLink() {
		return this.driver.findElement(this.addGroupLink);
	}

	public WebElement getGroupNameInput() {
		return this.driver.findElement(this.groupNameInput);
	}
	
	public WebElement getPriorityInput() {
		return this.driver.findElement(this.priorityInput);
	}
	
	public WebElement getSaveButton() {
		return this.driver.findElement(this.saveButton);
	}
	
	
	

	@Override
	public String getUrl() {
		return "https://" + Config.tnHost + ":" + Config.tnSuperAdminPort.toString() + "/core_tools/about/";
	}
}