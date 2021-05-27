/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GUI;

//import br.ufrn.dca.controle.QuanserClient;
import java.util.Random;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

/**
 *
 * @author Usuário
 */
public class Painel extends javax.swing.JFrame {

    //Global variables 
    boolean flag = false; //pro while da thread do grafico
    double tempo = 0; //contador de tempo
    double offset = 0;
    double periodo = 30;
    double frequencia = 1; //receberá a frequencia adicionada pelo usuário
    double freq = 1 / periodo;
    double amplitude = 0;
    double amplitude_max = 4, amplitude_min = -4;
    double duracao_max = 40, duracao_min = 5;
    double valor_enviado = 0, valor_enviadoPrev=0; //vai receber o valor de "num", pra ser enviado ao tanque
    double sinalEnviadoAposRestricoes =0;
    double sinalControle =0;
    double valor_recebido1 = 0; //recebera o sinal de saido do tanque 1
    double valor_recebido2 = 0; //recebera o sinal de saido do tanque 2
    Random randGen = new Random(); //objeto utilizado pra gerar numeros aleatorios
    double amplitude_aleatoria = randGen.nextInt((int) (amplitude_max - amplitude_min)) + amplitude_min; //usado para o sinal aleatorio
    double duracao_aleatoria = randGen.nextInt((int) (duracao_max - duracao_min)) + duracao_min; //usado para o sinal aleatorio
    double contador = 0; //sera utilizado pra gerar a onda aleatoria
    
    static final XYSeries entrada_sinal = new XYSeries("Sinal de Controle");
    static final XYSeries sinal_enviado = new XYSeries("Sinal Saturado");
    static final XYSeries acao_proporcional = new XYSeries("Sinal proporcional");
    static final XYSeries acao_integral = new XYSeries("Sinal integrativo");
    static final XYSeries acao_derivativa = new XYSeries("Sinal derivativo");
    static final XYSeries sinal_recebidoTanque1 = new XYSeries("Tanque 1");
    static final XYSeries sinal_recebidoTanque2 = new XYSeries("Tanque 2");
    static final XYSeries referencia_nivel = new XYSeries("Referência");
    
    double tensaoBombaTeste;
    boolean conectado = false;
    
    //Controle----
    int controle = 1;
   /// boolean pv1=true, pv2=false;
    double kP = 1, kI = 0, kD = 0;
    double acaoDerivativaValor=0, acaoIntegrativaValor=0, acaoProporcionalValor=0; //receberao os valores das acoes gerados pelas funcoes acaoProporciona, acaoderivativa e acaointegral
    
    //---CONTROLE CASCATA-----
    int controleCMestre = 1, controleCEscravo = 1;
    double mKp = 1, mKi = 0, mKd = 0;
    double eKp = 1, eKi = 0, eKd = 0;

    public int getControleCMestre() {
        return controleCMestre;
    }

    public int getControleCEscravo() {
        return controleCEscravo;
    }

    public double getmKp() {
        return mKp;
    }

    public double getmKi() {
        return mKi;
    }

    public double getmKd() {
        return mKd;
    }

    public double geteKp() {
        return eKp;
    }

    public double geteKi() {
        return eKi;
    }

    public double geteKd() {
        return eKd;
    }
    
    //-----------------

    
    public int getControle() {
        return controle;
    }

    public double getkP() {
        return kP;
    }

    public double getkI() {
        return kI;
    }

    public double getkD() {
        return kD;
    }

    public double getAcaoDerivativaValor() {
        return acaoDerivativaValor;
    }

    public void setAcaoDerivativaValor(double acaoDerivativaValor) {
        this.acaoDerivativaValor = acaoDerivativaValor;
    }

    public double getAcaoIntegrativaValor() {
        return acaoIntegrativaValor;
    }

    public void setAcaoIntegrativaValor(double acaoIntegrativaValor) {
        this.acaoIntegrativaValor = acaoIntegrativaValor;
    }

    public double getAcaoProporcionalValor() {
        return acaoProporcionalValor;
    }

    public void setAcaoProporcionalValor(double acaoProporcionalValor) {
        this.acaoProporcionalValor = acaoProporcionalValor;
    }
    
    
    //-----
    

    String ip;
    int porta;
    //malha is true -> fechada
    //malha is false -> aberta
    boolean malha = false;
    boolean cascata = false;
    double nivelDesejado = 0.0;

    public boolean isCascata() {
        return cascata;
    }
    
    
    public double getSinalEnviadoAposRestricoes() {
        return sinalEnviadoAposRestricoes;
    }

    public void setSinalEnviadoAposRestricoes(double sinalEnviadoAposRestricoes) {
        this.sinalEnviadoAposRestricoes = sinalEnviadoAposRestricoes;
    }
    
    public void setSinalControle(double sinalControle){
        this.sinalControle = sinalControle;
    }
    
    public double getSinalControle(){
        return sinalControle;
    }
    
    public void setNivelDesejado(double nivelDesejado) {
        this.nivelDesejado = nivelDesejado;
    }

    public double getNivelDesejado() {
        return nivelDesejado;
    }

    public boolean isMalha() {
        return malha;
    }

    public void setMalha(boolean malha) {
        this.malha = malha;
    }

    public boolean getMalha() {
        return malha;
    }

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    public String getIp() {
        return ip;
    }

    public int getPorta() {
        return porta;
    }
    
    public void setNivelTank1(double _nivel) {
        tank1.setValue((int) _nivel);
    }

    public void setNivelTank2(double _nivel) {
        tank2.setValue((int) _nivel);
    }

    public void setTensaoBombaTeste(double _tensao) {
        tensaoBombaTeste = _tensao;
    }

    public double getTensaoBombaTeste() {
        return tensaoBombaTeste;
    }

    public double getTempo() {
        return tempo;
    }

    public void setTempo(double tempo) {
        this.tempo = tempo;
    }

    public double getValor_enviadoPrev() {
        return valor_enviadoPrev;
    }

    public void setValor_enviadoPrev(double valor_enviadoPrev) {
        this.valor_enviadoPrev = valor_enviadoPrev;
    }
    
    

    /**
     * Creates new form Painel
     */
    public Painel() {

        initComponents();
        jRBMA.setSelected(true);

        jRadioButton_canal0.setSelected(true);
        jRadioButton_degrau.setSelected(true);
        jRadioButton_Tanque1.setSelected(true);
        jRadioButton_Tanque2.setSelected(true);
        
        jRadioButton_ExibirAcaoDerivativa.setEnabled(false);
        jRadioButton_ExibirAcaoIntegrativa.setEnabled(false);
        jRadioButton_ExibirAcaoProporcional.setEnabled(false);

        jTextField5_periodo.setEnabled(false);
        jTextField6_offset.setEnabled(false);
        jTextField_amplitudeMaxima.setEnabled(false);
        jTextField_amplitudeMinima.setEnabled(false);
        jTextField_duracaoMaxima.setEnabled(false);
        jTextField_duracaoMinima.setEnabled(false);
        
        jRadioButton_PV2.setEnabled(false);
        jRadioButton_PV1.setEnabled(false);
        jRadioButton_PV2.setSelected(true);
        
        jTextFieldTp.setEditable(false);
        jTextField_Mp.setEditable(false);
        init_graph();
        init_graph_saida();

    }

    //Metodo para atualizacao dos niveis dos tanks
  

    public void randomTensaoTeste() {
        Random r = new Random();
        setTensaoBombaTeste(r.nextInt(4));
    }

