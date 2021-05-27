/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UC;

import static java.lang.Math.abs;
import java.util.ArrayList;

/**
 *
 * @author Djanilson
 */
public class Especificacoes {

    private double tr100 = 0, tr95 = 0, tr90 = 0, tr5 = 0, tr10 = 0, tr595 = 0, tr1090 = 0; //variaveis pra armazenar o tempo de subida em 5,10, 90, 95 e 100%
    private double tp = 0, mp = 0;// tempo de pico e sobressinal
    private double ts2 = 0, ts5 = 0, ts7 = 0, ts10 = 0; //tempos de acomodação
    private String ts2String = "", ts5String = "", ts7String = "", ts10String = ""; //armazenam as strings pra aparecerem no campo de texto na interface gráfica
    private String tpstring = "", mpString = "", mpPorCentoString = "";
    private double tempoLocal = 0, tempoLocalts = 0; //esse tempo é diferente do tempo da outra classe
    private boolean flagtp = true;
    private boolean flagts2 = true, flagts5 = true, flagts7 = true, flagts10 = true; //flags pra o calculo do tempo de acomodaçao
    private boolean flagtr = true;
    ArrayList<Double> niveis = new ArrayList<Double>();
    ArrayList<Double> tempos = new ArrayList<Double>();
    int contador = 0;
    double maior = 0, menor=1000000;
    int i_maior = 0, i_menor=0;
    double nivelQuandoReferenciaMuda;
    public Especificacoes() {
    }

    public String getMpString() {
        return mpString;
    }

    public void setMpString(String mpString) {
        this.mpString = mpString;
    }

    public String getMpPorCentoString() {
        return mpPorCentoString;
    }

    public void setMpPorCentoString(String mpPorCentoString) {
        this.mpPorCentoString = mpPorCentoString;
    }

    //retorna a string com o tempo de subida pra imprimir no textField
    String tempoDeSubida(double nivelTank, double referencia, double referencia_prev, double tempo, int index, boolean cascata) {
        if (referencia > referencia_prev) {
            flagtr = true;
            tempoLocalts = 0;
            nivelQuandoReferenciaMuda=nivelTank;
        }else if(referencia < referencia_prev){
            flagtr=false;
            tempoLocalts = 0;
            nivelQuandoReferenciaMuda=nivelTank;
        }

        double fator = 1;
       if(cascata){
           fator=0.95;
       }
        if ((nivelTank <= referencia*fator) && flagtr == true) {  //o valor enviado é o valor de referencia
            tr100 = tempoLocalts;
            if (nivelTank <= (referencia-nivelQuandoReferenciaMuda) * 0.05+ nivelQuandoReferenciaMuda) {
                tr5 = tempoLocalts;
            }
            if (nivelTank <= (referencia-nivelQuandoReferenciaMuda) * 0.10+ nivelQuandoReferenciaMuda) {
                tr10 = tempoLocalts;
            }
            if (nivelTank <= (referencia-nivelQuandoReferenciaMuda) * 0.90+ nivelQuandoReferenciaMuda) {
                tr90 = tempoLocalts;
            }
            if (nivelTank <= (referencia-nivelQuandoReferenciaMuda) * 0.95+ nivelQuandoReferenciaMuda) {
                tr95 = tempoLocalts;
            }
        }else if((nivelTank >= referencia*fator) && flagtr == false){ //pra o tempo de descida
             tr100 = tempoLocalts;
            if (nivelTank >= nivelQuandoReferenciaMuda - (nivelQuandoReferenciaMuda - referencia) * 0.05) {
                tr5 = tempoLocalts;
            }
            if (nivelTank >= nivelQuandoReferenciaMuda - (nivelQuandoReferenciaMuda - referencia) * 0.10) {
                tr10 = tempoLocalts;
            }
            if (nivelTank >= nivelQuandoReferenciaMuda - (nivelQuandoReferenciaMuda - referencia) * 0.90) {
                tr90 = tempoLocalts;
            }
            if (nivelTank >= nivelQuandoReferenciaMuda - (nivelQuandoReferenciaMuda - referencia) * 0.95) {
                tr95 = tempoLocalts;
            }
        }else {
            tr595 = tr95 - tr5;
            tr1090 = tr90 - tr10;
          
            switch (index) {
                case 0:
                    if(cascata){
                     return "";
                }
                    return String.valueOf(tr100); 
                case 1:
                    return String.valueOf(tr595);
                case 2:
                    return String.valueOf(tr1090);
                default:
                    break;
            }
        }
        tempoLocalts = tempoLocalts + 0.1;
        return "";
    }
    
