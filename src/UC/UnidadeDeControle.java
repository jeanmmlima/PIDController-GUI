 /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UC;

import Comunicacao.QuanserClient;
import Comunicacao.QuanserClientException;
import GUI.Painel;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jeanmarioml
 */
public class UnidadeDeControle implements Runnable {

    private double nivelTank = 0,nivelTank_prev=0;
    private double nivelTank2 = 0, nivelTank2_prev=0;
    

    private double tensaoBomba;
    QuanserClient quanser;
    //Variaveis de tempo
    private long cicloExecucao = 100;
    private long tempoDeExecucao;
    Scanner ler = new Scanner(System.in);
    Painel frame;
    PID pid = new PID();
    PID pidCascataEscravo = new PID();
    PID pidCascataMestre = new PID();

    Especificacoes espec = new Especificacoes();
    ObservadorDeEstados oe = new ObservadorDeEstados();
    

    //construtor
    public UnidadeDeControle(Painel _frame, QuanserClient _q) {
        frame = _frame;
        quanser = _q;
        
    }

    public void setQuanser(QuanserClient _q) {
        quanser = _q;
    }

    //Recebe o nivel indicado pelo sensor e repassa a variavel
    public void setNivelTank(double _nivelTank) {
        nivelTank = _nivelTank;
    }

    public void setNivelTank2(double _nivelTank2) {
        nivelTank2 = _nivelTank2;
    }

    public double getNivelTank() {
        return nivelTank;
    }

    public double getNivelTank2() {
        return nivelTank2;
    }

    public double getNivelTank_prev() {
        return nivelTank_prev;
    }

    public void setNivelTank_prev(double nivelTank_prev) {
        this.nivelTank_prev = nivelTank_prev;
    }

    public double getNivelTank2_prev() {
        return nivelTank2_prev;
    }

    public void setNivelTank2_prev(double nivelTank2_prev) {
        this.nivelTank2_prev = nivelTank2_prev;
    }
    
    public double getTensaoBomba() {
        return tensaoBomba;
    }

