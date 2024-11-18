package grupo3programacionavanzada.views.empleados;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import grupo3programacionavanzada.data.Empleado;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Empleados")
@Route("/:empleadoID?/:action?(edit)")
@Menu(order = 0, icon = "line-awesome/svg/columns-solid.svg")
@RouteAlias("")
public class EmpleadosView extends Div implements BeforeEnterObserver {

    private final String EMPLEADO_ID = "empleadoID";
    private final String EMPLEADO_EDIT_ROUTE_TEMPLATE = "/%s/edit";

    private final Grid<Empleado> grid = new Grid<>(Empleado.class, false);

    private TextField idEmpledo;
    private TextField nombre;
    private TextField departamento;
    private TextField cargo;
    private TextField salarioHora;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private final BeanValidationBinder<Empleado> binder;

    private Empleado empleado;


    public EmpleadosView() {
        addClassNames("empleados-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idEmpledo").setAutoWidth(true);
        grid.addColumn("nombre").setAutoWidth(true);
        grid.addColumn("departamento").setAutoWidth(true);
        grid.addColumn("cargo").setAutoWidth(true);
        grid.addColumn("salarioHora").setAutoWidth(true);
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(EMPLEADO_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(EmpleadosView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Empleado.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(idEmpledo).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("idEmpledo");
        binder.forField(salarioHora).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("salarioHora");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.empleado == null) {
                    this.empleado = new Empleado();
                }
                binder.writeBean(this.empleado);
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(EmpleadosView.class);
            } catch (ObjectOptimisticLockingFailureException exception) {
                Notification n = Notification.show(
                        "Error updating the data. Somebody else has updated the record while you were making changes.");
                n.setPosition(Position.MIDDLE);
                n.addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (ValidationException validationException) {
                Notification.show("Failed to update the data. Check again that all values are valid");
            }
        });
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        Optional<Long> empleadoId = event.getRouteParameters().get(EMPLEADO_ID).map(Long::parseLong);
       /* if (empleadoId.isPresent()) {
            Optional<Empleado> empleadoFromBackend = empleadoService.get(empleadoId.get());
            if (empleadoFromBackend.isPresent()) {
                populateForm(empleadoFromBackend.get());
            } else {
                Notification.show(String.format("The requested empleado was not found, ID = %s", empleadoId.get()),
                        3000, Notification.Position.BOTTOM_START);
                // when a row is selected but the data is no longer available,
                // refresh grid
                refreshGrid();
                event.forwardTo(EmpleadosView.class);
            }
        }*/
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        idEmpledo = new TextField("Id Empledo");
        nombre = new TextField("Nombre");
        departamento = new TextField("Departamento");
        cargo = new TextField("Cargo");
        salarioHora = new TextField("Salario Hora");
        formLayout.add(idEmpledo, nombre, departamento, cargo, salarioHora);

        editorDiv.add(formLayout);
        createButtonLayout(editorLayoutDiv);

        splitLayout.addToSecondary(editorLayoutDiv);
    }

    private void createButtonLayout(Div editorLayoutDiv) {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.setClassName("button-layout");
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save, cancel);
        editorLayoutDiv.add(buttonLayout);
    }

    private void createGridLayout(SplitLayout splitLayout) {
        Div wrapper = new Div();
        wrapper.setClassName("grid-wrapper");
        splitLayout.addToPrimary(wrapper);
        wrapper.add(grid);
    }

    private void refreshGrid() {
        grid.select(null);
        grid.getDataProvider().refreshAll();
    }

    private void clearForm() {
        populateForm(null);
    }

    private void populateForm(Empleado value) {
        this.empleado = value;
        binder.readBean(this.empleado);

    }
}
