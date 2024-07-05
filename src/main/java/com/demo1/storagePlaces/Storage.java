package com.demo1.storagePlaces;

import com.demo1.FmxController;
import com.demo1.Requests;
import com.demo1.module1.modules.Detail;
import com.demo1.module1.modules.*;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

public class Storage extends Thread {
    private Detail detail;
    public Detail getDetail() {
        return detail;
    }

    public void setDetail(Detail detail) {
        this.detail = detail;
    }

    public void receiveData(Detail comingDetail){
        this.detail = comingDetail;
        System.out.println("Пришла деталь: " + detail);
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        Response response1 = Requests.universalGetRequest(client);
        List<Detail> deliver = response1.readEntity(new GenericType<List<Detail>>() {});
            boolean checkName = CheckDetailName(deliver,detail);
            System.out.println(checkName);
            if(!checkName){
                int coutFreePlaces = Requests.checkFreePlaceRequest(client);
                if(coutFreePlaces>0) {
                    long id = 0;
                    for (Detail detail1 : deliver) {
                        if (detail1.getDetailName() != null) {
                            System.out.println(detail1.getDetailId());
                            System.out.println(detail1.getDetailName());
                        } else {
                            System.out.println("Пустое место с индексом " + detail1.getDetailId());
                            id = detail1.getDetailId();
                            break;
                        }
                    }
                    Requests.updateDetailByIdOrQuantityByName(client, "/details/", (int) id, detail);
                    FmxController.flag = true;
                }
                else{
                    System.out.println("На складе нет места");
                    FmxController.flag = true;
                }
            } else{
                String detailName = detail.getDetailName();
                System.out.println("Такая деталь уже есть на складе, увеличивается ее количество");
                Detail detailWithNewQuantity = Requests.getDetailByNameRequest(client,detailName);
                int newQuantity = detailWithNewQuantity.getQuantity() + detail.getQuantity();
                Requests.updateDetailByIdOrQuantityByName(client,"/details/update/", newQuantity,detailWithNewQuantity);
                FmxController.flag = true;

            }
        }
    public boolean CheckDetailName(List<Detail> deliver ,Detail detail ){
        boolean checkName = false;
        for(Detail detail1: deliver){
            if(Objects.equals(detail1.getDetailName(), detail.getDetailName())){
                checkName = true;
                break;
            }
        }
        return checkName;
    }
    public Storage(){

    }
}