    public void setTensaoBomba(double tensaoBomba) {
        this.tensaoBomba = tensaoBomba;
    }
    
    
    public void configCascata(double nivel1, double nivel2, double tensao, double nivel2_prev){
        
        //----MESTRE-----
        double amplitude = tensao;
        double erro = amplitude - nivel2;
        
             frame.getjTextField_Tr().setText(espec.tempoDeSubida(nivel2, frame.getValor_enviado(), frame.getValor_enviadoPrev(),frame.getTempo(), frame.getjComboBox_Tr().getSelectedIndex(),frame.isCascata()));
            if(frame.getjComboBox_Mp().getSelectedIndex()==0){
                frame.getjTextField_Mp().setText(espec.getMpPorCentoString());
            }else if(frame.getjComboBox_Mp().getSelectedIndex()==1){
                frame.getjTextField_Mp().setText(espec.getMpString());
            }
           frame.getjTextField_Ts().setText(espec.tempoDeAcomodacao(nivel2, nivel2_prev, frame.getValor_enviado(), frame.getValor_enviadoPrev(), frame.getjComboBox_Ts().getSelectedIndex()));
           frame.getjTextFieldTp().setText(espec.tempoDePico(nivel2, frame.getValor_enviado(),frame.getValor_enviadoPrev()));
        
        //Controle P
            if (frame.getControleCMestre() == 1) {
                pidCascataMestre.setkP(frame.getmKp());
                this.tensaoBomba = pidCascataMestre.acaoProporcional(erro);
                frame.setAcaoProporcionalValor(pidCascataMestre.getAcaoProporcional());
            }
            
            if (frame.getControleCMestre() == 2) {
                pidCascataMestre.setkP(frame.getmKp());
                pidCascataMestre.setkI(frame.getmKi());
                this.tensaoBomba = pidCascataMestre.controlePI(erro);
                frame.setAcaoIntegrativaValor(pidCascataMestre.getAcaoIntegral());//pra gerar o grafico com os ganhos
                frame.setAcaoProporcionalValor(pidCascataMestre.getAcaoProporcional());
            }
            //Controle PD
            if (frame.getControleCMestre() == 3) {
                pidCascataMestre.setkP(frame.getmKp());
                pidCascataMestre.setkD(frame.getmKd());
                this.tensaoBomba = pidCascataMestre.controlePD(erro);
                frame.setAcaoDerivativaValor(pidCascataMestre.getAcaoDerivativa());
                frame.setAcaoProporcionalValor(pidCascataMestre.getAcaoProporcional());
            }
            //Controle PID
            if (frame.getControleCMestre() == 4) {
                pidCascataMestre.setkP(frame.getmKp());
                pidCascataMestre.setkI(frame.getmKi());
                pidCascataMestre.setkD(frame.getmKd());
                this.tensaoBomba = pidCascataMestre.controlePID(erro);
                frame.setAcaoProporcionalValor(pidCascataMestre.getAcaoProporcional());
                frame.setAcaoIntegrativaValor(pidCascataMestre.getAcaoIntegral());
                frame.setAcaoDerivativaValor(pidCascataMestre.getAcaoDerivativa());
            }
            //Controle PI-D
            if (frame.getControleCMestre() == 5) {
                pidCascataMestre.setkP(frame.getmKp());
                pidCascataMestre.setkI(frame.getmKi());
                pidCascataMestre.setkD(frame.getmKd());
                this.tensaoBomba = pidCascataMestre.controlePI_D(erro, nivel2);
                frame.setAcaoProporcionalValor(pidCascataMestre.getAcaoProporcional());
                frame.setAcaoIntegrativaValor(pidCascataMestre.getAcaoIntegral());
                frame.setAcaoDerivativaValor(pidCascataMestre.getAcaoDerivativa());
            }
            
            //-------ESCRAVO------
            
            double erro2 = this.tensaoBomba - nivel1;
            
            if (frame.getControleCMestre() == 1) {
                pidCascataEscravo.setkP(frame.geteKp());
                this.tensaoBomba = pidCascataEscravo.acaoProporcional(erro);
                frame.setAcaoProporcionalValor(pidCascataEscravo.getAcaoProporcional());
            }
            
            if (frame.getControleCMestre() == 2) {
                pidCascataEscravo.setkP(frame.geteKp());
                pidCascataEscravo.setkI(frame.geteKi());
                this.tensaoBomba = pidCascataEscravo.controlePI(erro2);
                frame.setAcaoIntegrativaValor(pidCascataEscravo.getAcaoIntegral());//pra gerar o grafico com os ganhos
                frame.setAcaoProporcionalValor(pidCascataEscravo.getAcaoProporcional());
            }
            //Controle PD
            if (frame.getControleCMestre() == 3) {
                pidCascataEscravo.setkP(frame.geteKp());
                pidCascataEscravo.setkD(frame.geteKd());
                this.tensaoBomba = pidCascataEscravo.controlePD(erro2);
                frame.setAcaoDerivativaValor(pidCascataEscravo.getAcaoDerivativa());
                frame.setAcaoProporcionalValor(pidCascataEscravo.getAcaoProporcional());
            }
            //Controle PID
            if (frame.getControleCMestre() == 4) {
                pidCascataEscravo.setkP(frame.geteKp());
                pidCascataEscravo.setkI(frame.geteKi());
                pidCascataEscravo.setkD(frame.geteKd());
                this.tensaoBomba = pidCascataEscravo.controlePID(erro2);
                frame.setAcaoProporcionalValor(pidCascataEscravo.getAcaoProporcional());
                frame.setAcaoIntegrativaValor(pidCascataEscravo.getAcaoIntegral());
                frame.setAcaoDerivativaValor(pidCascataEscravo.getAcaoDerivativa());
            }
            //Controle PI-D
            if (frame.getControleCMestre() == 5) {
                pidCascataEscravo.setkP(frame.geteKp());
                pidCascataEscravo.setkI(frame.geteKi());
                pidCascataEscravo.setkD(frame.geteKd());
                this.tensaoBomba = pidCascataEscravo.controlePI_D(erro2, nivel1);
                frame.setAcaoProporcionalValor(pidCascataEscravo.getAcaoProporcional());
                frame.setAcaoIntegrativaValor(pidCascataEscravo.getAcaoIntegral());
                frame.setAcaoDerivativaValor(pidCascataEscravo.getAcaoDerivativa());
            }
        
    }
    
