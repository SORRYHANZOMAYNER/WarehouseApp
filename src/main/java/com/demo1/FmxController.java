package com.demo1;
import com.demo1.storagePlaces.Conveyor;
import com.demo1.storagePlaces.Queue;
import com.demo1.storagePlaces.Assembly;
import com.demo1.module1.modules.Detail;
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
       createNewColumns(table);
       createNewColumns(tableConveyor);
       createNewColumns(tableAssembly);
    }
    public void createCustomDetail(ActionEvent actionEvent) {
        Assembly.createCustomDetail(customDetail.getText());
    }
    public void addButton(ActionEvent actionEvent) {
        Detail detail = new Detail();
        detail.setDetailName(String.valueOf(name.getText()));
        detail.setQuantity(Integer.parseInt(quantity.getText()));
        queue.enQueue(detail);
        while(!queue.isEmpty()){
            if(flag){
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

    public void addShelfButton(ActionEvent actionEvent) {
        Requests.addNewShelfRequest();
    }
    public void printAllDetailsButton(ActionEvent actionEvent) {
        table.getItems().clear();
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        Response response1 = Requests.universalGetRequest(client);
        List<Detail> deliver = response1.readEntity(new GenericType<>() {});
        List<Detail> details = new ArrayList<>();
        for(Detail detail:deliver){
            if(detail.getDetailName()!=null){
                details.add(detail);
            }
        }
        table.getItems().addAll(details);
    }
    public static void createNewColumns(TableView tableView){
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
                        if(flag){
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
    public void checkFreePlaceButton(ActionEvent actionEvent) {
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        int places = Requests.checkFreePlaceRequest(client);
        freePlace.setText(String.valueOf(places));

    }
}