    private void init_graph() {

        JFreeChart xylineChart = ChartFactory.createXYLineChart(
                "",
                "Tempo (s)",
                "Tensão (v)",
                createDataset(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel painelDoGrafico = new ChartPanel(xylineChart);

        jPanel2_sinalEnviado.setLayout(null);
        jPanel2_sinalEnviado.add(painelDoGrafico);
        painelDoGrafico.setBounds(jPanel2_sinalEnviado.getVisibleRect());

        // janelaDoGrafico.setVisible(true);
        // janelaDoGrafico.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        final XYPlot plot = xylineChart.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(50.0);
    }

    private void init_graph_saida() {
        JFreeChart xylineChartSaida = ChartFactory.createXYLineChart(
                "",
                "Tempo (s)",
                "Altura (cm)",
                createDatasetSaida(),
                PlotOrientation.VERTICAL,
                true, true, false);
        ChartPanel painelDoGraficoSaida = new ChartPanel(xylineChartSaida);

        jPanel3_sinalRecebido.setLayout(null);
        jPanel3_sinalRecebido.add(painelDoGraficoSaida);
        painelDoGraficoSaida.setBounds(jPanel2_sinalEnviado.getVisibleRect());
        final XYPlot plot = xylineChartSaida.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(50.0);
    }

    public double getValor_enviado() { //tensao enviada para o tanque, recebe o valor da variavel "num", enquanto os graficos sao gerados
        return valor_enviado;
    }

    //funçao pra criar o XYdataset pra gerar o grafico 2
    private XYDataset createDatasetSaida() {
        gen2 myGen = new gen2();
        new Thread(myGen).start();
        final XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(sinal_recebidoTanque1);
        dataset.addSeries(sinal_recebidoTanque2);
        dataset.addSeries(referencia_nivel);
        return dataset;
    }

    //funçao pra criar o XYdataset pra gerar o grafico 1
    private XYDataset createDataset() {

        gen myGen = new gen();
        new Thread(myGen).start();
//        Thread threadgrafico = new Thread(){
//            @Override public void run(){
//                 while(true) {
//                double num = Math.sin(Math.toRadians(i*360.0/periodo));
        final XYSeriesCollection dataset = new XYSeriesCollection();
       
        dataset.addSeries(entrada_sinal);
        dataset.addSeries(sinal_enviado);
        dataset.addSeries (acao_proporcional);
        dataset.addSeries(acao_integral);
        dataset.addSeries(acao_derivativa);
        return dataset;
    }

    
    private void setNivelDesejado() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Os sinais de referência são gerados nos próximos 5 métodos
     */
    private void criaSinalSenoidal() {
        double num = offset + amplitude * Math.sin(Math.toRadians(tempo * 360.0 / periodo));

        // System.out.println(num);
        if(!isMalha()){
        entrada_sinal.add(tempo, num);

        sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        //ts.addOrUpdate(new Millisecond(), num);
        }else{
            entrada_sinal.add(tempo, getSinalControle());
            referencia_nivel.add(tempo, num);
            sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        }
        valor_enviado = num;
       // tempo = tempo + 0.1;
    }

    private void criaSinalQuadrado() {
        if (tempo % periodo < periodo / 2) {
            double num = amplitude + offset;
            if(!isMalha()){
            entrada_sinal.add(tempo, num);
            sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
            }else{
                entrada_sinal.add(tempo, getSinalControle());
                referencia_nivel.add(tempo, num);
                sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
            }
            valor_enviado = num;
        } else {
            double num = offset - amplitude;
            if(!isMalha()){
            entrada_sinal.add(tempo, num);
            sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
            }else{
                entrada_sinal.add(tempo, getSinalControle());
                referencia_nivel.add(tempo, num);
                sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
            }
            valor_enviado = num;
        }  
       // tempo = tempo + 0.1;
    }

    private void criaSinalAleatorio() {
        double num = offset + amplitude_aleatoria;
        if(!isMalha()){
        entrada_sinal.add(tempo, num);
        if (contador > duracao_aleatoria) {
            amplitude_aleatoria = randGen.nextInt((int) (amplitude_max - amplitude_min)) + amplitude_min;
            duracao_aleatoria = randGen.nextInt((int) (duracao_max - duracao_min)) + duracao_min;
            contador = 0;
        }
        sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        }else{
            entrada_sinal.add(tempo, getSinalControle());
            referencia_nivel.add(tempo, num);
            sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
            
             if (contador > duracao_aleatoria) {
                    amplitude_aleatoria = randGen.nextInt((int) (amplitude_max - amplitude_min)) + amplitude_min;
                    duracao_aleatoria = randGen.nextInt((int) (duracao_max - duracao_min)) + duracao_min;
                    contador = 0;
                }
        }
        valor_enviado = num;
       // tempo = tempo + 0.1;
        contador = contador + 0.1;
    }

    private void criaSinalDenteDeSerra() {
        double num = offset + 2.0 * (amplitude / periodo) * (tempo % periodo) - amplitude;
        
        if(!isMalha()){
        entrada_sinal.add(tempo, num);
        sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        }else{
            entrada_sinal.add(tempo, getSinalControle());
            referencia_nivel.add(tempo, num);
            sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        }
        valor_enviado = num;
        //tempo = tempo + 0.1;
    }

    private void criaSinalDegrau() {
        double num = amplitude + offset;
        if(!isMalha()){
        entrada_sinal.add(tempo, num);
        sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        
        }else{
            entrada_sinal.add(tempo, getSinalControle());
            referencia_nivel.add(tempo, num);
            sinal_enviado.add(tempo,  getSinalEnviadoAposRestricoes());
        }
        valor_enviado = num;
       // tempo = tempo + 0.1;

    }
    
    private void criaSinaisDasAcoes(){
        acao_proporcional.add(tempo,getAcaoProporcionalValor());
        acao_integral.add(tempo,getAcaoIntegrativaValor());
        acao_derivativa.add(tempo,getAcaoDerivativaValor());
        
        
        if(!jRadioButton_ExibirAcaoDerivativa.isSelected()){
                        acao_derivativa.clear();
        }
        if(!jRadioButton_ExibirAcaoIntegrativa.isSelected()){
                        acao_integral.clear();
        }
        if(!jRadioButton_ExibirAcaoProporcional.isSelected()){
                        acao_proporcional.clear();
        }
    }
    

    //Thread do grafico do sinal de entrada
    class gen implements Runnable {

        @Override
        public void run() {
            while (flag) {
                //Atualiza valores de entrada em tempo real
                //set_entradas();
                
                if(isMalha()){ //so plota kp ki kd se a opçao malha fechada estiver selecionada
                criaSinaisDasAcoes();
                }
                valor_enviadoPrev=valor_enviado;
                if (jRadioButton_senoidal.isSelected()) {
                     criaSinalSenoidal();
                } else if (jRadioButton_degrau.isSelected()) {
                    criaSinalDegrau();
                } else if (jRadioButton_quadrada.isSelected()) {
                    criaSinalQuadrado();
                } else if (jRadioButton_denteDeSerra.isSelected()) {
                    criaSinalDenteDeSerra();
                } else if (jRadioButton_aleatoria.isSelected()) {
                    criaSinalAleatorio();
                } else {
                    System.out.println("Por favor, selecione um tipo de sinal");
                }
                tempo=tempo+0.1;

                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }
    
      //Thread do grafico dos tanques
    class gen2 implements Runnable {

        @Override
        public void run() {
            while (flag) {
                //if's para exibir ou esconder os graficos das saidas dos tanques 1 e 2
                if(jRadioButton_Tanque1.isSelected()){
                    sinal_recebidoTanque1.add(tempo, valor_recebido1);
                }else{
                        sinal_recebidoTanque1.clear();
                    }
                if(jRadioButton_Tanque2.isSelected()){
                    sinal_recebidoTanque2.add(tempo, valor_recebido2);
                }else{
                        sinal_recebidoTanque2.clear();
                    }
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    System.out.println(ex);
                }
            }
        }
    }

    //metodo para receber os valores de saida dos sensores dos tanques
    public void saidas_dos_tanques(double tanque1, double tanque2) {
        valor_recebido1 = tanque1;
        valor_recebido2 = tanque2;
    }

    //seta as entradas caso elas tenham sido digitadas
    public void set_entradas() {
        if (jTextField_amplitude.getText().length() > 0) {
            try {
                amplitude = Double.parseDouble(jTextField_amplitude.getText());
            } catch (NumberFormatException e) {

            }
        }

        if (jTextField_amplitudeMaxima.getText().length() > 0) {
            try {
                amplitude_max = Double.parseDouble(jTextField_amplitudeMaxima.getText());
            } catch (NumberFormatException e) {
            }

        }

        if (jTextField_amplitudeMinima.getText().length() > 0) {
            try {
                amplitude_min = Double.parseDouble(jTextField_amplitudeMinima.getText());
            } catch (NumberFormatException e) {
            }

        }
        if (jTextField_duracaoMaxima.getText().length() > 0) {
            try {
                duracao_max = Double.parseDouble(jTextField_duracaoMaxima.getText());
            } catch (NumberFormatException e) {
            }

        }
        if (jTextField_duracaoMinima.getText().length() > 0) {
            try {
                duracao_min = Double.parseDouble(jTextField_duracaoMinima.getText());
            } catch (NumberFormatException e) {
            }

        }

        if (jTextField6_offset.getText().length() > 0) {
            try {
                offset = Double.parseDouble(jTextField6_offset.getText());
            } catch (NumberFormatException e) {
            }

        }
        if (jTextField5_periodo.getText().length() > 0) {
            try {
                periodo = Double.parseDouble(jTextField5_periodo.getText());
            } catch (NumberFormatException e) {
            }
        }
        //Ganhos:
        if (jTextKP.getText().length() > 0) {
            try {
                kP = Double.parseDouble(jTextKP.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (jTextKI.getText().length() > 0) {
            try {
                kI = Double.parseDouble(jTextKI.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (jTextKD.getText().length() > 0) {
            try {
                kD = Double.parseDouble(jTextKD.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (tfCascataEscravoKP.getText().length() > 0) {
            try {
                eKp = Double.parseDouble(tfCascataEscravoKP.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (tfCascataEscravoKI.getText().length() > 0) {
            try {
                eKi = Double.parseDouble(tfCascataEscravoKI.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (tfCascataEscravoKD.getText().length() > 0) {
            try {
                eKd = Double.parseDouble(tfCascataEscravoKD.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (tfCascataMestreKP.getText().length() > 0) {
            try {
                mKp = Double.parseDouble(tfCascataMestreKP.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (tfCascataMestreKI.getText().length() > 0) {
            try {
                mKi = Double.parseDouble(tfCascataMestreKI.getText());
            } catch (NumberFormatException e) {
            }
        }
        
        if (tfCascataMestreKD.getText().length() > 0) {
            try {
                mKd = Double.parseDouble(tfCascataMestreKD.getText());
            } catch (NumberFormatException e) {
            }
        }
    }

  

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jInternalFrame1 = new javax.swing.JInternalFrame();
        jPanel1 = new javax.swing.JPanel();
        jPanel2_sinalEnviado = new javax.swing.JPanel();
        jPanel3_sinalRecebido = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel5 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jCheckBox1 = new javax.swing.JCheckBox();
        jCheckBox2 = new javax.swing.JCheckBox();
        jCheckBox3 = new javax.swing.JCheckBox();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jCheckBox6 = new javax.swing.JCheckBox();
        jCheckBox7 = new javax.swing.JCheckBox();
        jLabel8 = new javax.swing.JLabel();
        jRadioButton_canal0 = new javax.swing.JRadioButton();
        jRadioButton_canal1 = new javax.swing.JRadioButton();
        jRadioButton_canal2 = new javax.swing.JRadioButton();
        jRadioButton_canal3 = new javax.swing.JRadioButton();
        jRadioButton_canal4 = new javax.swing.JRadioButton();
        jRadioButton_canal5 = new javax.swing.JRadioButton();
        jRadioButton_canal6 = new javax.swing.JRadioButton();
        jRadioButton_canal7 = new javax.swing.JRadioButton();
        jCheckBox8 = new javax.swing.JCheckBox();
        jPanel2 = new javax.swing.JPanel();
        tank1 = new javax.swing.JProgressBar();
        tank2 = new javax.swing.JProgressBar();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jRadioButton_degrau = new javax.swing.JRadioButton();
        jRadioButton_senoidal = new javax.swing.JRadioButton();
        jRadioButton_quadrada = new javax.swing.JRadioButton();
        jRadioButton_denteDeSerra = new javax.swing.JRadioButton();
        jRadioButton_aleatoria = new javax.swing.JRadioButton();
        jLabel10 = new javax.swing.JLabel();
        jTextField_amplitude = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField5_periodo = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jTextField6_offset = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField_amplitudeMaxima = new javax.swing.JTextField();
        jTextField_amplitudeMinima = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField_duracaoMaxima = new javax.swing.JTextField();
        jLabel19 = new javax.swing.JLabel();
        jTextField_duracaoMinima = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jRBP = new javax.swing.JRadioButton();
        jRBPI = new javax.swing.JRadioButton();
        jRBPD = new javax.swing.JRadioButton();
        jRBPID = new javax.swing.JRadioButton();
        jRBPI_D = new javax.swing.JRadioButton();
        jLabel22 = new javax.swing.JLabel();
        jTextKP = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jTextKI = new javax.swing.JTextField();
        jLabel24 = new javax.swing.JLabel();
        jTextKD = new javax.swing.JTextField();
        jLabel_PV = new javax.swing.JLabel();
        jRadioButton_PV1 = new javax.swing.JRadioButton();
        jRadioButton_PV2 = new javax.swing.JRadioButton();
        jPanel6 = new javax.swing.JPanel();
        jTextField_Tr = new javax.swing.JTextField();
        jTextField_Ts = new javax.swing.JTextField();
        jTextFieldTp = new javax.swing.JTextField();
        jTextField_Mp = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jComboBox_Tr = new javax.swing.JComboBox<>();
        jComboBox_Ts = new javax.swing.JComboBox<>();
        jComboBox_Mp = new javax.swing.JComboBox<>();
        jPanel7 = new javax.swing.JPanel();
        jLabel29 = new javax.swing.JLabel();
        rbCascataMestreP = new javax.swing.JRadioButton();
        rbCascataMestrePI = new javax.swing.JRadioButton();
        rbCascataMestrePD = new javax.swing.JRadioButton();
        rbCascataMestrePID = new javax.swing.JRadioButton();
        rbCascataMestrePI_D = new javax.swing.JRadioButton();
        rbCascataEscravoPI_D = new javax.swing.JRadioButton();
        rbCascataEscravoPID = new javax.swing.JRadioButton();
        rbCascataEscravoPD = new javax.swing.JRadioButton();
        rbCascataEscravoPI = new javax.swing.JRadioButton();
        rbCascataEscravoP = new javax.swing.JRadioButton();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        tfCascataMestreKP = new javax.swing.JTextField();
        tfCascataMestreKI = new javax.swing.JTextField();
        tfCascataMestreKD = new javax.swing.JTextField();
        tfCascataEscravoKD = new javax.swing.JTextField();
        jLabel34 = new javax.swing.JLabel();
        tfCascataEscravoKI = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        tfCascataEscravoKP = new javax.swing.JTextField();
        jLabel36 = new javax.swing.JLabel();
        jTextIP = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextPorta = new javax.swing.JTextField();
        jBtnConectar = new javax.swing.JButton();
        jButton3_parar = new javax.swing.JButton();
        jButton4_gerarSinal = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jRBMA = new javax.swing.JRadioButton();
        jRBMF = new javax.swing.JRadioButton();
        jRadioButton_Tanque1 = new javax.swing.JRadioButton();
        jRadioButton_Tanque2 = new javax.swing.JRadioButton();
        jLabel_ExibirSaida = new javax.swing.JLabel();
        jRadioButton_ExibirAcaoIntegrativa = new javax.swing.JRadioButton();
        jRadioButton_ExibirAcaoDerivativa = new javax.swing.JRadioButton();
        jRadioButton_ExibirAcaoProporcional = new javax.swing.JRadioButton();
        jLabel_ExibirAcoes = new javax.swing.JLabel();
        btnAtualizar = new javax.swing.JButton();
        rbCascataConfig = new javax.swing.JRadioButton();

        jInternalFrame1.setVisible(true);

        javax.swing.GroupLayout jInternalFrame1Layout = new javax.swing.GroupLayout(jInternalFrame1.getContentPane());
        jInternalFrame1.getContentPane().setLayout(jInternalFrame1Layout);
        jInternalFrame1Layout.setHorizontalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jInternalFrame1Layout.setVerticalGroup(
            jInternalFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jPanel1.setPreferredSize(new java.awt.Dimension(1156, 711));

        jPanel2_sinalEnviado.setPreferredSize(new java.awt.Dimension(811, 300));

        javax.swing.GroupLayout jPanel2_sinalEnviadoLayout = new javax.swing.GroupLayout(jPanel2_sinalEnviado);
        jPanel2_sinalEnviado.setLayout(jPanel2_sinalEnviadoLayout);
        jPanel2_sinalEnviadoLayout.setHorizontalGroup(
            jPanel2_sinalEnviadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 811, Short.MAX_VALUE)
        );
        jPanel2_sinalEnviadoLayout.setVerticalGroup(
            jPanel2_sinalEnviadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jPanel3_sinalRecebido.setPreferredSize(new java.awt.Dimension(811, 300));

        javax.swing.GroupLayout jPanel3_sinalRecebidoLayout = new javax.swing.GroupLayout(jPanel3_sinalRecebido);
        jPanel3_sinalRecebido.setLayout(jPanel3_sinalRecebidoLayout);
        jPanel3_sinalRecebidoLayout.setHorizontalGroup(
            jPanel3_sinalRecebidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 811, Short.MAX_VALUE)
        );
        jPanel3_sinalRecebidoLayout.setVerticalGroup(
            jPanel3_sinalRecebidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jLabel1.setText("Sinais gerados:");

        jLabel2.setText("Niveis dos tanques:");

        jLabel7.setText("Canais de leitura:");

        jCheckBox1.setSelected(true);
        jCheckBox1.setText("Canal 0");

        jCheckBox2.setSelected(true);
        jCheckBox2.setText("Canal 1");

        jCheckBox3.setText("Canal 2");

        jCheckBox4.setText("Canal 3");

        jCheckBox5.setText("Canal 4");

        jCheckBox6.setText("Canal 5");

        jCheckBox7.setText("Canal 6");

        jLabel8.setText("Canais de escrita:");

        jRadioButton_canal0.setText("Canal 0");
        jRadioButton_canal0.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal0ActionPerformed(evt);
            }
        });

        jRadioButton_canal1.setText("Canal 1");
        jRadioButton_canal1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal1ActionPerformed(evt);
            }
        });

        jRadioButton_canal2.setText("Canal 2");
        jRadioButton_canal2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal2ActionPerformed(evt);
            }
        });

        jRadioButton_canal3.setText("Canal 3");
        jRadioButton_canal3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal3ActionPerformed(evt);
            }
        });

        jRadioButton_canal4.setText("Canal 4");
        jRadioButton_canal4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal4ActionPerformed(evt);
            }
        });

        jRadioButton_canal5.setText("Canal 5");
        jRadioButton_canal5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal5ActionPerformed(evt);
            }
        });

        jRadioButton_canal6.setText("Canal 6");
        jRadioButton_canal6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal6ActionPerformed(evt);
            }
        });

        jRadioButton_canal7.setText("Canal 7");
        jRadioButton_canal7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_canal7ActionPerformed(evt);
            }
        });

        jCheckBox8.setText("Canal 7");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jCheckBox1)
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addComponent(jCheckBox5)
                    .addComponent(jCheckBox6)
                    .addComponent(jCheckBox7)
                    .addComponent(jCheckBox8))
                .addGap(66, 66, 66)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRadioButton_canal7)
                    .addComponent(jRadioButton_canal6)
                    .addComponent(jRadioButton_canal5)
                    .addComponent(jRadioButton_canal4)
                    .addComponent(jRadioButton_canal3)
                    .addComponent(jRadioButton_canal2)
                    .addComponent(jRadioButton_canal1)
                    .addComponent(jRadioButton_canal0)
                    .addComponent(jLabel8))
                .addContainerGap(37, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox1)
                    .addComponent(jRadioButton_canal0))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox2)
                    .addComponent(jRadioButton_canal1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox3)
                    .addComponent(jRadioButton_canal2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox4)
                    .addComponent(jRadioButton_canal3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox5)
                    .addComponent(jRadioButton_canal4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox6)
                    .addComponent(jRadioButton_canal5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheckBox7)
                    .addComponent(jRadioButton_canal6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jRadioButton_canal7)
                    .addComponent(jCheckBox8))
                .addContainerGap(108, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Comunicação", jPanel5);

        tank1.setMaximum(32);
        tank1.setOrientation(1);

        tank2.setMaximum(32);
        tank2.setOrientation(1);

        jLabel20.setText("Tank1");

        jLabel21.setText("Tank2");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(tank1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                .addComponent(tank2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(61, 61, 61)
                .addComponent(jLabel20)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel21)
                .addGap(70, 70, 70))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(tank2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(tank1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(121, 121, 121))
        );

        jTabbedPane1.addTab("Tanks", jPanel2);

        jLabel9.setText("Tipos de ondas:");

        jRadioButton_degrau.setText("Degrau");
        jRadioButton_degrau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_degrauActionPerformed(evt);
            }
        });

        jRadioButton_senoidal.setText("Senoidal");
        jRadioButton_senoidal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_senoidalActionPerformed(evt);
            }
        });

        jRadioButton_quadrada.setText("Quadrada");
        jRadioButton_quadrada.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_quadradaActionPerformed(evt);
            }
        });

        jRadioButton_denteDeSerra.setText("Dente de serra");
        jRadioButton_denteDeSerra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_denteDeSerraActionPerformed(evt);
            }
        });

        jRadioButton_aleatoria.setText("Aleatória");
        jRadioButton_aleatoria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_aleatoriaActionPerformed(evt);
            }
        });

        jLabel10.setText("Amplitude");

        jTextField_amplitude.setText("0");
        jTextField_amplitude.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_amplitudeActionPerformed(evt);
            }
        });

        jLabel12.setText("Periodo");

        jTextField5_periodo.setText("30");
        jTextField5_periodo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5_periodoActionPerformed(evt);
            }
        });

        jLabel13.setText("Offset");

        jTextField6_offset.setText("0");
        jTextField6_offset.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6_offsetActionPerformed(evt);
            }
        });

        jLabel14.setText("Amplitude:");

        jLabel15.setText("Máxima");

        jLabel16.setText("Mínima");

        jTextField_amplitudeMaxima.setText("4");

        jTextField_amplitudeMinima.setText("-4");

        jLabel17.setText("Duração:");

        jLabel18.setText("Máxima");

        jTextField_duracaoMaxima.setText("40");
        jTextField_duracaoMaxima.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_duracaoMaximaActionPerformed(evt);
            }
        });

        jLabel19.setText("Mínima");

        jTextField_duracaoMinima.setText("10");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton_senoidal)
                            .addComponent(jLabel14))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel18)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField_duracaoMaxima))
                                            .addGroup(jPanel4Layout.createSequentialGroup()
                                                .addComponent(jLabel15)
                                                .addGap(18, 18, 18)
                                                .addComponent(jTextField_amplitudeMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel16)
                                            .addComponent(jLabel19))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextField_amplitudeMinima, javax.swing.GroupLayout.DEFAULT_SIZE, 39, Short.MAX_VALUE)
                                    .addComponent(jTextField_duracaoMinima)))
                            .addComponent(jRadioButton_degrau)
                            .addComponent(jRadioButton_aleatoria)
                            .addComponent(jRadioButton_denteDeSerra)
                            .addComponent(jRadioButton_quadrada)
                            .addComponent(jLabel9)
                            .addComponent(jLabel17)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel10)
                                    .addComponent(jLabel12))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jTextField5_periodo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 44, Short.MAX_VALUE)
                                    .addComponent(jTextField6_offset, javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jTextField_amplitude))))
                        .addContainerGap(106, Short.MAX_VALUE))))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRadioButton_degrau, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_senoidal, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_quadrada)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_denteDeSerra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_aleatoria)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(jTextField_amplitude, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(jTextField5_periodo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(jTextField6_offset, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(jTextField_amplitudeMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16)
                    .addComponent(jTextField_amplitudeMinima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jTextField_duracaoMaxima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(jTextField_duracaoMinima, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Sinal", jPanel4);

        jLabel11.setText("Tipos de Controle PID:");

        jRBP.setSelected(true);
        jRBP.setText("P");
        jRBP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBPActionPerformed(evt);
            }
        });

        jRBPI.setText("PI");
        jRBPI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBPIActionPerformed(evt);
            }
        });

        jRBPD.setText("PD");
        jRBPD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBPDActionPerformed(evt);
            }
        });

        jRBPID.setText("PID");
        jRBPID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBPIDActionPerformed(evt);
            }
        });

        jRBPI_D.setText("PI-D");
        jRBPI_D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBPI_DActionPerformed(evt);
            }
        });

        jLabel22.setText("Ganho Proporcional Kp:");

        jLabel23.setText("Ganho Integrativo Ki:");

        jTextKI.setEnabled(false);
        jTextKI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextKIActionPerformed(evt);
            }
        });

        jLabel24.setText("Ganho Derivativo Kd:");

        jTextKD.setEnabled(false);

        jLabel_PV.setText("PV:");

        jRadioButton_PV1.setText("Tanque 1");
        jRadioButton_PV1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_PV1ActionPerformed(evt);
            }
        });

        jRadioButton_PV2.setText("Tanque 2");
        jRadioButton_PV2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_PV2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(35, 35, 35)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jRBPI_D)
                    .addComponent(jRBPID)
                    .addComponent(jRBPD)
                    .addComponent(jRBPI)
                    .addComponent(jRBP)
                    .addComponent(jLabel11)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabel22)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTextKP, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel23)
                                .addComponent(jLabel24))
                            .addGap(18, 18, 18)
                            .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTextKD)
                                .addComponent(jTextKI))))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(jLabel_PV)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jRadioButton_PV2)
                            .addComponent(jRadioButton_PV1))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jRBP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBPI)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBPD)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBPID)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRBPI_D)
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(jTextKP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jTextKI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(jTextKD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel_PV)
                    .addComponent(jRadioButton_PV1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jRadioButton_PV2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PID", jPanel3);

        jTextField_Tr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_TrActionPerformed(evt);
            }
        });

        jTextField_Ts.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField_TsActionPerformed(evt);
            }
        });

        jLabel25.setText("Tr:");

        jLabel26.setText("Ts:");

        jLabel27.setText("Tp:");

        jLabel28.setText("Mp:");

        jComboBox_Tr.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "0 - 100 %", "5 - 95%", "10 - 90%" }));
        jComboBox_Tr.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox_TrActionPerformed(evt);
            }
        });

        jComboBox_Ts.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2%", "5%", "7%", "10%" }));

        jComboBox_Mp.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "%", "cm" }));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel25)
                    .addComponent(jLabel26)
                    .addComponent(jLabel27)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTextField_Tr, javax.swing.GroupLayout.DEFAULT_SIZE, 61, Short.MAX_VALUE)
                    .addComponent(jTextField_Ts)
                    .addComponent(jTextFieldTp)
                    .addComponent(jTextField_Mp))
                .addGap(28, 28, 28)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jComboBox_Tr, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox_Ts, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jComboBox_Mp, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Tr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(jComboBox_Tr, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Ts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel26)
                    .addComponent(jComboBox_Ts, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextFieldTp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel27))
                .addGap(18, 18, 18)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField_Mp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28)
                    .addComponent(jComboBox_Mp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(176, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Especificações", jPanel6);

        jLabel29.setText("Mestre:");

        rbCascataMestreP.setText("P");
        rbCascataMestreP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataMestrePActionPerformed(evt);
            }
        });

        rbCascataMestrePI.setText("PI");
        rbCascataMestrePI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataMestrePIActionPerformed(evt);
            }
        });

        rbCascataMestrePD.setText("PD");
        rbCascataMestrePD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataMestrePDActionPerformed(evt);
            }
        });

        rbCascataMestrePID.setText("PID");
        rbCascataMestrePID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataMestrePIDActionPerformed(evt);
            }
        });

        rbCascataMestrePI_D.setText("PI-D");
        rbCascataMestrePI_D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataMestrePI_DActionPerformed(evt);
            }
        });

        rbCascataEscravoPI_D.setText("PI-D");
        rbCascataEscravoPI_D.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataEscravoPI_DActionPerformed(evt);
            }
        });

        rbCascataEscravoPID.setText("PID");
        rbCascataEscravoPID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataEscravoPIDActionPerformed(evt);
            }
        });

        rbCascataEscravoPD.setText("PD");
        rbCascataEscravoPD.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataEscravoPDActionPerformed(evt);
            }
        });

        rbCascataEscravoPI.setText("PI");
        rbCascataEscravoPI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataEscravoPIActionPerformed(evt);
            }
        });

        rbCascataEscravoP.setText("P");
        rbCascataEscravoP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataEscravoPActionPerformed(evt);
            }
        });

        jLabel30.setText("Escravo:");

        jLabel31.setText("Kp:");

        jLabel32.setText("Ki:");

        jLabel33.setText("Kd:");

        tfCascataMestreKP.setText("2");

        tfCascataMestreKI.setText("0.05");

        tfCascataMestreKD.setText("0.005");

        tfCascataEscravoKD.setText("0.005");

        jLabel34.setText("Kd:");

        tfCascataEscravoKI.setText("0.05");

        jLabel35.setText("Ki:");

        tfCascataEscravoKP.setText("2");
        tfCascataEscravoKP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tfCascataEscravoKPActionPerformed(evt);
            }
        });

        jLabel36.setText("Kp:");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel33)
                            .addComponent(jLabel32)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataMestreKD, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCascataMestreKI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCascataMestreKP, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(rbCascataMestrePI)
                                .addComponent(rbCascataMestrePD)
                                .addComponent(rbCascataMestrePI_D)
                                .addComponent(rbCascataMestreP)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel29)
                                    .addComponent(rbCascataMestrePID))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel34)
                            .addComponent(jLabel35)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataEscravoKD, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCascataEscravoKI, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tfCascataEscravoKP, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(147, 147, 147)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(rbCascataEscravoPI)
                            .addComponent(rbCascataEscravoPD)
                            .addComponent(rbCascataEscravoPI_D)
                            .addComponent(rbCascataEscravoP)
                            .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel30)
                                .addComponent(rbCascataEscravoPID)))))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbCascataEscravoP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataEscravoPI)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataEscravoPD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataEscravoPID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataEscravoPI_D))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(rbCascataMestreP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataMestrePI)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataMestrePD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataMestrePID)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(rbCascataMestrePI_D)))
                .addGap(18, 18, 18)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataMestreKP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel31))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataMestreKI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel32))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataMestreKD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel33)))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataEscravoKP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel36))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataEscravoKI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel35))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                            .addComponent(tfCascataEscravoKD, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel34))))
                .addContainerGap(50, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("PID - CASCATA", jPanel7);

        jTextIP.setText("10.13.99.69");
        jTextIP.setEnabled(false);
        jTextIP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextIPActionPerformed(evt);
            }
        });

        jLabel3.setText("Conexão:");

        jLabel4.setText("IP:");

        jLabel5.setText("Porta:");

        jTextPorta.setText("20081");
        jTextPorta.setToolTipText("");
        jTextPorta.setEnabled(false);
        jTextPorta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextPortaActionPerformed(evt);
            }
        });

        jBtnConectar.setText("Conectar");
        jBtnConectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jBtnConectarActionPerformed(evt);
            }
        });

        jButton3_parar.setText("Parar");
        jButton3_parar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3_pararActionPerformed(evt);
            }
        });

        jButton4_gerarSinal.setText("Gerar Sinal");
        jButton4_gerarSinal.setPreferredSize(new java.awt.Dimension(59, 23));
        jButton4_gerarSinal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4_gerarSinalActionPerformed(evt);
            }
        });

        jLabel6.setText("Configurações");

        jRBMA.setText("Malha Aberta");
        jRBMA.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBMAActionPerformed(evt);
            }
        });

        jRBMF.setText("Malha Fechada");
        jRBMF.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRBMFActionPerformed(evt);
            }
        });

        jRadioButton_Tanque1.setText("Tanque 1");

        jRadioButton_Tanque2.setText("Tanque 2");

        jLabel_ExibirSaida.setText("Exibir:");

        jRadioButton_ExibirAcaoIntegrativa.setText("I");
        jRadioButton_ExibirAcaoIntegrativa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButton_ExibirAcaoIntegrativaActionPerformed(evt);
            }
        });

        jRadioButton_ExibirAcaoDerivativa.setText("D");

        jRadioButton_ExibirAcaoProporcional.setText("P");

        jLabel_ExibirAcoes.setText("Exibir Ações:");

        btnAtualizar.setText("Atualizar");
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        rbCascataConfig.setText("Cascata");
        rbCascataConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rbCascataConfigActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel2_sinalEnviado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel_ExibirAcoes)
                                .addGap(18, 18, 18)
                                .addComponent(jRadioButton_ExibirAcaoProporcional)
                                .addGap(28, 28, 28)
                                .addComponent(jRadioButton_ExibirAcaoDerivativa)
                                .addGap(26, 26, 26)
                                .addComponent(jRadioButton_ExibirAcaoIntegrativa)
                                .addGap(48, 48, 48))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel3_sinalRecebido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel_ExibirSaida)
                        .addGap(30, 30, 30)
                        .addComponent(jRadioButton_Tanque1)
                        .addGap(18, 18, 18)
                        .addComponent(jRadioButton_Tanque2)
                        .addGap(50, 50, 50)))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel5)
                                    .addComponent(jLabel4))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTextPorta, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                                    .addComponent(jTextIP)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addGap(8, 8, 8)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jRBMF)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(btnAtualizar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(jButton4_gerarSinal, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))
                                                .addGap(39, 39, 39))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jRBMA)
                                                .addGap(35, 35, 35)))
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(rbCascataConfig)
                                            .addComponent(jBtnConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jButton3_parar, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                        .addGap(22, 22, 22))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 326, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel6)
                    .addComponent(jRadioButton_ExibirAcaoIntegrativa)
                    .addComponent(jRadioButton_ExibirAcaoDerivativa)
                    .addComponent(jRadioButton_ExibirAcaoProporcional)
                    .addComponent(jLabel_ExibirAcoes))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 431, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jRBMA)
                            .addComponent(rbCascataConfig))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(91, 91, 91)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel3)
                                    .addComponent(jBtnConectar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jRBMF)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jButton4_gerarSinal, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jButton3_parar, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnAtualizar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextIP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jTextPorta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jPanel2_sinalEnviado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jRadioButton_Tanque1)
                                .addComponent(jRadioButton_Tanque2)
                                .addComponent(jLabel_ExibirSaida)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3_sinalRecebido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(27, Short.MAX_VALUE))
        );

        jPanel2_sinalEnviado.getAccessibleContext().setAccessibleName("Sinal");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel1.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTextIPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextIPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextIPActionPerformed

    private void jTextPortaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextPortaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextPortaActionPerformed

    //Ao pressionar o botão parar, os graficos deixarao de ser gerados, pois a flag passa a ser falsa, a conexão com os tanques tb é cancelada.
    private void jButton3_pararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3_pararActionPerformed

        jButton4_gerarSinal.setEnabled(true);
        flag = false;
        setConectado(false);
        
        tempo=0;
        //limpa todos os XYdatasets para que o grafico volte pra o começo
        acao_proporcional.clear();
        acao_integral.clear();
        acao_derivativa.clear();
        entrada_sinal.clear();
        sinal_enviado.clear();
        sinal_recebidoTanque1.clear();
        sinal_recebidoTanque2.clear();
        referencia_nivel.clear();

    }//GEN-LAST:event_jButton3_pararActionPerformed

    private void jButton4_gerarSinalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4_gerarSinalActionPerformed

        //verifica se as entradas sao numeros e passa o valor
        set_entradas();
        //se a flag for falsa, inicia os graficos, essa flag é usada para que as threads que geram os gráficos fiquem em um loop infinito enquando for true
        if (!flag) {
            flag = true;
            init_graph();
            init_graph_saida();
        }
        //frequencia = Double.parseDouble(jTextField4_frequência.getText());

        //init_graph_saida();
        //jButton4_gerarSinal.setEnabled(false);
    }//GEN-LAST:event_jButton4_gerarSinalActionPerformed

    private void jBtnConectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jBtnConectarActionPerformed

        try{
        this.ip = jTextIP.getText();
        this.porta = Integer.parseInt(jTextPorta.getText());
        } catch(NumberFormatException ex){
            System.err.println("Insira IP e Porta");
        }
        setConectado(true);
    }//GEN-LAST:event_jBtnConectarActionPerformed

    private void jTextField_duracaoMaximaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_duracaoMaximaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_duracaoMaximaActionPerformed

    private void jTextField_amplitudeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_amplitudeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_amplitudeActionPerformed

    private void jRadioButton_aleatoriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_aleatoriaActionPerformed
        jRadioButton_degrau.setSelected(false);
        jRadioButton_senoidal.setSelected(false);
        jRadioButton_quadrada.setSelected(false);
        jRadioButton_denteDeSerra.setSelected(false);

        jTextField5_periodo.setEnabled(false);
        jTextField_amplitude.setEnabled(false);
        jTextField6_offset.setEnabled(false);
        jTextField_amplitudeMaxima.setEnabled(true);
        jTextField_amplitudeMinima.setEnabled(true);
        jTextField_duracaoMaxima.setEnabled(true);
        jTextField_duracaoMinima.setEnabled(true);
    }//GEN-LAST:event_jRadioButton_aleatoriaActionPerformed

    private void jRadioButton_denteDeSerraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_denteDeSerraActionPerformed
        jRadioButton_degrau.setSelected(false);
        jRadioButton_senoidal.setSelected(false);
        jRadioButton_quadrada.setSelected(false);
        jRadioButton_aleatoria.setSelected(false);

        jTextField5_periodo.setEnabled(true);
        jTextField_amplitude.setEnabled(true);
        jTextField6_offset.setEnabled(true);
        jTextField_amplitudeMaxima.setEnabled(false);
        jTextField_amplitudeMinima.setEnabled(false);
        jTextField_duracaoMaxima.setEnabled(false);
        jTextField_duracaoMinima.setEnabled(false);
    }//GEN-LAST:event_jRadioButton_denteDeSerraActionPerformed

    private void jRadioButton_quadradaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_quadradaActionPerformed
        jRadioButton_degrau.setSelected(false);
        jRadioButton_senoidal.setSelected(false);
        jRadioButton_denteDeSerra.setSelected(false);
        jRadioButton_aleatoria.setSelected(false);

        jTextField5_periodo.setEnabled(true);
        jTextField_amplitude.setEnabled(true);
        jTextField6_offset.setEnabled(true);
        jTextField_amplitudeMaxima.setEnabled(false);
        jTextField_amplitudeMinima.setEnabled(false);
        jTextField_duracaoMaxima.setEnabled(false);
        jTextField_duracaoMinima.setEnabled(false);
    }//GEN-LAST:event_jRadioButton_quadradaActionPerformed

    private void jRadioButton_senoidalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_senoidalActionPerformed
        jRadioButton_degrau.setSelected(false);
        jRadioButton_quadrada.setSelected(false);
        jRadioButton_denteDeSerra.setSelected(false);
        jRadioButton_aleatoria.setSelected(false);

        jTextField5_periodo.setEnabled(true);
        jTextField_amplitude.setEnabled(true);
        jTextField6_offset.setEnabled(true);
        jTextField_amplitudeMaxima.setEnabled(false);
        jTextField_amplitudeMinima.setEnabled(false);
        jTextField_duracaoMaxima.setEnabled(false);
        jTextField_duracaoMinima.setEnabled(false);
    }//GEN-LAST:event_jRadioButton_senoidalActionPerformed

    private void jRadioButton_degrauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_degrauActionPerformed

        jRadioButton_senoidal.setSelected(false);
        jRadioButton_quadrada.setSelected(false);
        jRadioButton_denteDeSerra.setSelected(false);
        jRadioButton_aleatoria.setSelected(false);
        
        jTextField_amplitude.setEnabled(true);
        jTextField5_periodo.setEnabled(false);
         jTextField6_offset.setEnabled(false);
        jTextField_amplitudeMaxima.setEnabled(false);
        jTextField_amplitudeMinima.setEnabled(false);
        jTextField_duracaoMaxima.setEnabled(false);
        jTextField_duracaoMinima.setEnabled(false);

    }//GEN-LAST:event_jRadioButton_degrauActionPerformed

    private void jRadioButton_canal7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal7ActionPerformed
        jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        // jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal7ActionPerformed

    private void jRadioButton_canal6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal6ActionPerformed
        jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        // jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal6ActionPerformed

    private void jRadioButton_canal5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal5ActionPerformed
        jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        //  jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal5ActionPerformed

    private void jRadioButton_canal4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal4ActionPerformed
        jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        // jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal4ActionPerformed

    private void jRadioButton_canal3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal3ActionPerformed
        jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        //jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal3ActionPerformed

    private void jRadioButton_canal2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal2ActionPerformed
        jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        // jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal2ActionPerformed

    private void jRadioButton_canal1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal1ActionPerformed
        jRadioButton_canal0.setSelected(false);
        // jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal1ActionPerformed

    private void jRadioButton_canal0ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_canal0ActionPerformed
        //jRadioButton_canal0.setSelected(false);
        jRadioButton_canal1.setSelected(false);
        jRadioButton_canal2.setSelected(false);
        jRadioButton_canal3.setSelected(false);
        jRadioButton_canal4.setSelected(false);
        jRadioButton_canal5.setSelected(false);
        jRadioButton_canal6.setSelected(false);
        jRadioButton_canal7.setSelected(false);
    }//GEN-LAST:event_jRadioButton_canal0ActionPerformed

    private void jTextField6_offsetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6_offsetActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6_offsetActionPerformed

    private void jRBMAActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBMAActionPerformed
        // TODO add your handling code here:
        cascata = false;
        setMalha(false);
        jRBMA.setSelected(true);
        jRBMF.setSelected(false);
        rbCascataConfig.setSelected(false);
        
        jRadioButton_PV2.setEnabled(false);
        jRadioButton_PV1.setEnabled(false);
        
        jRadioButton_ExibirAcaoDerivativa.setSelected(false);
        jRadioButton_ExibirAcaoIntegrativa.setSelected(false);
        jRadioButton_ExibirAcaoProporcional.setSelected(false);
        jRadioButton_ExibirAcaoDerivativa.setEnabled(false);
        jRadioButton_ExibirAcaoIntegrativa.setEnabled(false);
        jRadioButton_ExibirAcaoProporcional.setEnabled(false);


    }//GEN-LAST:event_jRBMAActionPerformed

    private void jRBMFActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBMFActionPerformed
        // TODO add your handling code here:
        cascata = false;
        setMalha(true);
        
       // amplitude=amplitude/6.25;
        jRBMF.setSelected(true);
        jRBMA.setSelected(false);
        rbCascataConfig.setSelected(false);
        
        jRadioButton_PV2.setEnabled(true);
        jRadioButton_PV1.setEnabled(true);
        
        jRadioButton_ExibirAcaoDerivativa.setEnabled(true);
        jRadioButton_ExibirAcaoIntegrativa.setEnabled(true);
        jRadioButton_ExibirAcaoProporcional.setEnabled(true);
        jRadioButton_ExibirAcaoDerivativa.setSelected(true);
        jRadioButton_ExibirAcaoIntegrativa.setSelected(true);
        jRadioButton_ExibirAcaoProporcional.setSelected(true);

    }//GEN-LAST:event_jRBMFActionPerformed

    private void jTextKIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextKIActionPerformed
        // TODO ad your handling code here:
       
    }//GEN-LAST:event_jTextKIActionPerformed

    private void jRBPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBPActionPerformed
        // TODO add your handling code here:
        jRBPI.setSelected(false);
        jRBPD.setSelected(false);
        jRBPID.setSelected(false);
        jRBPI_D.setSelected(false);
        
        jTextKD.setEnabled(false);
        jTextKI.setEnabled(false);
        controle = 1;
    }//GEN-LAST:event_jRBPActionPerformed

    private void jRBPIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBPIActionPerformed
        // TODO add your handling code here:
         jRBP.setSelected(false);
        jRBPD.setSelected(false);
        jRBPID.setSelected(false);
        jRBPI_D.setSelected(false);
        
        jTextKI.setEnabled(true);
        jTextKD.setEnabled(false);
        controle = 2;
        
    }//GEN-LAST:event_jRBPIActionPerformed

    private void jRBPDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBPDActionPerformed
        // TODO add your handling code here:
        jRBPI.setSelected(false);
        jRBP.setSelected(false);
        jRBPID.setSelected(false);
        jRBPI_D.setSelected(false);
        
        
        jTextKD.setEnabled(true);
        jTextKI.setEnabled(false);
        controle = 3;
        
    }//GEN-LAST:event_jRBPDActionPerformed

    private void jRBPIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBPIDActionPerformed
        // TODO add your handling code here:
        jRBPI.setSelected(false);
        jRBP.setSelected(false);
        jRBPD.setSelected(false);
        jRBPI_D.setSelected(false);
        
        
        jTextKD.setEnabled(true);
        jTextKI.setEnabled(true);
        controle = 4;
    }//GEN-LAST:event_jRBPIDActionPerformed

    private void jRBPI_DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRBPI_DActionPerformed
        // TODO add your handling code here:
         jRBPI.setSelected(false);
        jRBP.setSelected(false);
        jRBPD.setSelected(false);
        jRBPID.setSelected(false);
        
        
        jTextKD.setEnabled(true);
        jTextKI.setEnabled(true);
        controle = 5;
    }//GEN-LAST:event_jRBPI_DActionPerformed

    private void jRadioButton_ExibirAcaoIntegrativaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_ExibirAcaoIntegrativaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jRadioButton_ExibirAcaoIntegrativaActionPerformed

    private void jRadioButton_PV1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_PV1ActionPerformed
        jRadioButton_PV2.setSelected(false);
        jRadioButton_PV1.setEnabled(false);
        jRadioButton_PV2.setEnabled(true);
        
    }//GEN-LAST:event_jRadioButton_PV1ActionPerformed

    private void jRadioButton_PV2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jRadioButton_PV2ActionPerformed
        jRadioButton_PV1.setSelected(false);
        jRadioButton_PV1.setEnabled(true);
        jRadioButton_PV2.setEnabled(false);
    }//GEN-LAST:event_jRadioButton_PV2ActionPerformed

    private void jTextField_TrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_TrActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_TrActionPerformed

    private void jTextField_TsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField_TsActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField_TsActionPerformed

    private void jComboBox_TrActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox_TrActionPerformed
      
    }//GEN-LAST:event_jComboBox_TrActionPerformed

    private void jTextField5_periodoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5_periodoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5_periodoActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
        set_entradas();
    }//GEN-LAST:event_btnAtualizarActionPerformed

    private void rbCascataConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataConfigActionPerformed
        // TODO add your handling code here:
        jRBMA.setSelected(false);
        jRBMF.setSelected(false);
        
        jRadioButton_ExibirAcaoDerivativa.setEnabled(true);
        jRadioButton_ExibirAcaoIntegrativa.setEnabled(true);
        jRadioButton_ExibirAcaoProporcional.setEnabled(true);
        jRadioButton_ExibirAcaoDerivativa.setSelected(true);
        jRadioButton_ExibirAcaoIntegrativa.setSelected(true);
        jRadioButton_ExibirAcaoProporcional.setSelected(true);
        cascata = true;
        setMalha(true);
    }//GEN-LAST:event_rbCascataConfigActionPerformed

    private void rbCascataMestrePActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataMestrePActionPerformed
        // TODO add your handling code here:
        rbCascataMestrePD.setSelected(false);
        rbCascataMestrePI.setSelected(false);
        rbCascataMestrePID.setSelected(false);
        rbCascataMestrePI_D.setSelected(false);
        
        tfCascataMestreKI.setEnabled(false);
        tfCascataMestreKD.setEnabled(false);
        controleCMestre = 1;
        
    }//GEN-LAST:event_rbCascataMestrePActionPerformed

    private void rbCascataMestrePIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataMestrePIActionPerformed
        // TODO add your handling code here:
        rbCascataMestrePD.setSelected(false);
        rbCascataMestreP.setSelected(false);
        rbCascataMestrePID.setSelected(false);
        rbCascataMestrePI_D.setSelected(false);
        
        tfCascataMestreKI.setEnabled(true);
        tfCascataMestreKD.setEnabled(false);
        controleCMestre = 2;
    }//GEN-LAST:event_rbCascataMestrePIActionPerformed

    private void rbCascataMestrePDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataMestrePDActionPerformed
        // TODO add your handling code here:
        rbCascataMestrePI.setSelected(false);
        rbCascataMestreP.setSelected(false);
        rbCascataMestrePID.setSelected(false);
        rbCascataMestrePI_D.setSelected(false);
        
        tfCascataMestreKI.setEnabled(false);
        tfCascataMestreKD.setEnabled(true);
        controleCMestre = 3;
    }//GEN-LAST:event_rbCascataMestrePDActionPerformed

    private void rbCascataMestrePIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataMestrePIDActionPerformed
        // TODO add your handling code here:
         rbCascataMestrePI.setSelected(false);
        rbCascataMestreP.setSelected(false);
        rbCascataMestrePD.setSelected(false);
        rbCascataMestrePI_D.setSelected(false);
        
        tfCascataMestreKI.setEnabled(true);
        tfCascataMestreKD.setEnabled(true);
        controleCMestre = 4;
    }//GEN-LAST:event_rbCascataMestrePIDActionPerformed

    private void rbCascataMestrePI_DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataMestrePI_DActionPerformed
        // TODO add your handling code here:
        rbCascataMestrePI.setSelected(false);
        rbCascataMestreP.setSelected(false);
        rbCascataMestrePD.setSelected(false);
        rbCascataMestrePID.setSelected(false);
        
        tfCascataMestreKI.setEnabled(true);
        tfCascataMestreKD.setEnabled(true);
        controleCMestre = 5;
    }//GEN-LAST:event_rbCascataMestrePI_DActionPerformed

    private void rbCascataEscravoPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataEscravoPActionPerformed
        // TODO add your handling code here:
        rbCascataEscravoPI.setSelected(false);
        rbCascataEscravoPI_D.setSelected(false);
        rbCascataEscravoPD.setSelected(false);
        rbCascataEscravoPID.setSelected(false);
        
        tfCascataEscravoKI.setEnabled(false);
        tfCascataEscravoKD.setEnabled(false);
        
        controleCEscravo = 1;
    }//GEN-LAST:event_rbCascataEscravoPActionPerformed

    private void rbCascataEscravoPIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataEscravoPIActionPerformed
        // TODO add your handling code here:
        rbCascataEscravoP.setSelected(false);
        rbCascataEscravoPI_D.setSelected(false);
        rbCascataEscravoPD.setSelected(false);
        rbCascataEscravoPID.setSelected(false);
        
        tfCascataEscravoKI.setEnabled(true);
        tfCascataEscravoKD.setEnabled(false);
        
        controleCEscravo = 2;
    }//GEN-LAST:event_rbCascataEscravoPIActionPerformed

    private void rbCascataEscravoPDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataEscravoPDActionPerformed
        // TODO add your handling code here:
        rbCascataEscravoPI.setSelected(false);
        rbCascataEscravoPI_D.setSelected(false);
        rbCascataEscravoP.setSelected(false);
        rbCascataEscravoPID.setSelected(false);
        
        tfCascataEscravoKI.setEnabled(false);
        tfCascataEscravoKD.setEnabled(true);
        
        controleCEscravo = 3;
    }//GEN-LAST:event_rbCascataEscravoPDActionPerformed

    private void rbCascataEscravoPIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataEscravoPIDActionPerformed
        // TODO add your handling code here:
        rbCascataEscravoPI.setSelected(false);
        rbCascataEscravoPI_D.setSelected(false);
        rbCascataEscravoPD.setSelected(false);
        rbCascataEscravoP.setSelected(false);
        
        tfCascataEscravoKI.setEnabled(true);
        tfCascataEscravoKD.setEnabled(true);
        
        controleCEscravo = 4;
    }//GEN-LAST:event_rbCascataEscravoPIDActionPerformed

    private void rbCascataEscravoPI_DActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rbCascataEscravoPI_DActionPerformed
        // TODO add your handling code here:
        rbCascataEscravoPI.setSelected(false);
        rbCascataEscravoP.setSelected(false);
        rbCascataEscravoPD.setSelected(false);
        rbCascataEscravoPID.setSelected(false);
        
        tfCascataEscravoKI.setEnabled(true);
        tfCascataEscravoKD.setEnabled(true);
        
        controleCEscravo = 5;
    }//GEN-LAST:event_rbCascataEscravoPI_DActionPerformed

    private void tfCascataEscravoKPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tfCascataEscravoKPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfCascataEscravoKPActionPerformed

    public JTextField getjTextFieldTp() {
        return jTextFieldTp;
    }

    public void setjTextFieldTp(JTextField jTextFieldTp) {
        this.jTextFieldTp = jTextFieldTp;
    }

    public JTextField getjTextField_Mp() {
        return jTextField_Mp;
    }

    public void setjTextField_Mp(JTextField jTextField_Mp) {
        this.jTextField_Mp = jTextField_Mp;
    }

    public JTextField getjTextField_Tr() {
        return jTextField_Tr;
    }

    public void setjTextField_Tr(JTextField jTextField_Tr) {
        this.jTextField_Tr = jTextField_Tr;
    }

    public JTextField getjTextField_Ts() {
        return jTextField_Ts;
    }

    public void setjTextField_Ts(JTextField jTextField_Ts) {
        this.jTextField_Ts = jTextField_Ts;
    }

    
    
    public JRadioButton getjRadioButton_PV1() {
        return jRadioButton_PV1;
    }

    public void setjRadioButton_PV1(JRadioButton jRadioButton_PV1) {
        this.jRadioButton_PV1 = jRadioButton_PV1;
    }

    public JRadioButton getjRadioButton_PV2() {
        return jRadioButton_PV2;
    }

    public void setjRadioButton_PV2(JRadioButton jRadioButton_PV2) {
        this.jRadioButton_PV2 = jRadioButton_PV2;
    }

    public JComboBox<String> getjComboBox_Mp() {
        return jComboBox_Mp;
    }

    public void setjComboBox_Mp(JComboBox<String> jComboBox_Mp) {
        this.jComboBox_Mp = jComboBox_Mp;
    }

    public JComboBox<String> getjComboBox_Tr() {
        return jComboBox_Tr;
    }

    public void setjComboBox_Tr(JComboBox<String> jComboBox_Tr) {
        this.jComboBox_Tr = jComboBox_Tr;
    }

    public JComboBox<String> getjComboBox_Ts() {
        return jComboBox_Ts;
    }

    public void setjComboBox_Ts(JComboBox<String> jComboBox_Ts) {
        this.jComboBox_Ts = jComboBox_Ts;
    }
    
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Painel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Painel().setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton jBtnConectar;
    private javax.swing.JButton jButton3_parar;
    private javax.swing.JButton jButton4_gerarSinal;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBox jCheckBox6;
    private javax.swing.JCheckBox jCheckBox7;
    private javax.swing.JCheckBox jCheckBox8;
    private javax.swing.JComboBox<String> jComboBox_Mp;
    private javax.swing.JComboBox<String> jComboBox_Tr;
    private javax.swing.JComboBox<String> jComboBox_Ts;
    private javax.swing.JInternalFrame jInternalFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JLabel jLabel_ExibirAcoes;
    private javax.swing.JLabel jLabel_ExibirSaida;
    private javax.swing.JLabel jLabel_PV;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel2_sinalEnviado;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel3_sinalRecebido;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButton jRBMA;
    private javax.swing.JRadioButton jRBMF;
    private javax.swing.JRadioButton jRBP;
    private javax.swing.JRadioButton jRBPD;
    private javax.swing.JRadioButton jRBPI;
    private javax.swing.JRadioButton jRBPID;
    private javax.swing.JRadioButton jRBPI_D;
    private javax.swing.JRadioButton jRadioButton_ExibirAcaoDerivativa;
    private javax.swing.JRadioButton jRadioButton_ExibirAcaoIntegrativa;
    private javax.swing.JRadioButton jRadioButton_ExibirAcaoProporcional;
    private javax.swing.JRadioButton jRadioButton_PV1;
    private javax.swing.JRadioButton jRadioButton_PV2;
    private javax.swing.JRadioButton jRadioButton_Tanque1;
    private javax.swing.JRadioButton jRadioButton_Tanque2;
    private javax.swing.JRadioButton jRadioButton_aleatoria;
    private javax.swing.JRadioButton jRadioButton_canal0;
    private javax.swing.JRadioButton jRadioButton_canal1;
    private javax.swing.JRadioButton jRadioButton_canal2;
    private javax.swing.JRadioButton jRadioButton_canal3;
    private javax.swing.JRadioButton jRadioButton_canal4;
    private javax.swing.JRadioButton jRadioButton_canal5;
    private javax.swing.JRadioButton jRadioButton_canal6;
    private javax.swing.JRadioButton jRadioButton_canal7;
    private javax.swing.JRadioButton jRadioButton_degrau;
    private javax.swing.JRadioButton jRadioButton_denteDeSerra;
    private javax.swing.JRadioButton jRadioButton_quadrada;
    private javax.swing.JRadioButton jRadioButton_senoidal;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField5_periodo;
    private javax.swing.JTextField jTextField6_offset;
    private javax.swing.JTextField jTextFieldTp;
    private javax.swing.JTextField jTextField_Mp;
    private javax.swing.JTextField jTextField_Tr;
    private javax.swing.JTextField jTextField_Ts;
    private javax.swing.JTextField jTextField_amplitude;
    private javax.swing.JTextField jTextField_amplitudeMaxima;
    private javax.swing.JTextField jTextField_amplitudeMinima;
    private javax.swing.JTextField jTextField_duracaoMaxima;
    private javax.swing.JTextField jTextField_duracaoMinima;
    private javax.swing.JTextField jTextIP;
    private javax.swing.JTextField jTextKD;
    private javax.swing.JTextField jTextKI;
    private javax.swing.JTextField jTextKP;
    private javax.swing.JTextField jTextPorta;
    private javax.swing.JRadioButton rbCascataConfig;
    private javax.swing.JRadioButton rbCascataEscravoP;
    private javax.swing.JRadioButton rbCascataEscravoPD;
    private javax.swing.JRadioButton rbCascataEscravoPI;
    private javax.swing.JRadioButton rbCascataEscravoPID;
    private javax.swing.JRadioButton rbCascataEscravoPI_D;
    private javax.swing.JRadioButton rbCascataMestreP;
    private javax.swing.JRadioButton rbCascataMestrePD;
    private javax.swing.JRadioButton rbCascataMestrePI;
    private javax.swing.JRadioButton rbCascataMestrePID;
    private javax.swing.JRadioButton rbCascataMestrePI_D;
    private javax.swing.JProgressBar tank1;
    private javax.swing.JProgressBar tank2;
    private javax.swing.JTextField tfCascataEscravoKD;
    private javax.swing.JTextField tfCascataEscravoKI;
    private javax.swing.JTextField tfCascataEscravoKP;
    private javax.swing.JTextField tfCascataMestreKD;
    private javax.swing.JTextField tfCascataMestreKI;
    private javax.swing.JTextField tfCascataMestreKP;
    // End of variables declaration//GEN-END:variables

}
