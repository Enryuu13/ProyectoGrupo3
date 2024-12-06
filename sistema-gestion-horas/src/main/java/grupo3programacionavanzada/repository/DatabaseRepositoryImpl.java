package grupo3programacionavanzada.repository;
import java.io.IOException;

import grupo3programacionavanzada.data.EmpleadosResponse;
import retrofit2.Call;
import retrofit2.Response;


public class DatabaseRepositoryImpl {
	private static DatabaseRepositoryImpl INSTANCE;
	private DatabaseClient client;
	
	//PATRON SINGLETON
	
	private DatabaseRepositoryImpl(String url, Long timeout) {
		
		client = new DatabaseClient (url, timeout);
	}
	
public static DatabaseRepositoryImpl getInstance(String url, Long timeout) {
		
		if (INSTANCE == null) {
			synchronized(DatabaseRepositoryImpl.class) {
				if (INSTANCE == null) {
						INSTANCE = new DatabaseRepositoryImpl(url, timeout);
				}
				
			}
		}
		return INSTANCE;
	}
public EmpleadosResponse consultarEmpleados() throws IOException {
	Call<EmpleadosResponse> call = client.getDatabase().consultarempleados();
	Response<EmpleadosResponse> response = call.execute();
	if (response.isSuccessful()){
		return response.body();
	}else {
	return null;}
}

}
