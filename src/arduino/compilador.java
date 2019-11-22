/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package arduino;

import com.sun.awt.AWTUtilities;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author elomj
 */
public class compilador extends javax.swing.JFrame {

    /**
     * Creates new form compilador
     */
    String cadena = "";

    ArrayList<String> pines = new ArrayList<>();
    ArrayList<String> pinesA = new ArrayList<>();
    static int ubi = 0;
    static String codigoC = "";
    static String inicializar = "";
    static String mSetup = "";
    static String mLoop = "";
    static String mLeds = "";
    static String mServo = "";
    static String mSonido = "";
    static String mMotor = "";
    static String mApagar = "";
    static char[] c = null;

    public compilador() {
        this.setUndecorated(true);
        initComponents();

        AWTUtilities.setWindowOpaque(this, false);
        this.setLocationRelativeTo(null);

        izquierda.setSelected(true);
        reloj.setSelected(true);

        desabilitar();
        pines();

        for (String campos : pines) {
            selectPinServo.addItem(campos);
        }

        for (String campos : pinesA) {
            selectPinSlider.addItem(campos);
        }

    }

    private void pines() {

        pines.add("");
        pines.add("P2");
        pines.add("P3");
        pines.add("P4");
        pines.add("P5");
        pines.add("P6");
        pines.add("P7");
        pines.add("P8");
        pines.add("P9");
        pines.add("P10");
        pines.add("P11");
        pines.add("P12");
        pines.add("P13");

        pinesA.add("A0");
        pinesA.add("A1");
        pinesA.add("A2");
        pinesA.add("A3");
        pinesA.add("A4");
        pinesA.add("A5");

    }

    private void desabilitar() {

        panelMotor.setVisible(false);
        panelServo.setVisible(false);
        panelLedsServo.setVisible(false);
        panelLedsMotor.setVisible(false);
        panelBuzzer.setVisible(false);
        panelFin.setVisible(false);
        panelExito.setVisible(false);

        selectPin2Servo.setVisible(false);
        selectPin3Servo.setVisible(false);
        selectPin4Servo.setVisible(false);
        ledsTerminadosServo.setVisible(false);

        selectPin2Motor.setVisible(false);
        selectPin3Motor.setVisible(false);
        selectPin4Motor.setVisible(false);
        ledsMotorTerminado.setVisible(false);

        servoTerminado.setVisible(false);
        buzzerTerminado.setVisible(false);
        generarCodigo.setVisible(false);
        selectPinApagar.setVisible(false);
        
        btnHome.setVisible(false);

    }

    private void eliminar_posicion(String pin) {

        //ELIMINAR PIN
        int posicion = -1;
        for (int i = 0; i < pines.size(); i++) {
            if (pines.get(i) == pin) {
                posicion = i;
            }
        }
        pines.remove(posicion);

    }

    //REVISAR CADENA ___________________________________________________________
    private void revisarCadena() throws IOException {

        c = cadena.toCharArray();

        inicializar = "//" + cadena + "\n";

        //SERVO O MOTOR
        if (servoOMotor()) {

            gradosServo();
            direccionServo();
            pinServo();

        } else {

            direccionMotor();
            pinMotor();

        }

        pinLed(1);
        pinLed(2);
        pinLed(3);
        pinLed(4);

        pinBuzzer();
        herciosBuzzer();

        //FINALES
        pinLed(5);
        pulsador();
        pinSlider();

        finMetodos();

        codigoC = inicializar + mSetup + mLoop + mServo + mMotor + mLeds + mSonido + mApagar;

        String ruta = "/" + cadena + ".ino";
        File file = new File(ruta);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(codigoC);
        bw.close();
        return;

    }

    private boolean servoOMotor() {

        boolean camino = false;

        if (c[ubi] == 'S') {

            inicializar += ""
                    + "#include <Servo.h>\n"
                    + "Servo myservo;\n";

            mSetup = ""
                    + "void setup()\n"
                    + "{\n"
                    + "myservo.attach(servo);\n"
                    + "pinMode(led1, OUTPUT);\n"
                    + "pinMode(led2, OUTPUT);\n"
                    + "pinMode(led3, OUTPUT);\n"
                    + "pinMode(led4, OUTPUT);\n"
                    + "pinMode(led5, OUTPUT);\n"
                    + "pinMode(pulsador, INPUT);\n"
                    + "}\n";

            mLoop += ""
                    + "void loop()\n"
                    + "{\n"
                    + "grados = analogRead(slider);\n"
                    + "servoM();\n"
                    + "leds();\n"
                    + "sonido();\n"
                    + "apagar();\n"
                    + "}\n";

            mLeds += ""
                    + "void leds()\n"
                    + "{\n"
                    + "if(grados>=(grado*3)/4){\n"
                    + "digitalWrite(led4, HIGH);\n"
                    + "digitalWrite(led3, HIGH);\n"
                    + "digitalWrite(led2, HIGH);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else if(grados>=grado/2){\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, HIGH);\n"
                    + "digitalWrite(led2, HIGH);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else if(grados>=grado/4){\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, LOW);\n"
                    + "digitalWrite(led2, HIGH);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else if(grados>0){\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, LOW);\n"
                    + "digitalWrite(led2, LOW);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else{\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, LOW);\n"
                    + "digitalWrite(led2, LOW);\n"
                    + "digitalWrite(led1, LOW);\n"
                    + "}\n"
                    + "}\n";

            mSonido += ""
                    + "void sonido(){\n"
                    + "tone(buzzer, (((grados*push)+(hercio*push))));\n"
                    + "}\n";

            c[ubi] = 'X';
            ubi += 1;
            camino = true;

        } else if (c[ubi] == 'M') {

            mSetup = ""
                    + "void setup()\n"
                    + "{\n"
                    + "pinMode( direccion, OUTPUT);\n"
                    + "pinMode(led1, OUTPUT);\n"
                    + "pinMode(led2, OUTPUT);\n"
                    + "pinMode(led3, OUTPUT);\n"
                    + "pinMode(led4, OUTPUT);\n"
                    + "pinMode(led5,OUTPUT);\n"
                    + "pinMode(pulsador,INPUT);\n"
                    + "}\n";

            mLoop += ""
                    + "void loop()\n"
                    + "{\n"
                    + "slider1 = analogRead(slider);\n"
                    + "slider1 = map(slider1,0,1023,0,180);\n"
                    + "leds();\n"
                    + "sonido();\n"
                    + "motor();\n"
                    + "apagar();\n"
                    + "}\n";

            mLeds += ""
                    + "void leds()\n"
                    + "{\n"
                    + "if(slider1>=(180*3)/4){\n"
                    + "digitalWrite(led4, HIGH);\n"
                    + "digitalWrite(led3, HIGH);\n"
                    + "digitalWrite(led2, HIGH);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else if(slider1>=180/2){\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, HIGH);\n"
                    + "digitalWrite(led2, HIGH);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else if(slider1>=180/4){\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, LOW);\n"
                    + "digitalWrite(led2, HIGH);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else if(slider1>0){\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, LOW);\n"
                    + "digitalWrite(led2, LOW);\n"
                    + "digitalWrite(led1, HIGH);\n"
                    + "}else{\n"
                    + "digitalWrite(led4, LOW);\n"
                    + "digitalWrite(led3, LOW);\n"
                    + "digitalWrite(led2, LOW);\n"
                    + "digitalWrite(led1, LOW);\n"
                    + "}\n"
                    + "}\n";

            mSonido += ""
                    + "void sonido(){\n"
                    + "tone(buzzer, (hercio+slider1)*push);\n"
                    + "}\n";

            c[ubi] = 'X';
            ubi += 1;
            camino = false;

        }
        return camino;

    }

