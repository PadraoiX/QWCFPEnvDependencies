package br.com.pix.qware.qwcfp.teste;



import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import br.com.pix.qware.qwcfp.teste.Car;
import br.com.pix.qware.qwcfp.teste.CarService;
 
@Named("dtContextMenuView")
@ViewScoped
public class ContextMenuView implements Serializable {
    
    private List<Car> cars;
     
    private Car selectedCar;
     
    @ManagedProperty("org.primefaces.showcase.service.CarService@2521c1")
    private CarService service;
 
  
    public void init() {
        cars = service.createCars(10);
    }
     
    public List<Car> getCars() {
        return cars;
    }
 
    public Car getSelectedCar() {
        return selectedCar;
    }
 
    public void setSelectedCar(Car selectedCar) {
        this.selectedCar = selectedCar;
    }
 
    public void setService(CarService service) {
        this.service = service;
    }
     
    public void deleteCar() {
        cars.remove(selectedCar);
        selectedCar = null;
    }
}