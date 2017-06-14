package com.example.josedaniel.medidorvelocidad.dto;

/**
 * Created by calderjo on 13/06/2017.
 */

public class medidorVelocidadDTO {
    public   String dispositivo;
    public   String macdispositivo;
    public  int imgdispositivo;


    public medidorVelocidadDTO(String dispositivo, String macdispositivo, int imgdispositivo) {
        this.dispositivo = dispositivo;
        this.macdispositivo = macdispositivo;
        this.imgdispositivo = imgdispositivo;
    }
}

