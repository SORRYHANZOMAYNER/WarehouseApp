package com.example.demo1;
import com.example.demo1.module1.DTO.DetailDTO;
import com.example.demo1.storagePlaces.Conveyor;
import com.example.demo1.storagePlaces.Assembly;
import com.example.demo1.module1.DTO.ShelfDTO;
import com.example.demo1.module1.modules.Detail;
import com.example.demo1.module1.modules.Shelf;
import com.example.demo1.storagePlaces.Queue;
import com.example.demo1.storagePlaces.Storage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.PortUnreachableException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FmxController implements Initializable {
    @FXML
    public TextField name;
    @FXML
    public TextField quantity;
    @FXML
    public TextField customDetail;
    @FXML
    public TableView<Detail> table;
    @FXML
    public TableView<Detail> tableConveyor;
    @FXML
    public TableView<Detail> tableAssembly;
    @FXML
    public Label freePlace;
    public static boolean flag = true;
    public static boolean flagStorageOrAssembly = true;
    public static Queue queue = new Queue();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
       CreateNewColumns(table);
       CreateNewColumns(tableConveyor);
       CreateNewColumns(tableAssembly);
    }
    public void CreateCustomDetail(ActionEvent actionEvent) throws Exception {
        Assembly.createCustomDetail(customDetail.getText());
    }
    public void AddButton(ActionEvent actionEvent) {
        Detail detail = new Detail();
        detail.setDetailName(String.valueOf(name.getText()));
        detail.setQuantity(Integer.parseInt(quantity.getText()));
        queue.enQueue(detail);
        while(!queue.isEmpty()){
            if(flag==true){
                flag = false;
                Detail detail1 = queue.peak();
                queue.deQueue();
                System.out.println("Деталь начинает двигаться по конвейеру " + detail1.getDetailName());
                Conveyor c = new Conveyor(detail1);
                Thread tr = new Thread(c);
                tr.start();
            }
        }
    }

    public void AddShelfButton(ActionEvent actionEvent) {
        Requests.addNewShelfRequest();
    }
    public void PrintAllDetailsButton(ActionEvent actionEvent) {
        table.getItems().clear();
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        Response response1 = Requests.universalGetRequest(client);
        List<DetailDTO> deliver = response1.readEntity(new GenericType<List<DetailDTO>>() {});
        List<Detail> details = new ArrayList<>();
        for(DetailDTO detailDTO:deliver){
            if(detailDTO.getDetailName()!=null){
                details.add(Storage.DetailToEntity(detailDTO));
            }
        }
        table.getItems().addAll(details);
    }
    public static void CreateNewColumns(TableView tableView){
        TableColumn<Detail, String> nameColumn = new TableColumn<>("Названия");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("detailName"));
        tableView.getColumns().add(nameColumn);

        TableColumn<Detail, Integer> quantityColumn = new TableColumn<>("Количество");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableView.getColumns().add(quantityColumn);
    }

    public void PrintDetailOnConveyor(ActionEvent actionEvent) {
        tableConveyor.getItems().clear();
        tableConveyor.getItems().addAll(Conveyor.detailsOnConveyor);
    }

    public void PrintDetailOnAssembly(ActionEvent actionEvent) {
        tableAssembly.getItems().clear();
        tableAssembly.getItems().addAll(Assembly.detailsOnAssembly);
    }

    public void AddDetailToAssemblyButton(ActionEvent actionEvent) throws Exception {
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        String requirementDetail = customDetail.getText();
        switch (requirementDetail){
            case "вал":

                if(Requests.checkDetailForAssembly(client, "гайка", 1) && Requests.checkDetailForAssembly(client, "шестерня", 2)){
                    Requests.updateStorageAfterAssembly(client,"гайка", 1);
                    Requests.updateStorageAfterAssembly(client,"шестерня", 2);
                    queue.enQueue(new Detail("гайка",1));
                    queue.enQueue(new Detail("шестерня",2));
                    while(!queue.isEmpty()){
                        if(flag==true){
                            flag = false;
                            flagStorageOrAssembly = false;
                            Detail detail1 = queue.peak();
                            queue.deQueue();
                            System.out.println("Деталь начинает двигаться по конвейеру " + detail1.getDetailName());
                            Conveyor c = new Conveyor(detail1);
                            Thread tr = new Thread(c);
                            tr.start();
                        }
                    }
                }
                break;
        }
    }

    public void CheckFreePlaceButton(ActionEvent actionEvent) {
        //fr = new Label();
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        int places = Requests.checkFreePlaceRequest(client);
        freePlace.setText(String.valueOf(places));

    }
}
