package com.anuj.snackmenu.views;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.data.validator.IntegerRangeValidator;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.stream.Collectors;

@Route("")
public class MainView extends VerticalLayout {
    private Grid<SnackOrder>grid = new Grid<>(SnackOrder.class);
    private List<SnackOrder>snackOrderList= new LinkedList<>();
    public MainView(){
        add(
                new H1("Snack Order"),
                buildForm(),
                grid
        );
    }

    private Component buildForm() {
        Map<String, List<String>>snacks = new HashMap<>();
        snacks.put("Fruits", Arrays.asList("Banana","Apple","Orange","Avocado"));
        snacks.put("Candy",Arrays.asList("Chocolate Bar","Gummy Bears","Granola Bears"));
        snacks.put("Drinks", Arrays.asList("Soda","Water","Coffee","Tea"));

        TextField name = new TextField("Name");
        TextField quantity = new TextField("Quantity");
        ComboBox<String> typeSelect = new ComboBox<>("Type", snacks.keySet());
        ComboBox<String> snackSelect = new ComboBox<>("Snack", Collections.emptyList());
        Button orderButton = new Button("Order");
        orderButton.setEnabled(false);

        snackSelect.setEnabled(false);
        typeSelect.addValueChangeListener(e->{
            String type = e.getValue();
            Boolean enabled = type!= null && !type.isEmpty();
            snackSelect.setEnabled(enabled);
            if (enabled){
                snackSelect.setValue("s");
                snackSelect.setItems(snacks.get(type));
            }
        });

        // to bind the values from the fields to a snackOrder Object
        Binder<SnackOrder>snackOrderBinder = new Binder<>(SnackOrder.class);
        snackOrderBinder.forField(name)
                .asRequired("Name is required")
                .bind("name");
        snackOrderBinder.forField(quantity)
                .asRequired()
                .withConverter(new StringToIntegerConverter("Quantity needs to be an integer"))
                .withValidator(new IntegerRangeValidator("Quantity needs to be at least 1",1,100))
                .bind("quantity");
        snackOrderBinder.forField(snackSelect)
                .asRequired("Please select a snack")
                .bind("snack");


        snackOrderBinder.addStatusChangeListener(status->
        {
            orderButton.setEnabled(!status.hasValidationErrors());
            // order button is not clickable until other fields are
            // not validated
        });
        snackOrderBinder.readBean(new SnackOrder());

        orderButton.addClickListener(click->{
            SnackOrder savedOrder = new SnackOrder();
            try {
                snackOrderBinder.writeBean(savedOrder);
                addOrder(savedOrder);
                snackOrderBinder.readBean(new SnackOrder());
                // clearing out the fields
                typeSelect.setValue("");
            } catch (ValidationException e) {
                e.printStackTrace();
            }
        });




        Div errorLayout = new Div();
        orderButton.addThemeName("primary");
        HorizontalLayout formLayout = new HorizontalLayout();
        formLayout.add(
                name,
                quantity,
                typeSelect,
                snackSelect,
                orderButton
        );
        Div wrapperLayout = new Div(formLayout, errorLayout);// stacking formlayout on top of errorlayout
        formLayout.setDefaultVerticalComponentAlignment(Alignment.BASELINE);
        wrapperLayout.setWidth("100%");

        return wrapperLayout;
    }

    private void addOrder(SnackOrder savedOrder) {
        snackOrderList.add(savedOrder);
        grid.setItems(snackOrderList);
    }
}