    public void checaMalha(boolean malha, double nivel, double tensao, double nivel_prev) {
        if (!malha) {
            this.tensaoBomba = tensao;
        } else {
            double amplitude = tensao;
            double erro = amplitude - nivel;

            frame.getjTextField_Tr().setText(espec.tempoDeSubida(nivel, frame.getValor_enviado(), frame.getValor_enviadoPrev(),frame.getTempo(), frame.getjComboBox_Tr().getSelectedIndex(),frame.isCascata()));
            if(frame.getjComboBox_Mp().getSelectedIndex()==0){
                frame.getjTextField_Mp().setText(espec.getMpPorCentoString());
            }else if(frame.getjComboBox_Mp().getSelectedIndex()==1){
                frame.getjTextField_Mp().setText(espec.getMpString());
            }
           frame.getjTextField_Ts().setText(espec.tempoDeAcomodacao(nivel, nivel_prev, frame.getValor_enviado(), frame.getValor_enviadoPrev(), frame.getjComboBox_Ts().getSelectedIndex()));
           frame.getjTextFieldTp().setText(espec.tempoDePico(nivel, frame.getValor_enviado(),frame.getValor_enviadoPrev()));
            
            
            
            //Controle P
            if (frame.getControle() == 1) {
                pid.setkP(frame.getkP());
                this.tensaoBomba = pid.acaoProporcional(erro);
                frame.setAcaoProporcionalValor(pid.getAcaoProporcional());
            }
            
            if (frame.getControle() == 2) {
                pid.setkP(frame.getkP());
                pid.setkI(frame.getkI());
                this.tensaoBomba = pid.controlePI(erro);
                frame.setAcaoIntegrativaValor(pid.getAcaoIntegral());//pra gerar o grafico com os ganhos
                frame.setAcaoProporcionalValor(pid.getAcaoProporcional());
            }
            //Controle PD
            if (frame.getControle() == 3) {
                pid.setkP(frame.getkP());
                pid.setkD(frame.getkD());
                this.tensaoBomba = pid.controlePD(erro);
                frame.setAcaoDerivativaValor(pid.getAcaoDerivativa());
                frame.setAcaoProporcionalValor(pid.getAcaoProporcional());
            }
            //Controle PID
            if (frame.getControle() == 4) {
                pid.setkP(frame.getkP());
                pid.setkI(frame.getkI());
                pid.setkD(frame.getkD());
                this.tensaoBomba = pid.controlePID(erro);
                frame.setAcaoProporcionalValor(pid.getAcaoProporcional());
                frame.setAcaoIntegrativaValor(pid.getAcaoIntegral());
                frame.setAcaoDerivativaValor(pid.getAcaoDerivativa());
            }
            //Controle PI-D
            if (frame.getControle() == 5) {
                pid.setkP(frame.getkP());
                pid.setkI(frame.getkI());
                pid.setkD(frame.getkD());
                this.tensaoBomba = pid.controlePI_D(erro, nivel);
                frame.setAcaoProporcionalValor(pid.getAcaoProporcional());
                frame.setAcaoIntegrativaValor(pid.getAcaoIntegral());
                frame.setAcaoDerivativaValor(pid.getAcaoDerivativa());
            }
        }
    }

    public void restricoesTensao(double _tensaoDoSinal) {
        //implementar Restricoes
        if (_tensaoDoSinal > 4) {
            tensaoBomba = 4.0;
        }
        if (_tensaoDoSinal < -4) {
            tensaoBomba = -4.0;
        }
        if (getNivelTank() < 4 && _tensaoDoSinal < 0) {
            tensaoBomba = 0.0;
        }
        if (getNivelTank() > 28 && _tensaoDoSinal > 3.25) {
            tensaoBomba = 3.25;
        }
        if (getNivelTank() > 29 && _tensaoDoSinal > 0) {
            tensaoBomba = 0.0;
        }

        //envia o valor do sinal enviado apos as restrições para gerar o gráfico
        frame.setSinalEnviadoAposRestricoes(tensaoBomba);
    }

