package com.example.demo1.storagePlaces;

import com.example.demo1.FmxController;
import com.example.demo1.Requests;
import com.example.demo1.module1.DTO.*;
import com.example.demo1.module1.modules.*;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
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
        DetailDTO detailDTO = DetailToDto(detail);
        Response response1 = Requests.universalGetRequest(client);
        List<DetailDTO> deliver = response1.readEntity(new GenericType<List<DetailDTO>>() {});
            boolean checkName = CheckDetailName(deliver,detailDTO);
            System.out.println(checkName);
            if(!checkName){
                int coutFreePlaces = Requests.checkFreePlaceRequest(client);
                if(coutFreePlaces>0) {
                    long id = 0;
                    for (DetailDTO detailDTO1 : deliver) {
                        if (detailDTO1.getDetailName() != null) {
                            System.out.println(detailDTO1.getDetailId());
                            System.out.println(detailDTO1.getDetailName());
                        } else {
                            System.out.println("Пустое место с индексом " + detailDTO1.getDetailId());
                            id = detailDTO1.getDetailId();
                            break;
                        }
                    }
                    Requests.updateDetailByIdOrQuantityByName(client, "/details/", (int) id, detailDTO);
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
                Requests.updateDetailByIdOrQuantityByName(client,"/details/update/", newQuantity,DetailToDto(detailWithNewQuantity));
                FmxController.flag = true;

            }
        }
    public boolean CheckDetailName(List<DetailDTO> deliver ,DetailDTO detailDTO ){
        boolean checkName = false;
        for(DetailDTO detailDTO1: deliver){
            if(Objects.equals(detailDTO1.getDetailName(), detailDTO.getDetailName())){
                checkName = true;
                break;
            }
        }
        return checkName;
    }
    public static Detail DetailToEntity(DetailDTO detailDTO) {
        Detail detail = new Detail();
        detail.setDetailName(detailDTO.getDetailName());
        detail.setQuantity(detailDTO.getQuantity());
        detail.setDetailId(detailDTO.getDetailId());
        return detail;
    }
    public static DetailDTO DetailToDto(Detail detail) {
        DetailDTO detailDTO = new DetailDTO()
                .setDetailName(detail.getDetailName())
                .setQuantity(detail.getQuantity());
        detailDTO.setDetailId(detail.getDetailId());
        return detailDTO;
    }
    public Storage(){

    }
}
