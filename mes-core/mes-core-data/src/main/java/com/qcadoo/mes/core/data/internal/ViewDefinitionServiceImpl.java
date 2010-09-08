package com.qcadoo.mes.core.data.internal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qcadoo.mes.core.data.api.DataDefinitionService;
import com.qcadoo.mes.core.data.api.ViewDefinitionService;
import com.qcadoo.mes.core.data.definition.ColumnDefinition;
import com.qcadoo.mes.core.data.definition.DataDefinition;
import com.qcadoo.mes.core.data.definition.FieldDefinition;
import com.qcadoo.mes.core.data.definition.FormDefinition;
import com.qcadoo.mes.core.data.definition.GridDefinition;
import com.qcadoo.mes.core.data.definition.ViewDefinition;
import com.qcadoo.mes.core.data.definition.ViewElementDefinition;

@Service
public class ViewDefinitionServiceImpl implements ViewDefinitionService {

    @Autowired
    private DataDefinitionService dataDefinitionService;

    private Map<String, ViewDefinition> viewDefinitions;

    @PostConstruct
    public void initViews() {
        viewDefinitions = new HashMap<String, ViewDefinition>();
        viewDefinitions.put("products.productGridView", createProductGridView());
        viewDefinitions.put("products.productDetailsView", createProductDetailsView());
        viewDefinitions.put("products.substituteDetailsView", createProductSubstituteDetailsView());
        viewDefinitions.put("products.substituteComponentDetailsView", createProductSubstituteComponentDetailsView());
        viewDefinitions.put("users.groupGridView", createUserGroupGridView());
        viewDefinitions.put("users.groupDetailsView", createUserGroupDetailsView());
        viewDefinitions.put("users.userGridView", createUserGridView());
        viewDefinitions.put("users.userDetailsView", createUserDetailsView());
        viewDefinitions.put("orders.orderGridView", createOrderGridView());
        viewDefinitions.put("orders.orderDetailsView", createOrderDetailsView());
        viewDefinitions.put("core.dictionaryGridView", createDictionaryGridView());
        viewDefinitions.put("core.dictionaryDetailsView", createDictionaryDetailsView());
        viewDefinitions.put("core.dictionaryItemDetailsView", createDictionaryItemDetailsView());
    }

    @Override
    public ViewDefinition getViewDefinition(final String viewName) {
        return viewDefinitions.get(viewName);
    }

    @Override
    public List<ViewDefinition> getAllViews() {
        List<ViewDefinition> viewsList = new ArrayList<ViewDefinition>(viewDefinitions.values());
        Collections.sort(viewsList, new Comparator<ViewDefinition>() {

            @Override
            public int compare(final ViewDefinition v1, final ViewDefinition v2) {
                return v1.getName().compareTo(v2.getName());
            }
        });
        return viewsList;

    }

