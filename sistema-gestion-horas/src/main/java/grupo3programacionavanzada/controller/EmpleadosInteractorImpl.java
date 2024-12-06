package grupo3programacionavanzada.controller;

import grupo3programacionavanzada.data.EmpleadosResponse;
import grupo3programacionavanzada.repository.DatabaseRepositoryImpl;
import grupo3programacionavanzada.views.empleados.EmpleadosViewModel;

public class EmpleadosInteractorImpl implements EmpleadosInteractor {
	
	private DatabaseRepositoryImpl modelo;
	private EmpleadosViewModel vista;
	
	public EmpleadosInteractorImpl(EmpleadosViewModel vista) {
		
		super();
		this.vista = vista;
		this.modelo = DatabaseRepositoryImpl.getInstance("https://apex.oracle.com", 3000L);
	}
	
	@Override
	public void consultarEmpleados() {
	try {
		EmpleadosResponse respuesta = this.modelo.consultarEmpleados();
		if(respuesta == null || respuesta.getCount() == 0 || respuesta.getItems() == null) {
		}else {
			this.vista.mostrarMensajeError("No hay empleados disponibles");
		}
	}catch (Exception error) {
			EmpleadosResponse respuesta = null;
			this.vista.mostrarEmpleadosEnGrid(respuesta.getItems());
			}
		
	}
}