    @Override
    public void run() {
        //Execucao da Thread
        tempoDeExecucao = System.nanoTime();

        try {
            setNivelTank_prev(getNivelTank());
            setNivelTank2_prev(getNivelTank2());
            //Leitura do Nivel
            setNivelTank(quanser.read(0) * 6.25); //multiplica por 6.25 (fator de conversao) pq o sensor envia um valor de tensao de 0 a 5, mas desejamos saber o valor em cm
            setNivelTank2(quanser.read(1) * 6.25);
            //System.out.println("O nivel do tank 1 e: " + getNivelTank());
            // setNivelTank(ler.nextDouble());
            //Atualiza Grafico
            frame.saidas_dos_tanques(getNivelTank(), getNivelTank2());
            // frame.saidas_dos_tanques(getNivelTank(),getNivelTank2());
            //Atualiza Tanks
            frame.setNivelTank1(getNivelTank());
            frame.setNivelTank2(getNivelTank2());
        } catch (QuanserClientException ex) {
            Logger.getLogger(UnidadeDeControle.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Metodo para setar uma tensao de acordo com o sinal (tensaoSinal)
        // frame.randomTensaoTeste();
        double tensaoSinal = frame.getValor_enviado();
        System.out.println("Tensao que chega do sinal: " + tensaoSinal);
        //Executa restricoes na tensao do respectivo sinal em execucao
        tensaoBomba = tensaoSinal;
        
        if(frame.isCascata()){
            this.configCascata(getNivelTank(), getNivelTank2(), tensaoSinal, getNivelTank2_prev());
        }

        else if (frame.getjRadioButton_PV1().isSelected()) {
            this.checaMalha(frame.getMalha(), getNivelTank(), tensaoBomba, getNivelTank_prev());
        } else {
            this.checaMalha(frame.getMalha(), getNivelTank2(), tensaoBomba, getNivelTank2_prev());
            oe.setNiveisObservados(tensaoBomba, getNivelTank2());
        }
        frame.setSinalControle(tensaoBomba);
        restricoesTensao(tensaoBomba);

        try {
            //Apos restricoes, escreve a tensao na bomba

            //Escrita na Bomba
            quanser.write(0, tensaoBomba);
            System.out.println("A tensao e: " + this.tensaoBomba);
        } catch (QuanserClientException ex) {
            Logger.getLogger(UnidadeDeControle.class.getName()).log(Level.SEVERE, null, ex);
        }

        //Calcula o tempo restante de execucao. Deve ser algum valor entre 0 e 100 (0.1s)
        long tempoRestante = cicloExecucao - (int) (System.nanoTime() - tempoDeExecucao) / 1000000;
        try {
            //Caso tenha tempo restante, a Thread espera esse tempo ate que possa executar de novo.
            if (tempoRestante > 0 && tempoRestante <= 100) {
                Thread.sleep(tempoRestante);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(UnidadeDeControle.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void main(String args[]) throws InterruptedException, QuanserClientException {

        Thread thrdControle;
        QuanserClient quanserCliente = null;

        Painel gui = new Painel();

        gui.show();
        gui.pack();

        while (true) {
            //Enquanto a conexao com a planta nao eh estabelecida
            while (!gui.isConectado()) {
                System.out.println("Planta nao esta conectada!");
                Thread.sleep(100);
            }

            //Quando sair do loop acima, indica que o usuario passou valores de ip e porta e clicou no botao conectar.
            //Codigo de Conexao!
            try {
                quanserCliente = new QuanserClient("10.13.99.69", 20081);
                System.out.println("Conectou!");
            } catch (QuanserClientException ex) {
                ex.printStackTrace();
            }

            UnidadeDeControle uc = new UnidadeDeControle(gui, quanserCliente);
            while (gui.isConectado()) {
                thrdControle = new Thread(uc);

                thrdControle.start();
                try {
                    thrdControle.join();
                } catch (InterruptedException ie) {
                }
            }
        }

    }

}
