package grupo3programacionavanzada.repository;

import grupo3programacionavanzada.data.EmpleadosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface DatabaseRepository {
	@Headers({
	    "Accept: application/vnd.github.v3.full+json",
	    "User-Agent: sistema-gestion-horas"
	})
	@GET("/pls/apex/201920110195/apphoteleria/empleados")
	Call<EmpleadosResponse> consultarempleados() ;
}
