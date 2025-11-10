package co.edu.uptc.view;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.swing.JLabel;

public class Texto extends JLabel{
    
    private String texto;
    private TipoTexto tipo;
    private ColorConstante color;
    private Font fuente;
    
    public Texto(String texto, TipoTexto tipo, String color) {
        this.texto = texto;
        this.tipo = tipo;
        this.setText(texto);
        this.color = new ColorConstante();
        style();
        color(color);
    }
    
    private void color(String color) {
        if(color.equals("VERDE")){
            this.setForeground(this.color.getVerdeOscuro());
        } else if(color.equals("BLANCO")){
            this.setForeground(this.color.getBlanco());
        } 
    }

    private void style(){
        this.setHorizontalAlignment(JLabel.CENTER);
        
        switch(tipo){
            case TITULO:
                try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Source_Serif_4/SourceSerif4-VariableFont_opsz,wght.ttf")) {
                    if (resourceStream == null) {
                        throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Source_Serif_4/SourceSerif4-VariableFont_opsz,wght.ttf");
                    }
                    try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                        Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
                        this.fuente = myFont.deriveFont(Font.BOLD, 40);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.fuente = new Font(Font.SERIF, Font.BOLD, 40);
                }
                this.setFont(this.fuente);
                break;
            case SUBTITULO:
                try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Source_Serif_4/SourceSerif4-VariableFont_opsz,wght.ttf")) {
                    if (resourceStream == null) {
                        throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Source_Serif_4/SourceSerif4-VariableFont_opsz,wght.ttf");
                    }
                    try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                        Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
                        this.fuente = myFont.deriveFont(Font.BOLD, 35f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.fuente = new Font(Font.SERIF, Font.BOLD, 35);
                }
                this.setFont(this.fuente);
                break;
            case INSTRUCCION:
                try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Cambay/Cambay-Regular.ttf")) {
                    if (resourceStream == null) {
                        throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Cambay/Cambay-Regular.ttf");
                    }
                    try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                        Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
                        this.fuente = myFont.deriveFont(Font.PLAIN, 20);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.fuente = new Font(Font.SERIF, Font.PLAIN, 20);
                }
                this.setFont(this.fuente);
                break;
            case NORMAL:
            default:
                try (InputStream resourceStream = getClass().getResourceAsStream("/letras/Cambay/Cambay-Bold.ttf")) {
                    if (resourceStream == null) {
                        throw new IllegalArgumentException("Recurso de fuente no encontrado en classpath: /letras/Cambay/Cambay-Bold.ttf");
                    }
                    try (InputStream myStream = new BufferedInputStream(resourceStream)) {
                        Font myFont = Font.createFont(Font.TRUETYPE_FONT, myStream);
                        this.fuente = myFont.deriveFont(Font.BOLD, 23f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    this.fuente = new Font(Font.SERIF, Font.BOLD, 23);
                    System.out.println("aaaaaaaaaaaaaa");
                }
                this.setFont(this.fuente);
                break;
        }
    }
    
}
