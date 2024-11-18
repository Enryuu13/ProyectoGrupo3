package grupo3programacionavanzada.views.horastrabajadas;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
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
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import grupo3programacionavanzada.data.Horas;
import java.util.Optional;
import org.springframework.data.domain.PageRequest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

@PageTitle("Horas Trabajadas")
@Route("horas-trabajadas/:horasID?/:action?(edit)")
@Menu(order = 1, icon = "line-awesome/svg/columns-solid.svg")
public class HorasTrabajadasView extends Div implements BeforeEnterObserver {

    private final String HORAS_ID = "horasID";
    private final String HORAS_EDIT_ROUTE_TEMPLATE = "horas-trabajadas/%s/edit";

    private final Grid<Horas> grid = new Grid<>(Horas.class, false);

    private TextField idRegistro;
    private TextField idEmpleado;
    private DatePicker fecha;
    private TextField horasTrabajadas;
    private TextField tipoTurno;

    private final Button cancel = new Button("Cancelar");
    private final Button save = new Button("Guardar");

    private final BeanValidationBinder<Horas> binder;

    private Horas horas;

 

    public HorasTrabajadasView() {
    
        addClassNames("horas-trabajadas-view");

        // Create UI
        SplitLayout splitLayout = new SplitLayout();

        createGridLayout(splitLayout);
        createEditorLayout(splitLayout);

        add(splitLayout);

        // Configure Grid
        grid.addColumn("idRegistro").setAutoWidth(true);
        grid.addColumn("idEmpleado").setAutoWidth(true);
        grid.addColumn("fecha").setAutoWidth(true);
        grid.addColumn("horasTrabajadas").setAutoWidth(true);
        grid.addColumn("tipoTurno").setAutoWidth(true);
        
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);

        // when a row is selected or deselected, populate form
        grid.asSingleSelect().addValueChangeListener(event -> {
            if (event.getValue() != null) {
                UI.getCurrent().navigate(String.format(HORAS_EDIT_ROUTE_TEMPLATE, event.getValue().getId()));
            } else {
                clearForm();
                UI.getCurrent().navigate(HorasTrabajadasView.class);
            }
        });

        // Configure Form
        binder = new BeanValidationBinder<>(Horas.class);

        // Bind fields. This is where you'd define e.g. validation rules
        binder.forField(idRegistro).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("idRegistro");
        binder.forField(idEmpleado).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("idEmpleado");
        binder.forField(horasTrabajadas).withConverter(new StringToIntegerConverter("Only numbers are allowed"))
                .bind("horasTrabajadas");

        binder.bindInstanceFields(this);

        cancel.addClickListener(e -> {
            clearForm();
            refreshGrid();
        });

        save.addClickListener(e -> {
            try {
                if (this.horas == null) {
                    this.horas = new Horas();
                }
                binder.writeBean(this.horas);
              
                clearForm();
                refreshGrid();
                Notification.show("Data updated");
                UI.getCurrent().navigate(HorasTrabajadasView.class);
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
        Optional<Long> horasId = event.getRouteParameters().get(HORAS_ID).map(Long::parseLong);
		/*
		 * if (horasId.isPresent()) { Optional<Horas> horasFromBackend =
		 * horasService.get(horasId.get()); if (horasFromBackend.isPresent()) {
		 * populateForm(horasFromBackend.get()); } else {
		 * Notification.show(String.format("The requested horas was not found, ID = %s",
		 * horasId.get()), 3000, Notification.Position.BOTTOM_START); // when a row is
		 * selected but the data is no longer available, // refresh grid refreshGrid();
		 * event.forwardTo(HorasTrabajadasView.class); } }
		 */
    }

    private void createEditorLayout(SplitLayout splitLayout) {
        Div editorLayoutDiv = new Div();
        editorLayoutDiv.setClassName("editor-layout");

        Div editorDiv = new Div();
        editorDiv.setClassName("editor");
        editorLayoutDiv.add(editorDiv);

        FormLayout formLayout = new FormLayout();
        idRegistro = new TextField("Id Registro");
        idEmpleado = new TextField("Id Empleado");
        fecha = new DatePicker("Fecha");
        horasTrabajadas = new TextField("Horas Trabajadas");
        tipoTurno = new TextField("Tipo Turno");
        formLayout.add(idRegistro, idEmpleado, fecha, horasTrabajadas, tipoTurno);

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

    private void populateForm(Horas value) {
        this.horas = value;
        binder.readBean(this.horas);

    }
}
