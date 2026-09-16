public class MotorPropulsao {
    private String idMotor;
    private  int potenciaAtual;
    private boolean superaquecido;

    public MotorPropulsao(String idMotor){
        this.idMotor = idMotor;
        this.potenciaAtual = 0;
        this.superaquecido = false;
    }

    public String getIdMotor(){
        return this.idMotor;
    }

    public int getPotenciaAtual(){
        return this.potenciaAtual;
    }

    public void setPotenciaAtual(int potencia){
        if(potencia >= 0 && potencia <= 100){
            this.potenciaAtual = potencia;
        }
    }

    public boolean isSuperaquecido(){
        return this.superaquecido;
    }

    public void setSuperaquecido(boolean status){
        this.superaquecido = status;
    }

    public String toString(){
        return "Motor [" + this.idMotor + 
                "] - Potência: [" + this.potenciaAtual + 
                "]% - Alerta: [" + this.superaquecido + "]";
    }

    public boolean equals(MotorPropulsao obj){
        return this.idMotor.equals(obj.getIdMotor());
    }

    public void acelerar(TanqueCombustivel tanque){
        if(this.superaquecido){
            System.out.println("Erro: Motor superaquecido. Aceleração bloqueada!");
        }
        else {
            if(tanque.getVolumeAtual() >= TanqueCombustivel.CONSUMO_PADRAO){ 
                tanque.consumir();
                if(this.potenciaAtual + 20 > 100) potenciaAtual = 100;
                else this.potenciaAtual += 20;
            }
        }
    }

    public void acelerar(TanqueCombustivel tanque, int incremento){
         if(superaquecido){
            System.out.println("Erro: Motor superaquecido. Aceleração bloqueada!");
        }
        else {
            double consumo = incremento * 0.75;
            if(tanque.getVolumeAtual() >= consumo){
                tanque.consumir(consumo);
                if(potenciaAtual + incremento > 100) potenciaAtual = 100;
                else potenciaAtual += incremento;
            }
        }
    }

    public void avaliarSuperaquecimento(){
        if(potenciaAtual == 100){
            superaquecido = true;
            potenciaAtual = potenciaAtual/2;
        }
    }

    public void acionarResfriamentoEmergencia(){
        potenciaAtual = 0;
        superaquecido = false;
        System.out.println("Resfriamento concluído. Motor pronto.");
    }
}