    private ViewDefinition createProductGridView() {
        ViewDefinition viewDefinition = new ViewDefinition("products.productGridView");
        viewDefinition.setHeader("Products:");

        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition gridDataDefinition = dataDefinitionService.get("products.product");
        GridDefinition gridDefinition = new GridDefinition("products", gridDataDefinition);
        gridDefinition.setCorrespondingViewName("products.productDetailsView");
        Map<String, String> gridOptions = new HashMap<String, String>();
        gridOptions.put("paging", "true");
        gridOptions.put("sortable", "true");
        gridOptions.put("filter", "true");
        gridOptions.put("multiselect", "true");
        gridOptions.put("height", "450");
        gridDefinition.setOptions(gridOptions);
        Map<String, String> gridEvents = new HashMap<String, String>();
        gridDefinition.setEvents(gridEvents);
        ColumnDefinition columnNumber = createColumnDefinition("number", gridDataDefinition.getField("number"), null);
        ColumnDefinition columnName = createColumnDefinition("name", gridDataDefinition.getField("name"), null);
        ColumnDefinition columnType = createColumnDefinition("typeOfMaterial", gridDataDefinition.getField("typeOfMaterial"),
                null);
        ColumnDefinition columnEan = createColumnDefinition("ean", gridDataDefinition.getField("ean"), null);

        gridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnNumber, columnName, columnType, columnEan }));
        elements.add(gridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createProductDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("products.productDetailsView");
        viewDefinition.setHeader("Product:");

        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition productDataDefinition = dataDefinitionService.get("products.product");
        FormDefinition form = new FormDefinition("productDetailsForm", productDataDefinition);
        form.setParent("entityId");
        form.setCorrespondingViewName("products.productGridView");
        elements.add(form);

        DataDefinition substituteDataDefinition = dataDefinitionService.get("products.substitute");
        GridDefinition substituteGridDefinition = new GridDefinition("substitutesGrid", substituteDataDefinition);
        substituteGridDefinition.setParent("viewElement:productDetailsForm");
        substituteGridDefinition.setParentField("product");
        substituteGridDefinition.setHeader("Substitutes:");
        ColumnDefinition columnNumber = createColumnDefinition("number", substituteDataDefinition.getField("number"), null);
        ColumnDefinition columnName = createColumnDefinition("name", substituteDataDefinition.getField("name"), null);
        ColumnDefinition columnPriority = createColumnDefinition("priority", substituteDataDefinition.getField("priority"), null);
        substituteGridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnNumber, columnName, columnPriority }));
        Map<String, String> substituteGridOptions = new HashMap<String, String>();
        substituteGridOptions.put("paging", "false");
        substituteGridOptions.put("sortable", "false");
        substituteGridOptions.put("filter", "false");
        substituteGridOptions.put("multiselect", "false");
        substituteGridOptions.put("height", "150");
        substituteGridDefinition.setOptions(substituteGridOptions);
        Map<String, String> substituteGridEvents = new HashMap<String, String>();
        substituteGridDefinition.setEvents(substituteGridEvents);
        substituteGridDefinition.setCorrespondingViewName("products.substituteDetailsView");
        // substituteGridDefinition.setCorrespondingViewModal(true);
        elements.add(substituteGridDefinition);

        DataDefinition substituteComponentDataDefinition = dataDefinitionService.get("products.substituteComponent");
        GridDefinition substituteComponentGridDefinition = new GridDefinition("substitutesComponentGrid",
                substituteComponentDataDefinition);
        substituteComponentGridDefinition.setParent("viewElement:substitutesGrid");
        substituteComponentGridDefinition.setParentField("substitute");
        substituteComponentGridDefinition.setHeader("Substitute products:");
        ColumnDefinition columnSubstituteNumber = createColumnDefinition("number",
                substituteComponentDataDefinition.getField("number"), "fields['product'].fields['number']");
        ColumnDefinition columnProductName = createColumnDefinition("name", substituteComponentDataDefinition.getField("name"),
                "fields['product'].fields['name']");
        ColumnDefinition columnQuantity = createColumnDefinition("quantity",
                substituteComponentDataDefinition.getField("quantity"), null);
        substituteComponentGridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnSubstituteNumber,
                columnProductName, columnQuantity }));
        Map<String, String> substituteComponentGridOptions = new HashMap<String, String>();
        substituteComponentGridOptions.put("paging", "false");
        substituteComponentGridOptions.put("sortable", "false");
        substituteComponentGridOptions.put("filter", "false");
        substituteComponentGridOptions.put("multiselect", "false");
        substituteComponentGridOptions.put("height", "150");
        substituteComponentGridDefinition.setOptions(substituteComponentGridOptions);
        substituteComponentGridDefinition.setCorrespondingViewName("products.substituteComponentDetailsView");
        elements.add(substituteComponentGridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createProductSubstituteDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("products.substituteDetailsView");
        viewDefinition.setHeader("Substitute:");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition substitutesDataDefinition = dataDefinitionService.get("products.substitute");
        FormDefinition form = new FormDefinition("substitutesDetailsForm", substitutesDataDefinition);
        form.setCorrespondingViewName("products.productDetailsView");
        form.setParent("entityId");
        form.setParentField("product");
        elements.add(form);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createProductSubstituteComponentDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("products.substituteComponentDetailsView");
        viewDefinition.setHeader("Substitute product:");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition substitutesComponentDataDefinition = dataDefinitionService.get("products.substituteComponent");
        FormDefinition form = new FormDefinition("substitutesComponentDetailsForm", substitutesComponentDataDefinition);
        form.setCorrespondingViewName("products.productDetailsView");
        form.setParent("entityId");
        form.setParentField("substitute");
        elements.add(form);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createUserGroupGridView() {
        ViewDefinition viewDefinition = new ViewDefinition("users.groupGridView");

        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition gridDataDefinition = dataDefinitionService.get("users.group");
        GridDefinition gridDefinition = new GridDefinition("groups", gridDataDefinition);
        gridDefinition.setCorrespondingViewName("users.groupDetailsView");
        Map<String, String> gridOptions = new HashMap<String, String>();
        gridOptions.put("paging", "true");
        gridOptions.put("sortable", "true");
        gridOptions.put("filter", "false");
        gridOptions.put("multiselect", "true");
        gridOptions.put("height", "450");
        gridDefinition.setOptions(gridOptions);
        ColumnDefinition columnName = createColumnDefinition("name", gridDataDefinition.getField("name"), null);
        ColumnDefinition columnRole = createColumnDefinition("role", gridDataDefinition.getField("role"), null);

        gridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnName, columnRole }));
        elements.add(gridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createUserGroupDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("users.groupDetailsView");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition groupDataDefinition = dataDefinitionService.get("users.group");
        FormDefinition form = new FormDefinition("groupDetailsForm", groupDataDefinition);
        form.setParent("entityId");
        form.setCorrespondingViewName("users.groupGridView");
        elements.add(form);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createUserGridView() {
        ViewDefinition viewDefinition = new ViewDefinition("users.userGridView");

        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition gridDataDefinition = dataDefinitionService.get("users.user");
        GridDefinition gridDefinition = new GridDefinition("users", gridDataDefinition);
        gridDefinition.setCorrespondingViewName("users.userDetailsView");
        Map<String, String> gridOptions = new HashMap<String, String>();
        gridOptions.put("paging", "true");
        gridOptions.put("sortable", "true");
        gridOptions.put("filter", "false");
        gridOptions.put("multiselect", "true");
        gridOptions.put("height", "450");
        gridDefinition.setOptions(gridOptions);
        ColumnDefinition columnLogin = createColumnDefinition("login", gridDataDefinition.getField("userName"), null);
        ColumnDefinition columnEmail = createColumnDefinition("email", gridDataDefinition.getField("email"), null);
        ColumnDefinition columnFirstName = createColumnDefinition("firstName", gridDataDefinition.getField("firstName"), null);
        ColumnDefinition columnLastName = createColumnDefinition("lastName", gridDataDefinition.getField("lastName"), null);
        ColumnDefinition columnUserGroup = createColumnDefinition("userGroup", gridDataDefinition.getField("userGroup"),
                "fields['userGroup'].fields['name']");

        gridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnLogin, columnEmail, columnFirstName,
                columnLastName, columnUserGroup }));
        elements.add(gridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createUserDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("users.userDetailsView");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition userDataDefinition = dataDefinitionService.get("users.user");
        FormDefinition form = new FormDefinition("userDetailsForm", userDataDefinition);
        form.setParent("entityId");
        form.setCorrespondingViewName("users.userGridView");
        elements.add(form);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createOrderGridView() {
        ViewDefinition viewDefinition = new ViewDefinition("orders.orderGridView");
        viewDefinition.setHeader("Orders:");

        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition gridDataDefinition = dataDefinitionService.get("orders.order");
        GridDefinition gridDefinition = new GridDefinition("orders", gridDataDefinition);
        gridDefinition.setCorrespondingViewName("orders.orderDetailsView");
        Map<String, String> gridOptions = new HashMap<String, String>();
        gridOptions.put("paging", "true");
        gridOptions.put("sortable", "true");
        gridOptions.put("filter", "true");
        gridOptions.put("multiselect", "true");
        gridOptions.put("height", "450");
        gridDefinition.setOptions(gridOptions);
        ColumnDefinition columnNumber = createColumnDefinition("number", gridDataDefinition.getField("number"), null);
        ColumnDefinition columnName = createColumnDefinition("name", gridDataDefinition.getField("name"), null);
        ColumnDefinition columnState = createColumnDefinition("state", gridDataDefinition.getField("state"), null);

        gridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnNumber, columnName, columnState }));
        elements.add(gridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createOrderDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("orders.orderDetailsView");
        viewDefinition.setHeader("Order:");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition orderDataDefinition = dataDefinitionService.get("orders.order");
        FormDefinition form = new FormDefinition("orderDetailsForm", orderDataDefinition);
        form.setParent("entityId");
        form.setCorrespondingViewName("orders.orderGridView");
        elements.add(form);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createDictionaryGridView() {
        ViewDefinition viewDefinition = new ViewDefinition("core.dictionaryGridView");
        viewDefinition.setHeader("Dictonaries:");

        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();
        DataDefinition gridDataDefinition = dataDefinitionService.get("core.dictionary");
        GridDefinition gridDefinition = new GridDefinition("distionaries", gridDataDefinition);
        gridDefinition.setCorrespondingViewName("core.dictionaryDetailsView");
        Map<String, String> gridOptions = new HashMap<String, String>();
        gridOptions.put("paging", "true");
        gridOptions.put("sortable", "true");
        gridOptions.put("filter", "true");
        gridOptions.put("multiselect", "false");
        gridOptions.put("height", "450");
        gridOptions.put("canDelete", "false");
        gridOptions.put("canNew", "false");
        gridDefinition.setOptions(gridOptions);
        ColumnDefinition columnName = createColumnDefinition("name", gridDataDefinition.getField("name"), null);

        gridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnName }));
        elements.add(gridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createDictionaryDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("core.dictionaryDetailsView");
        viewDefinition.setHeader("Dictonary:");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition gridDataDefinition = dataDefinitionService.get("core.dictionaryItem");
        GridDefinition gridDefinition = new GridDefinition("dictionaryItems", gridDataDefinition);
        gridDefinition.setParent("entityId");
        gridDefinition.setParentField("dictionary");
        gridDefinition.setCorrespondingViewName("core.dictionaryItemDetailsView");
        Map<String, String> gridOptions = new HashMap<String, String>();
        gridOptions.put("paging", "false");
        gridOptions.put("sortable", "false");
        gridOptions.put("filter", "false");
        gridOptions.put("multiselect", "true");
        gridOptions.put("height", "250");
        gridDefinition.setOptions(gridOptions);
        ColumnDefinition columnName = createColumnDefinition("name", gridDataDefinition.getField("name"), null);
        ColumnDefinition columnDescription = createColumnDefinition("description", gridDataDefinition.getField("description"),
                null);
        gridDefinition.setColumns(Arrays.asList(new ColumnDefinition[] { columnName, columnDescription }));
        elements.add(gridDefinition);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ViewDefinition createDictionaryItemDetailsView() {
        ViewDefinition viewDefinition = new ViewDefinition("core.dictionaryItemDetailsView");
        viewDefinition.setHeader("Dictionary item:");
        List<ViewElementDefinition> elements = new LinkedList<ViewElementDefinition>();

        DataDefinition orderDataDefinition = dataDefinitionService.get("core.dictionaryItem");
        FormDefinition form = new FormDefinition("dictionaryItemDetailsForm", orderDataDefinition);
        form.setParent("entityId");
        form.setParentField("dictionary");
        form.setCorrespondingViewName("core.dictionaryDetailsView");
        elements.add(form);

        viewDefinition.setElements(elements);
        return viewDefinition;
    }

    private ColumnDefinition createColumnDefinition(final String name, final FieldDefinition field, final String expression) {
        ColumnDefinition columnDefinition = new ColumnDefinition(name);
        columnDefinition.setFields(Arrays.asList(new FieldDefinition[] { field }));
        columnDefinition.setExpression(expression);
        return columnDefinition;
    }

}
