package grupo3programacionavanzada.data;


import java.time.LocalDate;


public class Horas extends AbstractEntity {

    private Integer idRegistro;
    private Integer idEmpleado;
    private LocalDate fecha;
    private Integer horasTrabajadas;
    private String tipoTurno;

    public Integer getIdRegistro() {
        return idRegistro;
    }
    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }
    public Integer getIdEmpleado() {
        return idEmpleado;
    }
    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }
    public LocalDate getFecha() {
        return fecha;
    }
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public Integer getHorasTrabajadas() {
        return horasTrabajadas;
    }
    public void setHorasTrabajadas(Integer horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }
    public String getTipoTurno() {
        return tipoTurno;
    }
    public void setTipoTurno(String tipoTurno) {
        this.tipoTurno = tipoTurno;
    }

}