    private void gradosServo() {

        String gradosS = "";

        if (sinCero() == true) {

            while (conCero()) {

                gradosS += c[ubi];
                ubi += 1;

            }

        } else if (c[ubi] == '0') {

            gradosS += c[ubi];
            ubi += 1;

        }

        int grados = Integer.parseInt(gradosS);

        if (0 <= grados && grados <= 180) {

            inicializar += ""
                    + "int grados;\n"
                    + "int grado=" + grados + ";\n";

        } else {

            //ES MAYOR A 180 o MENOR A 0
        }
    }

    private void direccionServo() {

        if (c[ubi] == 'D') {

            mServo = ""
                    + "void servoM(){\n"
                    + "grados = map(grados,0,1023,0,grado);\n"
                    + "myservo.write(grados);\n"
                    + "}\n";

        } else if (c[ubi] == 'I') {

            mServo = ""
                    + "void servoM(){\n"
                    + "grados = map(grados,0,1023,grado,0);\n"
                    + "myservo.write(grados);\n"
                    + "}\n";

        }

        ubi += 1;

    }

    private void direccionMotor() {

        if (c[ubi] == 'D') {

            mMotor += "void motor(){\n"
                    + "if(slider1<90){\n"
                    + "digitalWrite(direccion, LOW);\n"
                    + "}else{\n"
                    + "digitalWrite(direccion, HIGH);\n"
                    + "}\n"
                    + "}";

        } else if (c[ubi] == 'I') {

            mMotor += "void motor(){\n"
                    + "if(slider1<90){\n"
                    + "digitalWrite(direccion, HIGH);\n"
                    + "}else{\n"
                    + "digitalWrite(direccion, LOW);\n"
                    + "}\n"
                    + "}";

        }

        ubi += 1;

    }

