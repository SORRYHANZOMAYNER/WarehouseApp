package com.demo1.storagePlaces;

import com.demo1.Requests;
import com.demo1.module1.modules.Detail;
import javax.ws.rs.client.Client;
import java.util.*;
import java.util.Timer;

public class Assembly {
    public static List<Detail> detailsOnAssembly = new ArrayList<>();

    public Assembly() {
    }

    public static void createCustomDetail(String order) {
        createCustomDetail(order, 1);
        detailsOnAssembly.clear();
    }

    public static void createCustomDetail(String name, int quantity) {
        Client client = Requests.initClient();
        Detail customDetail = new Detail(name, quantity);
        System.out.println("В зоне сборки создается сборочная деталь " + customDetail.getDetailName());
        int countFreePlaces = Requests.checkFreePlaceRequest(client);
        if (countFreePlaces > 0) {
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.out.println("Деталь с названием: " + customDetail.getDetailName() + " закончила создаваться и начинает двигаться по конвейеру");
                    Conveyor conveyor = new Conveyor(customDetail);
                    Thread thread = new Thread(conveyor);
                    thread.start();
                }
            }, 10000);
        } else {
            System.out.println("Деталь создалась,но на складе нет места");
        }
    }
}
