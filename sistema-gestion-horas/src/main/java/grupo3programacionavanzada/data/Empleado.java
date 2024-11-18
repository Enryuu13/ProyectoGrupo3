package grupo3programacionavanzada.data;




public class Empleado extends AbstractEntity {

    private Integer idEmpledo;
    private String nombre;
    private String departamento;
    private String cargo;
    private Integer salarioHora;

    public Integer getIdEmpledo() {
        return idEmpledo;
    }
    public void setIdEmpledo(Integer idEmpledo) {
        this.idEmpledo = idEmpledo;
    }
    public String getNombre() {
        return nombre;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public String getDepartamento() {
        return departamento;
    }
    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }
    public String getCargo() {
        return cargo;
    }
    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    public Integer getSalarioHora() {
        return salarioHora;
    }
    public void setSalarioHora(Integer salarioHora) {
        this.salarioHora = salarioHora;
    }

}
