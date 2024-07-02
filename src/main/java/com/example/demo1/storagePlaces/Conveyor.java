package com.example.demo1.storagePlaces;

import com.example.demo1.FmxController;
import com.example.demo1.module1.exceptions.ZeroDetailException;
import com.example.demo1.module1.modules.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Conveyor extends Thread {
    private Detail detail;
    public Detail getDetail() {
        return detail;
    }
    public void setDetail(Detail detail) {this.detail = detail;}
    public Conveyor(Detail detail){
        this.detail = detail;
    }
    public static List<Detail> detailsOnConveyor = new ArrayList<>();
    public void transferToStorage() throws ZeroDetailException {
        Thread thread = new Thread(() -> {
            Storage storage = new Storage();
            detailsOnConveyor.remove(detail);
            storage.receiveData(detail);
        });
        if(this.getDetail().getQuantity()>0){ thread.start();}
        else{throw new ZeroDetailException();}
    }
    public void assemblyTransfer() throws ZeroDetailException {
        Thread thread = new Thread(() -> {
            Assembly.detailsOnAssembly.add(detail);
            detailsOnConveyor.remove(detail);
            FmxController.flagStorageOrAssembly = true;
            FmxController.flag = true;
        });
        if(this.getDetail().getQuantity()>0){ thread.start();}
        else{throw new ZeroDetailException();}
    }
    public void run(){
        Timer timer = new Timer();
        detailsOnConveyor.add(detail);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("Деталь закончила двигаться по конвейеру");
                try {
                    if(FmxController.flagStorageOrAssembly){
                        transferToStorage();
                    }
                    else{
                        assemblyTransfer();
                    }
                } catch (ZeroDetailException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 10000);
    }

}
