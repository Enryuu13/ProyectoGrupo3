package grupo3programacionavanzada.views.empleados;

import java.util.List;
import grupo3programacionavanzada.data.Empleado;

public interface EmpleadosViewModel {
	void mostrarEmpleadosEnGrid(List<Empleado> items);
	void mostrarMensajeError(String mensaje);
	void mostrarMensajeExito(String mensaje);
}
