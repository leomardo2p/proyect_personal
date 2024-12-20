/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.mavenproject1;

/**
 *
 * @author User
 */
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class MyTelegramBot extends TelegramLongPollingBot {

    private String botUsername;
    private String botToken;
    private boolean esperando;
    private boolean partici;
    private int cant;
    private ArrayList<String> list;
    private int cont;

    public MyTelegramBot(String botUsername, String botToken) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        esperando=true;
        partici=true;
        cant=0;
        list=new ArrayList<>();
        cont=0;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        String chid = update.getMessage().getChatId().toString();
        if(update.getMessage().hasText() && update.hasMessage()){
            //System.out.println(update.getMessage().getText());
            if(update.getMessage().getText().equalsIgnoreCase("/stop")){
                esperando=true;
                partici=true;
                cant=0;
                cont=0;
                list.clear();
                sendMessage(chid,"Se ha detenido todo");
            }
            if(update.getMessage().getText().equalsIgnoreCase("/torneo") && esperando){
                sendMessage(chid,"Ok vamos a hacer el torneo, escribe la cantidad de personas a participar");
                sendMessage(chid,"Si desea cancelar esto por favor solo teclee /stop y detendra todo");
                esperando=false;
            }
            
            if(!partici && cont!=cant){
                list.add(update.getMessage().getText());
                //System.out.println(list.get(cont));
                cont++;
                if(cont==cant){
                    sendMessage(chid,"En un segundo tendran las parejas al azar");
                    parejas(list,chid);
                    esperando=true;
                    partici=true;
                    cant=0;
                    cont=0;
                    list.clear();
                }
            }

            if(!esperando){
                cant=Integer.parseInt(update.getMessage().getText());
                if(cant%2!=0){
                    sendMessage(chid,"El numero introducido debe ser par");
                } else{
                    sendMessage(chid,"Introduce los nombres uno por uno");
                    partici=false;
                }
            }

          
            
        }
        
    }
    
    public void parejas(ArrayList<String> par,String chid){
        Random r=new Random();
        int cont=1;
        while(!par.isEmpty()){
            int i=r.nextInt(0,par.size());
            String uno=par.get(i);
            par.remove(i);
            i=r.nextInt(0,par.size());
            String dos=par.get(i);
            par.remove(i);
            sendMessage(chid,cont+"-"+uno+ " y " + dos);
            cont++;
        }
        
    }
    
    private void sendMessage(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(chatId);
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String botUsername = "probandojavaytelegram";
        String botToken = "7917673334:AAHtdtT6zMswGdMOuxkPPUop8TAl4gKCIS0";
        MyTelegramBot bot = new MyTelegramBot(botUsername, botToken);
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        
        
    }
}


