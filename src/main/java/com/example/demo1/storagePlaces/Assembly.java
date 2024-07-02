package com.example.demo1.storagePlaces;

import com.example.demo1.FmxController;
import com.example.demo1.Requests;
import com.example.demo1.module1.exceptions.ZeroDetailException;
import com.example.demo1.storagePlaces.Conveyor;
import com.example.demo1.module1.modules.*;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJsonProvider;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;
import java.util.Timer;

public class Assembly {
    public static List<Detail> detailsOnAssembly = new ArrayList<>();
    public Assembly(){
    }
    // рецепт custom_detail_1 - detail_1 1, detail2 2
    public static void createCustomDetail(String order){
        switch (order){
            case "вал":
                createCustomDetail(order,1);
                detailsOnAssembly.clear();
        }
    }
    public static void createCustomDetail(String name, int quantity){
        Detail customDetail = new Detail();
        customDetail.setDetailName(name);
        customDetail.setQuantity(quantity);
        System.out.println("В зоне сборки создается сборочная деталь " + customDetail.getDetailName());
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Деталь с названием: " + customDetail.getDetailName() + "закончила создаваться и начинает двигаться по конвейеру");
                Conveyor c = new Conveyor(customDetail);
                Thread tr = new Thread(c);
                tr.start();
            }
        }, 10000);


    }
}
