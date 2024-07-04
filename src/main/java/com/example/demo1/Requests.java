package com.example.demo1;

import com.example.demo1.module1.modules.Detail;
import com.example.demo1.module1.modules.Shelf;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Objects;

public class Requests {
    private static final String SERVER_URL = "http://localhost:8000/api1/v1";
    public static void updateStorageAfterAssembly(Client client,String detailName,int detailCount){
        Detail detail = Requests.getDetailByNameRequest(client,detailName);
        int newQuantity = detail.getQuantity() - detailCount;
        if(newQuantity == 0){Requests.deleteDetailByNameRequest(client,detail.getDetailName());}
        else{Requests.updateDetailRequest(client,detail,newQuantity);}

    }
    public static boolean checkDetailForAssembly(Client client,String detailName,int detailCount) throws Exception {
        try{
            Detail detail = Requests.getDetailByNameRequest(client,detailName);
            if(Objects.equals(detail.getDetailName(), detailName) && detail.getQuantity() >= detailCount){
                System.out.println("Деталь на складе " + detail.getDetailName() + " в количестве " + detail.getQuantity());
                return true;
            }
            else{
                System.out.println("Такой детали нет на складе: " + detailName);
                return false;
            }
        }
        catch (Exception e){
            throw new Exception("Такой детали нет на складе: " + detailName);
        }

    }
    public static Detail getDetailByNameRequest(Client client,String name){
        Response response = client.target(SERVER_URL + "/details/" + name)
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println(response);
        Detail createdDeliver = response.readEntity(Detail.class);
        System.out.println(createdDeliver);
        return createdDeliver;
    }
    public static void deleteDetailByNameRequest(Client client,String name){
        Detail detail = new Detail();
        Response response = client.target(SERVER_URL + "/details/null/" + name)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(detail, MediaType.APPLICATION_JSON));
        System.out.println(response);
        if (response.getStatus() == 200) {
            System.out.println("Деталь с именем " + name + " успешно удалена.");
        } else {
            System.out.println("Ошибка удаления детали. Код ответа: " + response.getStatus());
        }
    }
    public static void updateDetailRequest(Client client,Detail detail,int quantity){
        Detail updateDetail = new Detail();
        updateDetail.setDetailName(detail.getDetailName());
        updateDetail.setQuantity(quantity);
        Response response2 = client.target(SERVER_URL + "/details/" + detail.getDetailId())
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(updateDetail, MediaType.APPLICATION_JSON));
        System.out.println(response2);
    }
    public static Response universalGetRequest(Client client){
        Response response = client.target(SERVER_URL + "/details")
                .request(MediaType.APPLICATION_JSON)
                .get();
        System.out.println(response);
        return response;
    }
    public static void addNewShelfRequest(){
        Shelf s1 = new Shelf();
        Client client = ClientBuilder.newClient();
        client.register(JacksonJsonProvider.class);
        Response response = client.target(SERVER_URL + "/shelf")
                .request(MediaType.APPLICATION_JSON)
                .post(Entity.entity(s1, MediaType.APPLICATION_JSON));
        System.out.println(response);
    }
    public static int checkFreePlaceRequest(Client client){
        Response response1 = Requests.universalGetRequest(client);
        List<Detail> deliver = response1.readEntity(new GenericType<>() {
        });
        int freePlace = 0;
        for(Detail detailDTO1:deliver){
            if(detailDTO1.getDetailName() == null){
                freePlace +=1;
            }
        }
        System.out.println("На складе " + freePlace + " свободных мест");
        return freePlace;
    }
    public static void updateDetailByIdOrQuantityByName(Client client, String url, int cout, Detail detail){
        Response response = client.target(SERVER_URL + url + cout)
                .request(MediaType.APPLICATION_JSON)
                .put(Entity.entity(detail, MediaType.APPLICATION_JSON));
        System.out.println(response);
    }
}
