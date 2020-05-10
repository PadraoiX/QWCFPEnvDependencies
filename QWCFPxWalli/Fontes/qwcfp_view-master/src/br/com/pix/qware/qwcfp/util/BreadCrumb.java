package br.com.pix.qware.qwcfp.util;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.MenuModel;

//@ManagedBean	
@Named("breadCrumb")
@RequestScoped
public class BreadCrumb {
	
	private MenuModel menuModel = new DefaultMenuModel();
	
	public BreadCrumb(){

		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create index menuItem
		DefaultMenuItem index = new DefaultMenuItem();
		index.setValue("Index");
		index.setCommand("#{breadCrumb.navigateIndex}");

		// Add menuItems
		this.menuModel.addElement(index);
	}	
	
	public String navigateIndex(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create index menuItem
		DefaultMenuItem index = new DefaultMenuItem();
		index.setValue("Index");
		index.setCommand("#{breadCrumb.navigateIndex}");
		index.setUrl("main.faces");

		// Add menuItems
		this.menuModel.addElement(index);

		return "main.faces?faces-redirect=true"; 
	}
	
	public String navigateHome(){
		// Initialize
		this.menuModel = new DefaultMenuModel();

		// Create index menuItem
		DefaultMenuItem index = new DefaultMenuItem();
		index.setValue("Index");
		index.setCommand("#{breadCrumb.navigateIndex}");
		index.setUrl("main.faces");

		// Create home menuItem
		DefaultMenuItem home = new DefaultMenuItem();
		home.setValue("Home");
		home.setCommand("#{breadCrumb.navigateHome}");
		index.setUrl("main.faces");

		// Add menuItems
		this.menuModel.addElement(index);
		this.menuModel.addElement(home);

		return "main.faces?faces-redirect=true";
	}
	

	public MenuModel getMenuModel() {
		return menuModel;
	}

	public void setMenuModel(MenuModel menuModel) {
		this.menuModel = menuModel;
	}
	
}