    String tempoDePico(double nivelTank, double referencia, double referencia_prev){
        
        if (referencia > referencia_prev) {
            flagtp = true;        //para indicar q a nova referencia é maior do que a referencia anterior
            tempoLocal=0;         //o tempo local eh zerado sempre q a referencia muda
            setMpString("");
            setMpPorCentoString("");
        }else if (referencia < referencia_prev){
            flagtp = false;       //para indicar q a nova referencia é MENOR do que a referencia anterior
            tempoLocal=0;
            setMpString("");
            setMpPorCentoString("");
        }
        if(nivelTank>maior && flagtp==true){
            maior=nivelTank;
            contador=0;         // a variavel contador serve pra assegurar q o valor maximo ou minimo nao foi atualizado por 15 passos seguidos
        }else if(nivelTank<menor && flagtp==false){
            menor=nivelTank;
            contador=0;
        }else{
            contador++;
        }
        
            
        if(contador==15 && flagtp==true){
            mp=maior;
            setMpString(String.valueOf(mp));
            tp=tempoLocal-1.5; //pq se passaram 1.5 segundos ate o contador contar ate 15
            tpstring=String.format("%.2f",tp);// TESTAR PRA VER SE SO SAI COM DUAS CASAS DECIMAIS
            //tpstring=String.valueOf(tp);   
            setMpPorCentoString(String.valueOf(abs(((mp-referencia)/referencia)*100)));
        }else if(contador==15 && flagtp==false){
            mp=menor;
            setMpString(String.valueOf(mp));
            setMpPorCentoString(String.valueOf(((mp-referencia)/referencia)*100));
            tp=tempoLocal-1.5; //pq se passaram 1.5 segundos ate o contador contar ate 15
            tpstring=String.valueOf(tp);   
        }else if(contador>15){
            return tpstring;
        }
        
        tempoLocal=tempoLocal+0.1;
        return "";
    }

    //calcula o tempo de pico e nela tambem é calculado o sobressinal
  /*  String tempoDePico(double nivelTank, double referencia, double referencia_prev) {
        niveis.add(nivelTank);
        tempos.add(tempoLocal);
        tempoLocal=tempoLocal+0.1;
        if (referencia > referencia_prev) {
            contador = 0;
            tempoLocal=0;
            niveis.clear();
            tempos.clear();
        }else if (referencia < referencia_prev){
            contador = 3;
            tempoLocal=0;
            niveis.clear();
            tempos.clear();
        }
        
        if ((nivelTank < referencia) && contador == 3) {
            contador=contador+1;
        }
        
        if ((nivelTank > referencia) && contador == 4) {
            contador++;
             for (int i = 0; i < niveis.size(); i++) {
                if (niveis.get(i) < menor) {
                    menor = niveis.get(i);
                    i_menor = i;
                }
             }   
        }

        if (contador == 5) {       
            mp = menor;
            mpString = String.valueOf(mp);
            mpPorCentoString=String.valueOf(((mp-referencia)/referencia)*100);
            return String.valueOf(tempos.get(i_menor));
        }
             
        if ((nivelTank > referencia) && contador == 0) {
            contador++;
        }
      
        if ((nivelTank < referencia )&& contador == 1) {
            contador++;
            for (int i = 0; i < niveis.size(); i++) {
                if (niveis.get(i) > maior) {
                    maior = niveis.get(i);
                    i_maior = i;
                }
            }
            
        }

        if (contador == 2) {
            mp = maior;
            mpString = String.valueOf(mp);
            mpPorCentoString=String.valueOf(((mp-referencia)/referencia)*100);
            return String.valueOf(tempos.get(i_maior));
        }
        return "";
    }*/

    //retorna a string com o tempo de acomodaçao
    String tempoDeAcomodacao(double nivelTank, double nivelTank_prev, double referencia, double referencia_prev, int index) { //index é o indice da opçao selecionada no combobox

        if (nivelTank >= referencia * 0.98 && nivelTank <= referencia * 1.02 && flagts2 == true) {
            ts2 = tempoLocal;
            ts2String = String.valueOf(ts2);
            flagts2 = false;
        }
        if ((nivelTank < referencia * 0.98 || nivelTank > referencia * 1.02) && flagts2 == false) {
            flagts2 = true;
            ts2String = "";
        }

        if (nivelTank >= referencia * 0.95 && nivelTank <= referencia * 1.05 && flagts5 == true) {
            ts5 = tempoLocal;
            ts5String = String.valueOf(ts5);
            flagts5 = false;
        }
        if ((nivelTank < referencia * 0.95 || nivelTank > referencia * 1.05) && flagts5 == false) {
            flagts5 = true;
            ts5String = "";
        }

        if (nivelTank >= referencia * 0.93 && nivelTank <= referencia * 1.07 && flagts7 == true) {
            ts7 = tempoLocal;
            ts7String = String.valueOf(ts7);
            flagts7 = false;
        }
        if ((nivelTank < referencia * 0.93 || nivelTank > referencia * 1.07) && flagts7 == false) {
            flagts7 = true;
            ts7String = "";
        }

        if (nivelTank >= referencia * 0.90 && nivelTank <= referencia * 1.1 && flagts10 == true) {
            ts10 = tempoLocal;
            ts10String = String.valueOf(ts10);
            flagts10 = false;
        }
        if ((nivelTank < referencia * 0.90 || nivelTank > referencia * 1.1) && flagts10 == false) {
            flagts10 = true;
            ts10String = "";
        }

        switch (index) {
            case 0:
                return ts2String;
            case 1:
                return ts5String;
            case 2:
                return ts7String;
            case 3:
                return ts10String;
            default:
                break;
        }
        return "";
    }
}
