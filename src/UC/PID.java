/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UC;

/**
 *
 * @author jeanmarioml
 */
public class PID {
    
    private double kP,kI,kD;
    
    private double i_prev = 0;
    private double e_prev = 0;
    private double h = 0.1;
    private double acaoProporcional=0;
    private double acaoIntegral=0;
    private double acaoDerivativa=0;

    PID(){
        this.kP=1;
        this.kI=0;
        this.kD=0;
    }
    public double getAcaoProporcional() {
        return acaoProporcional;
    }

    public void setAcaoProporcional(double acaoProporcional) {
        this.acaoProporcional = acaoProporcional;
    }

    public double getAcaoIntegral() {
        return acaoIntegral;
    }

    public void setAcaoIntegral(double acaoIntegral) {
        this.acaoIntegral = acaoIntegral;
    }

    public double getAcaoDerivativa() {
        return acaoDerivativa;
    }

    public void setAcaoDerivativa(double acaoDerivativa) {
        this.acaoDerivativa = acaoDerivativa;
    }
    
    public void setkP(double kP) {
        this.kP = kP;
    }

    public void setkI(double kI) {
        this.kI = kI;
    }

    public void setkD(double kD) {
        this.kD = kD;
    }
    
    
    //Ação Proporcional e Controle Porporcional
    
    public double acaoProporcional(double erro){
        acaoProporcional=this.kP*erro;
        return this.kP*erro;
    }
    
    //Ação Integral
    
    public double acaoIntegral(double erro){
        double i = this.i_prev + (this.kI*h*erro);
        i_prev = i;
        setAcaoIntegral(i);
        return i;
    }
    
    //Ação Derivativa
    public double acaoDerivativa(double erro){
        double d = this.kD * ((erro - this.e_prev)/h);
        e_prev = erro;
        setAcaoDerivativa(d);
        return d;
    }
    
    //Controle PI
    public double controlePI(double erro){
        return (this.acaoProporcional(erro) + this.acaoIntegral(erro));
    }
    
    //Controle PD
    public double controlePD(double erro){
        return (this.acaoProporcional(erro) + this.acaoDerivativa(erro));
    }
    
    //Controle PID
    public double controlePID(double erro){
        return (this.acaoProporcional(erro) + this.acaoIntegral(erro) + this.acaoDerivativa(erro));
    }
    
    //Controle PI - Derivativo
    public double controlePI_D(double erro, double nivel){
        return (this.acaoProporcional(erro) + this.acaoIntegral(erro) + this.acaoDerivativa(nivel));
    }
    
}
