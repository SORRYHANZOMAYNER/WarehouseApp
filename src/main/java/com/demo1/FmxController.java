package com.demo1;
import com.demo1.module1.modules.Shelf;
import com.demo1.storagePlaces.Conveyor;
import com.demo1.storagePlaces.Queue;
import com.demo1.storagePlaces.Assembly;
import com.demo1.module1.modules.Detail;
import com.demo1.storagePlaces.Storage;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class FmxController implements Initializable {
    @FXML
    public TextField nameAndQuantityTextField;
    @FXML
    public TextField customDetail;
    @FXML
    public TextField shelfIdTextField;
    @FXML
    public TableView<Detail> table;
    @FXML
    public TableView<Detail> tableConveyor;
    @FXML
    public TableView<Detail> tableAssembly;
    public static boolean flag = true;
    public static boolean flagStorageOrAssembly = true;
    public static Queue queue = new Queue();
    @FXML
    public Label freePlaceLabel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createNewColumns(table);
        createNewColumns(tableConveyor);
        createNewColumns(tableAssembly);
    }
    public void createCustomDetail(ActionEvent actionEvent) {
        Assembly.createCustomDetail(customDetail.getText());
    }
    public void addButton(ActionEvent actionEvent) {
        Client client = Requests.initClient();
        Response response1 = Requests.universalGetRequest(client);
        List<Detail> deliver = response1.readEntity(new GenericType<>() {
        });
        String[] nameAndQuantity = nameAndQuantityTextField.getText().split(" ");
        Detail detail = new Detail(nameAndQuantity[0],Integer.parseInt(nameAndQuantity[1]));
        boolean checkName = Storage.checkDetailName(deliver, detail);
        if (!checkName) {
            int countFreePlaces = Requests.checkFreePlaceRequest(client);
            if (countFreePlaces > 0) {
                queue.enQueue(detail);
            } else {
                System.out.println("На складе нет места");
            }
        } else {
            String detailName = detail.getDetailName();
            System.out.println("Такая деталь уже есть на складе, увеличивается ее количество " + detailName);
            queue.enQueue(detail);

        }
        while(!queue.isEmpty()){
            if(flag){
                flag = false;
                Detail detail1 = queue.peak();
                queue.deQueue();
                System.out.println("Деталь начинает двигаться по конвейеру " + detail1.getDetailName());
                Conveyor conveyor = new Conveyor(detail1);
                Thread thread = new Thread(conveyor);
                thread.start();
            }
        }
    }

    public void addShelfButton(ActionEvent actionEvent) {
        Requests.addNewShelfRequest();
        System.out.println("Создалась полка с 4-мя местами");
    }
    public void printAllDetailsButton(ActionEvent actionEvent) {
       printAllDetails();
    }
    public void createNewColumns(TableView<Detail> tableView) {
        TableColumn<Detail, String> nameColumn = new TableColumn<>("Названия");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("detailName"));
        tableView.getColumns().add(nameColumn);

        TableColumn<Detail, Integer> quantityColumn = new TableColumn<>("Количество");
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        tableView.getColumns().add(quantityColumn);
    }

    public void printDetailOnConveyor(ActionEvent actionEvent) {
        tableConveyor.getItems().clear();
        tableConveyor.getItems().addAll(Conveyor.detailsOnConveyor);
    }

    public void printDetailOnAssembly(ActionEvent actionEvent) {
        tableAssembly.getItems().clear();
        tableAssembly.getItems().addAll(Assembly.detailsOnAssembly);
    }

    public void addDetailToAssemblyButton(ActionEvent actionEvent) throws Exception {
        Client client = Requests.initClient();
        String requirementDetail = customDetail.getText();
        switch (requirementDetail) {
            case "вал":
                if (Requests.checkDetailForAssembly(client, "гайка", 1) && Requests.checkDetailForAssembly(client, "шестерня", 2)) {
                    Requests.updateStorageAfterAssembly(client, "гайка", 1);
                    Requests.updateStorageAfterAssembly(client, "шестерня", 2);
                    addQueue(new Detail("шестерня", 2), new Detail("гайка", 1));
                }
                break;
            case "колесо":
                if (Requests.checkDetailForAssembly(client, "спица", 8) && Requests.checkDetailForAssembly(client, "круг", 1)) {
                    Requests.updateStorageAfterAssembly(client, "спица", 8);
                    Requests.updateStorageAfterAssembly(client, "круг", 1);
                    addQueue(new Detail("спица", 8), new Detail("круг", 1));
                }
            default:
                System.out.println("Такой детали нет");
                break;
        }
    }
    public void addQueue(Detail ... details){
        for(Detail detail: details){
            queue.enQueue(detail);
        }
        while(!queue.isEmpty()){
            if(flag){
                flag = false;
                flagStorageOrAssembly = false;
                Detail detail1 = queue.peak();
                queue.deQueue();
                System.out.println("Деталь начинает двигаться по конвейеру " + detail1.getDetailName());
                Conveyor conveyor = new Conveyor(detail1);
                Thread thread = new Thread(conveyor);
                thread.start();
            }
        }
    }
    public void checkFreePlaceButton(ActionEvent actionEvent) {
        Client client = Requests.initClient();
        int places = Requests.checkFreePlaceRequest(client);
        freePlaceLabel.setText("На складе осталось свободных мест - " + String.valueOf(places));

    }
    public void printAllDetails() {
        table.getItems().clear();
        Client client = Requests.initClient();
        Response response1 = Requests.universalGetRequest(client);
        List<Detail> deliver = response1.readEntity(new GenericType<>() {
        });
        List<Detail> details = new ArrayList<>();
        for (Detail detail : deliver) {
            if (detail.getDetailName() != null) {
                details.add(detail);
            }
        }
        table.getItems().addAll(details);
    }
}