    private void pinServo() {

        if (c[ubi] == 'P') {

            ubi++;

            String pinS = "";

            if (paraPines()) {

                pinS += c[ubi];
                ubi++;

            } else if (c[ubi] == '1') {

                pinS += c[ubi];
                ubi++;

                if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3') {

                    pinS += c[ubi];
                    ubi++;

                }

            }

            int pin = Integer.parseInt(pinS);

            if (2 <= pin && pin <= 13) {

                inicializar += ""
                        + "int servo =" + pin + ";\n";

            } else {

                //ES MAYOR A 13 o MENOR A 0
            }

        }

    }

    private void pinMotor() {

        if (c[ubi] == 'P') {

            ubi++;

            String pinS = "";

            if (paraPines()) {

                pinS += c[ubi];
                ubi++;

            } else if (c[ubi] == '1') {

                pinS += c[ubi];
                ubi++;

                if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3') {

                    pinS += c[ubi];
                    ubi++;

                }

            }

            int pin = Integer.parseInt(pinS);

            if (2 <= pin && pin <= 13) {

                inicializar += ""
                        + "int slider1=0;\n"
                        + "#define direccion " + pin + " \n";

            } else {

                //ES MAYOR A 13 o MENOR A 0
            }

        }

    }

    private void pinLed(int led) {

        if (c[ubi] == 'P') {

            ubi++;

            String pinS = "";

            if (paraPines()) {

                pinS += c[ubi];
                ubi++;

            } else if (c[ubi] == '1') {

                pinS += c[ubi];
                ubi++;

                if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3') {

                    pinS += c[ubi];
                    ubi++;

                }

            }

            int pin = Integer.parseInt(pinS);

            if (2 <= pin && pin <= 13) {

                inicializar += ""
                        + "#define led" + led + " " + pin + "\n";

            } else {

                //ES MAYOR A 13 o MENOR A 0
            }

        }

    }

    private void pinBuzzer() {

        if (c[ubi] == 'P') {

            ubi++;

            String pinS = "";

            if (paraPines()) {

                pinS += c[ubi];
                ubi++;

            } else if (c[ubi] == '1') {

                pinS += c[ubi];
                ubi++;

                if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3') {

                    pinS += c[ubi];
                    ubi++;

                }

            }

            int pin = Integer.parseInt(pinS);

            if (2 <= pin && pin <= 13) {

                inicializar += ""
                        + "const int buzzer = " + pin + ";\n";

            } else {

                //ES MAYOR A 13 o MENOR A 0
            }

        }

    }

    private void herciosBuzzer() {

        if (c[ubi] == 'B') {

            ubi++;

            String herciosS = "";

            if (sinCero() == true) {

                while (conCero()) {

                    herciosS += c[ubi];
                    ubi++;

                }

            } else if (c[ubi] == '0') {

                herciosS += c[ubi];
                ubi++;

            }

            int hercios = Integer.parseInt(herciosS);

            if (0 <= hercios && hercios <= 1000) {

                inicializar += ""
                        + "int hercio=" + hercios + ";\n";

            } else {

                //ES MAYOR A 180 o MENOR A 0
            }
        }

    }

    private void pulsador() {

        if (c[ubi] == 'P') {

            ubi++;

            String pinS = "";

            if (paraPines()) {

                pinS += c[ubi];
                ubi++;

            } else if (c[ubi] == '1') {

                pinS += c[ubi];
                ubi++;

                if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3') {

                    pinS += c[ubi];
                    ubi++;

                }

            }

            int pin = Integer.parseInt(pinS);

            if (2 <= pin && pin <= 13) {

                inicializar += ""
                        + "int push=0;\n"
                        + "const int pulsador=" + pin + "; \n";

            } else {

                //ES MAYOR A 13 o MENOR A 0
            }

        }

    }

    private void pinSlider() {

        if (c[ubi] == 'A') {

            ubi++;

            if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3' || c[ubi] == '4' || c[ubi] == '5') {

                inicializar += ""
                        + "int slider =" + c[ubi] + ";\n";

            } else {

                //NO ES NINGUNO DE LOS ANTERIORES
            }
        }
    }

    private void finMetodos() {

        mApagar += ""
                + "void apagar(){\n"
                + "push=digitalRead(pulsador);\n"
                + "if (push==HIGH){\n"
                + "digitalWrite(led5,HIGH);\n"
                + "}\n"
                + "else {\n"
                + "digitalWrite(led5,LOW);\n"
                + "}\n"
                + "}\n";

    }

    private boolean sinCero() {

        if (c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3' || c[ubi] == '4' || c[ubi] == '5' || c[ubi] == '6' || c[ubi] == '7' || c[ubi] == '8' || c[ubi] == '9') {
            return true;
        } else {
            return false;
        }
    }

    private boolean conCero() {

        if (c[ubi] == '0' || c[ubi] == '1' || c[ubi] == '2' || c[ubi] == '3' || c[ubi] == '4' || c[ubi] == '5' || c[ubi] == '6' || c[ubi] == '7' || c[ubi] == '8' || c[ubi] == '9') {
            return true;
        } else {
            return false;
        }
    }

    private boolean paraPines() {

        if (c[ubi] == '2' || c[ubi] == '3' || c[ubi] == '4' || c[ubi] == '5' || c[ubi] == '6' || c[ubi] == '7' || c[ubi] == '8' || c[ubi] == '9') {
            return true;
        } else {
            return false;
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

        grupoServo = new javax.swing.ButtonGroup();
        grupoMotor = new javax.swing.ButtonGroup();
        panelSeleccion = new javax.swing.JPanel();
        imagenServo3 = new javax.swing.JLabel();
        headFijo3 = new javax.swing.JLabel();
        nombreDispositivo3 = new javax.swing.JLabel();
        lblGrados1 = new javax.swing.JLabel();
        jSeparator7 = new javax.swing.JSeparator();
        botonMotor = new javax.swing.JButton();
        botonServo = new javax.swing.JButton();
        nombreDispositivo6 = new javax.swing.JLabel();
        panelExito = new javax.swing.JPanel();
        imagenServo6 = new javax.swing.JLabel();
        headFijo9 = new javax.swing.JLabel();
        nombreDispositivo8 = new javax.swing.JLabel();
        lblCadena = new javax.swing.JLabel();
        lblCadena1 = new javax.swing.JLabel();
        lblCadena2 = new javax.swing.JLabel();
        panelServo = new javax.swing.JPanel();
        imagenServo = new javax.swing.JLabel();
        headFijo = new javax.swing.JLabel();
        nombreDispositivo = new javax.swing.JLabel();
        lblGrados = new javax.swing.JLabel();
        gradosServo = new javax.swing.JSlider();
        lblDireccion = new javax.swing.JLabel();
        derecha = new javax.swing.JRadioButton();
        izquierda = new javax.swing.JRadioButton();
        lblDerecha = new javax.swing.JLabel();
        lblIzquierda = new javax.swing.JLabel();
        etiquetaGrado = new javax.swing.JLabel();
        servoTerminado = new javax.swing.JButton();
        lblPin = new javax.swing.JLabel();
        selectPinServo = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        panelLedsServo = new javax.swing.JPanel();
        headFijo2 = new javax.swing.JLabel();
        nombreDispositivo2 = new javax.swing.JLabel();
        ledsTerminadosServo = new javax.swing.JButton();
        lblPin2 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel1 = new javax.swing.JLabel();
        selectPin1Servo = new javax.swing.JComboBox<>();
        jLabel2 = new javax.swing.JLabel();
        selectPin2Servo = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        selectPin3Servo = new javax.swing.JComboBox<>();
        selectPin4Servo = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        headFijo6 = new javax.swing.JLabel();
        panelMotor = new javax.swing.JPanel();
        imagenServo1 = new javax.swing.JLabel();
        headFijo1 = new javax.swing.JLabel();
        nombreDispositivo1 = new javax.swing.JLabel();
        lblDireccion1 = new javax.swing.JLabel();
        noReloj = new javax.swing.JRadioButton();
        reloj = new javax.swing.JRadioButton();
        lblDerecha1 = new javax.swing.JLabel();
        lblIzquierda1 = new javax.swing.JLabel();
        servoTerminado1 = new javax.swing.JButton();
        lblPin1 = new javax.swing.JLabel();
        selectPinMotor = new javax.swing.JComboBox<>();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator6 = new javax.swing.JSeparator();
        panelBuzzer = new javax.swing.JPanel();
        imagenServo4 = new javax.swing.JLabel();
        headFijo4 = new javax.swing.JLabel();
        nombreDispositivo4 = new javax.swing.JLabel();
        lblDireccion2 = new javax.swing.JLabel();
        buzzerTerminado = new javax.swing.JButton();
        lblPin4 = new javax.swing.JLabel();
        selectPinBuzzer = new javax.swing.JComboBox<>();
        jSeparator8 = new javax.swing.JSeparator();
        intencidadBuzzer = new javax.swing.JSlider();
        etiquetaBuzzer = new javax.swing.JLabel();
        panelFin = new javax.swing.JPanel();
        imagenServo5 = new javax.swing.JLabel();
        headFijo5 = new javax.swing.JLabel();
        nombreDispositivo5 = new javax.swing.JLabel();
        generarCodigo = new javax.swing.JButton();
        jSeparator9 = new javax.swing.JSeparator();
        selectPinSlider = new javax.swing.JComboBox<>();
        selectPinEstado = new javax.swing.JComboBox<>();
        selectPinApagar = new javax.swing.JComboBox<>();
        lblDireccion3 = new javax.swing.JLabel();
        lblDireccion4 = new javax.swing.JLabel();
        lblDireccion5 = new javax.swing.JLabel();
        Menu = new javax.swing.JPanel();
        salir = new javax.swing.JButton();
        btnHome = new javax.swing.JButton();
        panelLedsMotor = new javax.swing.JPanel();
        headFijo7 = new javax.swing.JLabel();
        nombreDispositivo7 = new javax.swing.JLabel();
        ledsMotorTerminado = new javax.swing.JButton();
        lblPin3 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jLabel5 = new javax.swing.JLabel();
        selectPin1Motor = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        selectPin2Motor = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        selectPin3Motor = new javax.swing.JComboBox<>();
        selectPin4Motor = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        headFijo8 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(new java.awt.Color(255, 0, 51));
        setMinimumSize(new java.awt.Dimension(400, 600));

        panelSeleccion.setBackground(new java.awt.Color(255, 255, 255));
        panelSeleccion.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        imagenServo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenServo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/seleccionar.png"))); // NOI18N

        headFijo3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo3.setAlignmentY(0.0F);

        nombreDispositivo3.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo3.setText("Servo");

        lblGrados1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblGrados1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrados1.setText("Selecciona un elemento:");

        botonMotor.setBackground(new java.awt.Color(255, 255, 255));
        botonMotor.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/Motor2.png"))); // NOI18N
        botonMotor.setBorder(null);
        botonMotor.setBorderPainted(false);
        botonMotor.setContentAreaFilled(false);
        botonMotor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonMotorActionPerformed(evt);
            }
        });

        botonServo.setBackground(new java.awt.Color(255, 255, 255));
        botonServo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/servo.png"))); // NOI18N
        botonServo.setBorder(null);
        botonServo.setBorderPainted(false);
        botonServo.setContentAreaFilled(false);
        botonServo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                botonServoActionPerformed(evt);
            }
        });

        nombreDispositivo6.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo6.setText("Motor");

        javax.swing.GroupLayout panelSeleccionLayout = new javax.swing.GroupLayout(panelSeleccion);
        panelSeleccion.setLayout(panelSeleccionLayout);
        panelSeleccionLayout.setHorizontalGroup(
            panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSeleccionLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelSeleccionLayout.createSequentialGroup()
                        .addComponent(botonServo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(botonMotor)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelSeleccionLayout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(nombreDispositivo3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(nombreDispositivo6)
                        .addGap(52, 52, 52))))
            .addGroup(panelSeleccionLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(imagenServo3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelSeleccionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator7)
                    .addComponent(lblGrados1, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
            .addGroup(panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headFijo3, javax.swing.GroupLayout.PREFERRED_SIZE, 311, Short.MAX_VALUE))
        );
        panelSeleccionLayout.setVerticalGroup(
            panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelSeleccionLayout.createSequentialGroup()
                .addGap(74, 74, 74)
                .addComponent(imagenServo3)
                .addGap(46, 46, 46)
                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGrados1, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(53, 53, 53)
                .addGroup(panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelSeleccionLayout.createSequentialGroup()
                        .addGap(7, 7, 7)
                        .addComponent(botonServo)
                        .addGap(18, 18, 18)
                        .addComponent(nombreDispositivo3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(panelSeleccionLayout.createSequentialGroup()
                        .addComponent(botonMotor, javax.swing.GroupLayout.DEFAULT_SIZE, 99, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreDispositivo6, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(101, 101, 101))))
            .addGroup(panelSeleccionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelSeleccionLayout.createSequentialGroup()
                    .addComponent(headFijo3, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 383, Short.MAX_VALUE)))
        );

        panelExito.setBackground(new java.awt.Color(255, 255, 255));
        panelExito.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        imagenServo6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenServo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/exito.gif"))); // NOI18N

        headFijo9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo9.setAlignmentY(0.0F);

        nombreDispositivo8.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo8.setText("¡Muy Bien!");

        lblCadena.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lblCadena.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCadena.setText("Cadena");

        lblCadena1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCadena1.setText("Busca el archivo arduino en tu PC");

        lblCadena2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblCadena2.setText("con este ID:");

        javax.swing.GroupLayout panelExitoLayout = new javax.swing.GroupLayout(panelExito);
        panelExito.setLayout(panelExitoLayout);
        panelExitoLayout.setHorizontalGroup(
            panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExitoLayout.createSequentialGroup()
                .addGroup(panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelExitoLayout.createSequentialGroup()
                            .addGap(85, 85, 85)
                            .addComponent(nombreDispositivo8))
                        .addGroup(panelExitoLayout.createSequentialGroup()
                            .addGap(19, 19, 19)
                            .addComponent(imagenServo6, javax.swing.GroupLayout.PREFERRED_SIZE, 268, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelExitoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(lblCadena, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelExitoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblCadena1, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblCadena2, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headFijo9, javax.swing.GroupLayout.PREFERRED_SIZE, 311, Short.MAX_VALUE))
        );
        panelExitoLayout.setVerticalGroup(
            panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelExitoLayout.createSequentialGroup()
                .addGap(148, 148, 148)
                .addComponent(lblCadena1, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCadena2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblCadena)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreDispositivo8, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imagenServo6, javax.swing.GroupLayout.PREFERRED_SIZE, 256, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(panelExitoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelExitoLayout.createSequentialGroup()
                    .addComponent(headFijo9, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 383, Short.MAX_VALUE)))
        );

        panelServo.setBackground(new java.awt.Color(255, 255, 255));
        panelServo.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        imagenServo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenServo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/ServoMotor.png"))); // NOI18N

        headFijo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo.setAlignmentY(0.0F);

        nombreDispositivo.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo.setText("Servo");

        lblGrados.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblGrados.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblGrados.setText("Grado máximo de movimiento");

        gradosServo.setMaximum(180);
        gradosServo.setValue(170);
        gradosServo.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                gradosServoStateChanged(evt);
            }
        });

        lblDireccion.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDireccion.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDireccion.setText("Dirección de inicio");

        grupoServo.add(derecha);
        derecha.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                derechaActionPerformed(evt);
            }
        });

        grupoServo.add(izquierda);
        izquierda.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                izquierdaActionPerformed(evt);
            }
        });

        lblDerecha.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDerecha.setText("Derecha");

        lblIzquierda.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblIzquierda.setText("Izquierda");

        etiquetaGrado.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        etiquetaGrado.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaGrado.setText("170 °");

        servoTerminado.setBackground(new java.awt.Color(255, 255, 255));
        servoTerminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/next.jpg"))); // NOI18N
        servoTerminado.setBorder(null);
        servoTerminado.setBorderPainted(false);
        servoTerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servoTerminadoActionPerformed(evt);
            }
        });

        lblPin.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPin.setText("Seleccione un PIN");

        selectPinServo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPinServo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPinServoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelServoLayout = new javax.swing.GroupLayout(panelServo);
        panelServo.setLayout(panelServoLayout);
        panelServoLayout.setHorizontalGroup(
            panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServoLayout.createSequentialGroup()
                .addGap(87, 87, 87)
                .addComponent(imagenServo)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelServoLayout.createSequentialGroup()
                .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelServoLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelServoLayout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(lblDireccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelServoLayout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(lblIzquierda)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(izquierda)
                                        .addGap(18, 18, 18)
                                        .addComponent(derecha)
                                        .addGap(6, 6, 6)
                                        .addComponent(lblDerecha))
                                    .addComponent(gradosServo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(etiquetaGrado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jSeparator3)
                                    .addComponent(lblGrados, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(panelServoLayout.createSequentialGroup()
                                    .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblPin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(selectPinServo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(servoTerminado, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(panelServoLayout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nombreDispositivo, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(37, 37, 37))
            .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 311, Short.MAX_VALUE))
        );
        panelServoLayout.setVerticalGroup(
            panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServoLayout.createSequentialGroup()
                .addGap(53, 53, 53)
                .addComponent(imagenServo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreDispositivo, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblGrados, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(gradosServo, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(etiquetaGrado, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(lblDireccion, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(izquierda)
                    .addComponent(lblDerecha)
                    .addComponent(lblIzquierda)
                    .addComponent(derecha))
                .addGap(13, 13, 13)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelServoLayout.createSequentialGroup()
                        .addGap(0, 14, Short.MAX_VALUE)
                        .addComponent(servoTerminado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelServoLayout.createSequentialGroup()
                        .addComponent(lblPin, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectPinServo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(panelServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelServoLayout.createSequentialGroup()
                    .addComponent(headFijo, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 383, Short.MAX_VALUE)))
        );

        panelLedsServo.setBackground(new java.awt.Color(255, 255, 255));
        panelLedsServo.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        headFijo2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led.png"))); // NOI18N
        headFijo2.setAlignmentY(0.0F);

        nombreDispositivo2.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo2.setText("Leds Servo");

        ledsTerminadosServo.setBackground(new java.awt.Color(255, 255, 255));
        ledsTerminadosServo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/next.jpg"))); // NOI18N
        ledsTerminadosServo.setBorder(null);
        ledsTerminadosServo.setBorderPainted(false);
        ledsTerminadosServo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledsTerminadosServoActionPerformed(evt);
            }
        });

        lblPin2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPin2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPin2.setText("Seleccione un PIN por Led");

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel1.setText("1.");

        selectPin1Servo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin1Servo.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                selectPin1ServoItemStateChanged(evt);
            }
        });
        selectPin1Servo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectPin1ServoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                selectPin1ServoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                selectPin1ServoMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                selectPin1ServoMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                selectPin1ServoMouseReleased(evt);
            }
        });
        selectPin1Servo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin1ServoActionPerformed(evt);
            }
        });
        selectPin1Servo.addVetoableChangeListener(new java.beans.VetoableChangeListener() {
            public void vetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {
                selectPin1ServoVetoableChange(evt);
            }
        });

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel2.setText("2.");

        selectPin2Servo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin2Servo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                selectPin2ServoMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                selectPin2ServoMouseEntered(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                selectPin2ServoMouseReleased(evt);
            }
        });
        selectPin2Servo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin2ServoActionPerformed(evt);
            }
        });

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel3.setText("3.");

        selectPin3Servo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin3Servo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                selectPin3ServoMouseReleased(evt);
            }
        });
        selectPin3Servo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin3ServoActionPerformed(evt);
            }
        });

        selectPin4Servo.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin4Servo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin4ServoActionPerformed(evt);
            }
        });

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel4.setText("4.");

        headFijo6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo6.setAlignmentY(0.0F);

        javax.swing.GroupLayout panelLedsServoLayout = new javax.swing.GroupLayout(panelLedsServo);
        panelLedsServo.setLayout(panelLedsServoLayout);
        panelLedsServoLayout.setHorizontalGroup(
            panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLedsServoLayout.createSequentialGroup()
                .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLedsServoLayout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(headFijo2))
                    .addGroup(panelLedsServoLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nombreDispositivo2)
                            .addComponent(lblPin2)
                            .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ledsTerminadosServo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelLedsServoLayout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin2Servo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelLedsServoLayout.createSequentialGroup()
                                    .addComponent(jLabel3)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin3Servo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelLedsServoLayout.createSequentialGroup()
                                    .addComponent(jLabel4)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin4Servo, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelLedsServoLayout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin1Servo, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLedsServoLayout.createSequentialGroup()
                    .addComponent(headFijo6, javax.swing.GroupLayout.PREFERRED_SIZE, 321, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panelLedsServoLayout.setVerticalGroup(
            panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLedsServoLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(headFijo2, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreDispositivo2, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPin2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(selectPin1Servo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(selectPin2Servo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(selectPin3Servo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(selectPin4Servo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(ledsTerminadosServo, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelLedsServoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLedsServoLayout.createSequentialGroup()
                    .addComponent(headFijo6, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 373, Short.MAX_VALUE)))
        );

        panelMotor.setBackground(new java.awt.Color(255, 255, 255));
        panelMotor.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        imagenServo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenServo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/Motor.png"))); // NOI18N

        headFijo1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo1.setAlignmentY(0.0F);

        nombreDispositivo1.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo1.setText("Motor");

        lblDireccion1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDireccion1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDireccion1.setText("Dirección de movimiento");

        grupoMotor.add(noReloj);
        noReloj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noRelojActionPerformed(evt);
            }
        });

        grupoMotor.add(reloj);
        reloj.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                relojActionPerformed(evt);
            }
        });

        lblDerecha1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblDerecha1.setHorizontalAlignment(javax.swing.SwingConstants.TRAILING);
        lblDerecha1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/direccionL.png"))); // NOI18N

        lblIzquierda1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        lblIzquierda1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/direccionR.png"))); // NOI18N

        servoTerminado1.setBackground(new java.awt.Color(255, 255, 255));
        servoTerminado1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/next.jpg"))); // NOI18N
        servoTerminado1.setBorder(null);
        servoTerminado1.setBorderPainted(false);
        servoTerminado1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                servoTerminado1ActionPerformed(evt);
            }
        });

        lblPin1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPin1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPin1.setText("Seleccione un PIN");

        selectPinMotor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPinMotor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPinMotorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelMotorLayout = new javax.swing.GroupLayout(panelMotor);
        panelMotor.setLayout(panelMotorLayout);
        panelMotorLayout.setHorizontalGroup(
            panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMotorLayout.createSequentialGroup()
                .addGap(51, 51, 51)
                .addComponent(lblIzquierda1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(reloj)
                .addGap(18, 18, 18)
                .addComponent(noReloj)
                .addGap(6, 6, 6)
                .addComponent(lblDerecha1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelMotorLayout.createSequentialGroup()
                .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelMotorLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(servoTerminado1, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelMotorLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator2)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMotorLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addComponent(imagenServo1)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(panelMotorLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(nombreDispositivo1)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelMotorLayout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(lblPin1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                                    .addComponent(selectPinMotor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(lblDireccion1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(37, 37, 37))
            .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headFijo1, javax.swing.GroupLayout.PREFERRED_SIZE, 311, Short.MAX_VALUE))
        );
        panelMotorLayout.setVerticalGroup(
            panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelMotorLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addComponent(imagenServo1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreDispositivo1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblDireccion1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDerecha1)
                    .addComponent(lblIzquierda1)
                    .addGroup(panelMotorLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(reloj)
                            .addComponent(noReloj))))
                .addGap(27, 27, 27)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPin1, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(selectPinMotor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(servoTerminado1, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelMotorLayout.createSequentialGroup()
                    .addComponent(headFijo1, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 383, Short.MAX_VALUE)))
        );

        panelBuzzer.setBackground(new java.awt.Color(255, 255, 255));
        panelBuzzer.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        imagenServo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenServo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/sonido.png"))); // NOI18N

        headFijo4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo4.setAlignmentY(0.0F);

        nombreDispositivo4.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo4.setText("Buzzer");

        lblDireccion2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDireccion2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblDireccion2.setText("Hercios para el sonido");

        buzzerTerminado.setBackground(new java.awt.Color(255, 255, 255));
        buzzerTerminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/next.jpg"))); // NOI18N
        buzzerTerminado.setBorder(null);
        buzzerTerminado.setBorderPainted(false);
        buzzerTerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buzzerTerminadoActionPerformed(evt);
            }
        });

        lblPin4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPin4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPin4.setText("Seleccione un PIN");

        selectPinBuzzer.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPinBuzzer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPinBuzzerActionPerformed(evt);
            }
        });

        intencidadBuzzer.setMaximum(1000);
        intencidadBuzzer.setValue(700);
        intencidadBuzzer.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                intencidadBuzzerStateChanged(evt);
            }
        });

        etiquetaBuzzer.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        etiquetaBuzzer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        etiquetaBuzzer.setText("700 °");

        javax.swing.GroupLayout panelBuzzerLayout = new javax.swing.GroupLayout(panelBuzzer);
        panelBuzzer.setLayout(panelBuzzerLayout);
        panelBuzzerLayout.setHorizontalGroup(
            panelBuzzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelBuzzerLayout.createSequentialGroup()
                .addGroup(panelBuzzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(panelBuzzerLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(imagenServo4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nombreDispositivo4))
                    .addGroup(panelBuzzerLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(panelBuzzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(selectPinBuzzer, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblPin4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(etiquetaBuzzer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblDireccion2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(intencidadBuzzer, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                            .addComponent(buzzerTerminado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(37, 37, 37))
            .addGroup(panelBuzzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headFijo4, javax.swing.GroupLayout.PREFERRED_SIZE, 311, Short.MAX_VALUE))
        );
        panelBuzzerLayout.setVerticalGroup(
            panelBuzzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelBuzzerLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(imagenServo4)
                .addGap(18, 18, 18)
                .addComponent(nombreDispositivo4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblDireccion2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(intencidadBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(etiquetaBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(lblPin4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(selectPinBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(buzzerTerminado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelBuzzerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelBuzzerLayout.createSequentialGroup()
                    .addComponent(headFijo4, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 383, Short.MAX_VALUE)))
        );

        panelFin.setBackground(new java.awt.Color(255, 255, 255));
        panelFin.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        imagenServo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        imagenServo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/final.png"))); // NOI18N

        headFijo5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo5.setAlignmentY(0.0F);

        nombreDispositivo5.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo5.setText("Falta poco");

        generarCodigo.setBackground(new java.awt.Color(255, 255, 255));
        generarCodigo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/confirmar.png"))); // NOI18N
        generarCodigo.setBorder(null);
        generarCodigo.setBorderPainted(false);
        generarCodigo.setContentAreaFilled(false);
        generarCodigo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generarCodigoActionPerformed(evt);
            }
        });

        selectPinSlider.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPinSlider.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPinSliderActionPerformed(evt);
            }
        });

        selectPinEstado.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPinEstado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPinEstadoActionPerformed(evt);
            }
        });

        selectPinApagar.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPinApagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPinApagarActionPerformed(evt);
            }
        });

        lblDireccion3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDireccion3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDireccion3.setText("Slider");

        lblDireccion4.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDireccion4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDireccion4.setText("Led estado");

        lblDireccion5.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblDireccion5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblDireccion5.setText("Botón apagar");

        javax.swing.GroupLayout panelFinLayout = new javax.swing.GroupLayout(panelFin);
        panelFin.setLayout(panelFinLayout);
        panelFinLayout.setHorizontalGroup(
            panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator9)
                    .addGroup(panelFinLayout.createSequentialGroup()
                        .addComponent(lblDireccion4, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectPinEstado, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFinLayout.createSequentialGroup()
                        .addComponent(lblDireccion3, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(selectPinSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFinLayout.createSequentialGroup()
                        .addComponent(lblDireccion5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(selectPinApagar, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(panelFinLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(nombreDispositivo5)))
                .addGap(37, 37, 37))
            .addGroup(panelFinLayout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(imagenServo5)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelFinLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(generarCodigo)
                .addContainerGap())
            .addGroup(panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(headFijo5, javax.swing.GroupLayout.PREFERRED_SIZE, 311, Short.MAX_VALUE))
        );
        panelFinLayout.setVerticalGroup(
            panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelFinLayout.createSequentialGroup()
                .addGap(75, 75, 75)
                .addComponent(imagenServo5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(nombreDispositivo5, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addGroup(panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectPinEstado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccion4, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectPinApagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccion5, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(selectPinSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDireccion3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(generarCodigo))
            .addGroup(panelFinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelFinLayout.createSequentialGroup()
                    .addComponent(headFijo5, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 383, Short.MAX_VALUE)))
        );

        Menu.setBackground(new java.awt.Color(255, 255, 255));
        Menu.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 51, 204)));

        salir.setBackground(new java.awt.Color(255, 255, 255));
        salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/salir.png"))); // NOI18N
        salir.setBorder(null);
        salir.setBorderPainted(false);
        salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                salirActionPerformed(evt);
            }
        });

        btnHome.setBackground(new java.awt.Color(255, 255, 255));
        btnHome.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/home.png"))); // NOI18N
        btnHome.setBorder(null);
        btnHome.setBorderPainted(false);
        btnHome.setContentAreaFilled(false);
        btnHome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHomeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout MenuLayout = new javax.swing.GroupLayout(Menu);
        Menu.setLayout(MenuLayout);
        MenuLayout.setHorizontalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
            .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        MenuLayout.setVerticalGroup(
            MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MenuLayout.createSequentialGroup()
                .addComponent(btnHome, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(salir, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        panelLedsMotor.setBackground(new java.awt.Color(255, 255, 255));
        panelLedsMotor.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 0, 153)));

        headFijo7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led.png"))); // NOI18N
        headFijo7.setAlignmentY(0.0F);

        nombreDispositivo7.setFont(new java.awt.Font("Tahoma", 1, 24)); // NOI18N
        nombreDispositivo7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        nombreDispositivo7.setText("Leds Motor");

        ledsMotorTerminado.setBackground(new java.awt.Color(255, 255, 255));
        ledsMotorTerminado.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/next.jpg"))); // NOI18N
        ledsMotorTerminado.setBorder(null);
        ledsMotorTerminado.setBorderPainted(false);
        ledsMotorTerminado.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ledsMotorTerminadoActionPerformed(evt);
            }
        });

        lblPin3.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblPin3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblPin3.setText("Seleccione un PIN por Led");

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel5.setText("1.");

        selectPin1Motor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin1Motor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin1MotorActionPerformed(evt);
            }
        });

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel6.setText("2.");

        selectPin2Motor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin2Motor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin2MotorActionPerformed(evt);
            }
        });

        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel7.setText("3.");

        selectPin3Motor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin3Motor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin3MotorActionPerformed(evt);
            }
        });

        selectPin4Motor.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        selectPin4Motor.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectPin4MotorActionPerformed(evt);
            }
        });

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/led1.png"))); // NOI18N
        jLabel8.setText("4.");

        headFijo8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/arduino/images/superior.png"))); // NOI18N
        headFijo8.setAlignmentY(0.0F);

        javax.swing.GroupLayout panelLedsMotorLayout = new javax.swing.GroupLayout(panelLedsMotor);
        panelLedsMotor.setLayout(panelLedsMotorLayout);
        panelLedsMotorLayout.setHorizontalGroup(
            panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLedsMotorLayout.createSequentialGroup()
                .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelLedsMotorLayout.createSequentialGroup()
                        .addGap(105, 105, 105)
                        .addComponent(headFijo7))
                    .addGroup(panelLedsMotorLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(nombreDispositivo7)
                            .addComponent(lblPin3)
                            .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator10, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ledsMotorTerminado, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(panelLedsMotorLayout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin2Motor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelLedsMotorLayout.createSequentialGroup()
                                    .addComponent(jLabel7)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin3Motor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelLedsMotorLayout.createSequentialGroup()
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin4Motor, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGroup(panelLedsMotorLayout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addGap(18, 18, 18)
                                    .addComponent(selectPin1Motor, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelLedsMotorLayout.createSequentialGroup()
                    .addComponent(headFijo8, javax.swing.GroupLayout.PREFERRED_SIZE, 321, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        panelLedsMotorLayout.setVerticalGroup(
            panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelLedsMotorLayout.createSequentialGroup()
                .addGap(82, 82, 82)
                .addComponent(headFijo7, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(nombreDispositivo7, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblPin3, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(selectPin1Motor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(selectPin2Motor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(selectPin3Motor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(selectPin4Motor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 25, Short.MAX_VALUE)
                .addComponent(ledsMotorTerminado, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(panelLedsMotorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(panelLedsMotorLayout.createSequentialGroup()
                    .addComponent(headFijo8, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 373, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelSeleccion, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelServo, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelLedsServo, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelLedsMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelMotor, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelBuzzer, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelFin, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelExito, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(Menu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSeleccion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelServo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLedsServo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelLedsMotor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelMotor, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelBuzzer, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelExito, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelFin, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void salirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_salirActionPerformed
        // TODO add your handling code here:

        System.exit(WIDTH);
    }//GEN-LAST:event_salirActionPerformed

    private void selectPinServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPinServoActionPerformed
        // TODO add your handling code here:

        if (selectPinServo.getSelectedItem().toString() != "") {

            selectPinServo.disable();

            servoTerminado.setVisible(true);

        }
    }//GEN-LAST:event_selectPinServoActionPerformed

    private void servoTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servoTerminadoActionPerformed
        // TODO add your handling code here:

        //GRADOS
        cadena += gradosServo.getValue();

        //DIRECCION
        if (izquierda.isSelected()) {
            cadena += "I";
        } else if (derecha.isSelected()) {
            cadena += "D";
        }
        //PIN SELECIONADO
        String pinSeleccionado = selectPinServo.getSelectedItem().toString();
        cadena += pinSeleccionado;

        eliminar_posicion(pinSeleccionado);

        //AGREGAR PINES
        for (String campos : pines) {
            selectPin1Servo.addItem(campos);
        }

        panelServo.setVisible(false);
        panelLedsServo.setVisible(true);
    }//GEN-LAST:event_servoTerminadoActionPerformed

    private void izquierdaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_izquierdaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_izquierdaActionPerformed

    private void derechaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_derechaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_derechaActionPerformed

    private void gradosServoStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_gradosServoStateChanged
        // TODO add your handling code here:

        etiquetaGrado.setText(gradosServo.getValue() + " °");
    }//GEN-LAST:event_gradosServoStateChanged

    private void noRelojActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noRelojActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noRelojActionPerformed

    private void relojActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_relojActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_relojActionPerformed

    private void servoTerminado1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_servoTerminado1ActionPerformed
        // TODO add your handling code here:

        if (reloj.isSelected()) {
            cadena += "D";
        } else if (noReloj.isSelected()) {
            cadena += "I";
        }

        //PIN SELECIONADO
        String pinSeleccionado = selectPinMotor.getSelectedItem().toString();
        cadena += pinSeleccionado;

        eliminar_posicion(pinSeleccionado);

        //AGREGAR PINES
        for (String campos : pines) {
            selectPin1Motor.addItem(campos);
        }

        panelMotor.setVisible(false);
        panelLedsMotor.setVisible(true);
    }//GEN-LAST:event_servoTerminado1ActionPerformed

    private void selectPinMotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPinMotorActionPerformed
        // TODO add your handling code here:

        if (selectPinMotor.getSelectedItem().toString() != "") {

            selectPinMotor.disable();

        }

    }//GEN-LAST:event_selectPinMotorActionPerformed

    private void ledsTerminadosServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledsTerminadosServoActionPerformed
        // TODO add your handling code here:

        //AGREGAR PINES
        for (String campos : pines) {
            selectPinBuzzer.addItem(campos);
        }

        panelLedsServo.setVisible(false);
        panelBuzzer.setVisible(true);
    }//GEN-LAST:event_ledsTerminadosServoActionPerformed

    private void selectPin1ServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin1ServoActionPerformed
        // TODO add your handling code here:

        if (selectPin1Servo.getSelectedItem().toString() != "") {

            selectPin1Servo.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin1Servo.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPin2Servo.addItem(campos);
            }

            selectPin2Servo.setVisible(true);

        }
    }//GEN-LAST:event_selectPin1ServoActionPerformed

    private void selectPin2ServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin2ServoActionPerformed
        // TODO add your handling code here:

        if (selectPin2Servo.getSelectedItem().toString() != "") {

            selectPin2Servo.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin2Servo.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPin3Servo.addItem(campos);
            }

            selectPin3Servo.setVisible(true);

        }
    }//GEN-LAST:event_selectPin2ServoActionPerformed

    private void selectPin3ServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin3ServoActionPerformed
        // TODO add your handling code here:

        if (selectPin3Servo.getSelectedItem().toString() != "") {

            selectPin3Servo.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin3Servo.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPin4Servo.addItem(campos);
            }

            selectPin4Servo.setVisible(true);

        }
    }//GEN-LAST:event_selectPin3ServoActionPerformed

    private void selectPin4ServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin4ServoActionPerformed
        // TODO add your handling code here:

        if (selectPin4Servo.getSelectedItem().toString() != "") {

            selectPin4Servo.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin4Servo.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            ledsTerminadosServo.setVisible(true);

        }
    }//GEN-LAST:event_selectPin4ServoActionPerformed

    private void buzzerTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buzzerTerminadoActionPerformed
        // TODO add your handling code here:

        for (String campos : pines) {
            selectPinEstado.addItem(campos);
        }

        cadena += "B" + intencidadBuzzer.getValue();

        panelBuzzer.setVisible(false);
        panelFin.setVisible(true);
    }//GEN-LAST:event_buzzerTerminadoActionPerformed

    private void selectPinBuzzerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPinBuzzerActionPerformed
        // TODO add your handling code here:

        if (selectPinBuzzer.getSelectedItem().toString() != "") {

            selectPinBuzzer.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPinBuzzer.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            buzzerTerminado.setVisible(true);

        }


    }//GEN-LAST:event_selectPinBuzzerActionPerformed

    private void intencidadBuzzerStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_intencidadBuzzerStateChanged
        // TODO add your handling code here:

        etiquetaBuzzer.setText(intencidadBuzzer.getValue() + " °");
    }//GEN-LAST:event_intencidadBuzzerStateChanged

    private void generarCodigoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generarCodigoActionPerformed
        // TODO add your handling code here:

        cadena += selectPinSlider.getSelectedItem().toString();

        lblCadena.setText(cadena);

        try {
            revisarCadena();
        } catch (IOException ex) {
            lblCadena.setText("ERROR AL CREAR EL DOCUMENTO");
        }

        panelFin.setVisible(false);
        panelExito.setVisible(true);
    }//GEN-LAST:event_generarCodigoActionPerformed

    private void botonMotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonMotorActionPerformed
        // TODO add your handling code here:
        cadena = "M";

        for (String campos : pines) {
            selectPinMotor.addItem(campos);
        }

        panelSeleccion.setVisible(false);
        panelMotor.setVisible(true);
    }//GEN-LAST:event_botonMotorActionPerformed

    private void botonServoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_botonServoActionPerformed
        // TODO add your handling code here:

        cadena = "S";
        panelSeleccion.setVisible(false);
        panelServo.setVisible(true);
    }//GEN-LAST:event_botonServoActionPerformed

    private void ledsMotorTerminadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ledsMotorTerminadoActionPerformed
        // TODO add your handling code here:

        for (String campos : pines) {
            selectPinBuzzer.addItem(campos);
        }

        panelLedsMotor.setVisible(false);
        panelBuzzer.setVisible(true);
    }//GEN-LAST:event_ledsMotorTerminadoActionPerformed

    private void selectPin1MotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin1MotorActionPerformed
        // TODO add your handling code here:

        if (selectPin1Motor.getSelectedItem().toString() != "") {

            selectPin1Motor.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin1Motor.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPin2Motor.addItem(campos);
            }

            selectPin2Motor.setVisible(true);

        }
    }//GEN-LAST:event_selectPin1MotorActionPerformed

    private void selectPin2MotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin2MotorActionPerformed
        // TODO add your handling code here:

        if (selectPin2Motor.getSelectedItem().toString() != "") {

            selectPin2Motor.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin2Motor.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPin3Motor.addItem(campos);
            }

            selectPin3Motor.setVisible(true);

        }

    }//GEN-LAST:event_selectPin2MotorActionPerformed

    private void selectPin3MotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin3MotorActionPerformed
        // TODO add your handling code here:

        if (selectPin3Motor.getSelectedItem().toString() != "") {

            selectPin3Motor.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin3Motor.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPin4Motor.addItem(campos);
            }

            selectPin4Motor.setVisible(true);

        }
    }//GEN-LAST:event_selectPin3MotorActionPerformed

    private void selectPin4MotorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPin4MotorActionPerformed
        // TODO add your handling code here:

        if (selectPin4Motor.getSelectedItem().toString() != "") {

            selectPin4Motor.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPin4Motor.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            ledsMotorTerminado.setVisible(true);

        }
    }//GEN-LAST:event_selectPin4MotorActionPerformed

    private void selectPin1ServoVetoableChange(java.beans.PropertyChangeEvent evt)throws java.beans.PropertyVetoException {//GEN-FIRST:event_selectPin1ServoVetoableChange
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin1ServoVetoableChange

    private void selectPin1ServoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin1ServoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin1ServoMouseEntered

    private void selectPin1ServoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_selectPin1ServoItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin1ServoItemStateChanged

    private void selectPin1ServoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin1ServoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin1ServoMouseReleased

    private void selectPin2ServoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin2ServoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin2ServoMouseReleased

    private void selectPin3ServoMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin3ServoMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin3ServoMouseReleased

    private void selectPin1ServoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin1ServoMouseClicked
        // TODO add your handling code here:


    }//GEN-LAST:event_selectPin1ServoMouseClicked

    private void selectPin1ServoMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin1ServoMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin1ServoMousePressed

    private void selectPin2ServoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin2ServoMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin2ServoMouseEntered

    private void selectPin1ServoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin1ServoMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin1ServoMouseExited

    private void selectPin2ServoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_selectPin2ServoMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPin2ServoMouseClicked

    private void selectPinSliderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPinSliderActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_selectPinSliderActionPerformed

    private void selectPinEstadoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPinEstadoActionPerformed
        // TODO add your handling code here:

        if (selectPinEstado.getSelectedItem().toString() != "") {

            selectPinEstado.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPinEstado.getSelectedItem().toString();
            cadena += pinSeleccionado;

            eliminar_posicion(pinSeleccionado);

            //AGREGAR PINES
            for (String campos : pines) {
                selectPinApagar.addItem(campos);
            }

            selectPinApagar.setVisible(true);

        }

    }//GEN-LAST:event_selectPinEstadoActionPerformed

    private void selectPinApagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectPinApagarActionPerformed
        // TODO add your handling code here:

        if (selectPinApagar.getSelectedItem().toString() != "") {

            selectPinApagar.disable();

            //PIN SELECIONADO
            String pinSeleccionado = selectPinApagar.getSelectedItem().toString();
            cadena += pinSeleccionado;

            generarCodigo.setVisible(true);

        }
    }//GEN-LAST:event_selectPinApagarActionPerformed

    private void btnHomeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHomeActionPerformed
        // TODO add your handling code here:

        main(null);

    }//GEN-LAST:event_btnHomeActionPerformed

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
            java.util.logging.Logger.getLogger(compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(compilador.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new compilador().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Menu;
    private javax.swing.JButton botonMotor;
    private javax.swing.JButton botonServo;
    private javax.swing.JButton btnHome;
    private javax.swing.JButton buzzerTerminado;
    private javax.swing.JRadioButton derecha;
    private javax.swing.JLabel etiquetaBuzzer;
    private javax.swing.JLabel etiquetaGrado;
    private javax.swing.JButton generarCodigo;
    private javax.swing.JSlider gradosServo;
    private javax.swing.ButtonGroup grupoMotor;
    private javax.swing.ButtonGroup grupoServo;
    private javax.swing.JLabel headFijo;
    private javax.swing.JLabel headFijo1;
    private javax.swing.JLabel headFijo2;
    private javax.swing.JLabel headFijo3;
    private javax.swing.JLabel headFijo4;
    private javax.swing.JLabel headFijo5;
    private javax.swing.JLabel headFijo6;
    private javax.swing.JLabel headFijo7;
    private javax.swing.JLabel headFijo8;
    private javax.swing.JLabel headFijo9;
    private javax.swing.JLabel imagenServo;
    private javax.swing.JLabel imagenServo1;
    private javax.swing.JLabel imagenServo3;
    private javax.swing.JLabel imagenServo4;
    private javax.swing.JLabel imagenServo5;
    private javax.swing.JLabel imagenServo6;
    private javax.swing.JSlider intencidadBuzzer;
    private javax.swing.JRadioButton izquierda;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JLabel lblCadena;
    private javax.swing.JLabel lblCadena1;
    private javax.swing.JLabel lblCadena2;
    private javax.swing.JLabel lblDerecha;
    private javax.swing.JLabel lblDerecha1;
    private javax.swing.JLabel lblDireccion;
    private javax.swing.JLabel lblDireccion1;
    private javax.swing.JLabel lblDireccion2;
    private javax.swing.JLabel lblDireccion3;
    private javax.swing.JLabel lblDireccion4;
    private javax.swing.JLabel lblDireccion5;
    private javax.swing.JLabel lblGrados;
    private javax.swing.JLabel lblGrados1;
    private javax.swing.JLabel lblIzquierda;
    private javax.swing.JLabel lblIzquierda1;
    private javax.swing.JLabel lblPin;
    private javax.swing.JLabel lblPin1;
    private javax.swing.JLabel lblPin2;
    private javax.swing.JLabel lblPin3;
    private javax.swing.JLabel lblPin4;
    private javax.swing.JButton ledsMotorTerminado;
    private javax.swing.JButton ledsTerminadosServo;
    private javax.swing.JRadioButton noReloj;
    private javax.swing.JLabel nombreDispositivo;
    private javax.swing.JLabel nombreDispositivo1;
    private javax.swing.JLabel nombreDispositivo2;
    private javax.swing.JLabel nombreDispositivo3;
    private javax.swing.JLabel nombreDispositivo4;
    private javax.swing.JLabel nombreDispositivo5;
    private javax.swing.JLabel nombreDispositivo6;
    private javax.swing.JLabel nombreDispositivo7;
    private javax.swing.JLabel nombreDispositivo8;
    private javax.swing.JPanel panelBuzzer;
    private javax.swing.JPanel panelExito;
    private javax.swing.JPanel panelFin;
    private javax.swing.JPanel panelLedsMotor;
    private javax.swing.JPanel panelLedsServo;
    private javax.swing.JPanel panelMotor;
    private javax.swing.JPanel panelSeleccion;
    private javax.swing.JPanel panelServo;
    private javax.swing.JRadioButton reloj;
    private javax.swing.JButton salir;
    private javax.swing.JComboBox<String> selectPin1Motor;
    private javax.swing.JComboBox<String> selectPin1Servo;
    private javax.swing.JComboBox<String> selectPin2Motor;
    private javax.swing.JComboBox<String> selectPin2Servo;
    private javax.swing.JComboBox<String> selectPin3Motor;
    private javax.swing.JComboBox<String> selectPin3Servo;
    private javax.swing.JComboBox<String> selectPin4Motor;
    private javax.swing.JComboBox<String> selectPin4Servo;
    private javax.swing.JComboBox<String> selectPinApagar;
    private javax.swing.JComboBox<String> selectPinBuzzer;
    private javax.swing.JComboBox<String> selectPinEstado;
    private javax.swing.JComboBox<String> selectPinMotor;
    private javax.swing.JComboBox<String> selectPinServo;
    private javax.swing.JComboBox<String> selectPinSlider;
    private javax.swing.JButton servoTerminado;
    private javax.swing.JButton servoTerminado1;
    // End of variables declaration//GEN-END:variables

}
