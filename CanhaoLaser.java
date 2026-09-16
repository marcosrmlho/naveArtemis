public class CanhaoLaser{
    private String idCanhao;
    private double cargaArma;
    private boolean prontoParaDisparo;


    public CanhaoLaser(String idCanhao){
        this.idCanhao = idCanhao;
        cargaArma = 0;
        prontoParaDisparo = false;
    }


    public String getIdCanhao(){
        return this.idCanhao;
    }

    public double getCargaArma(){
        return this.cargaArma;
    }

    public boolean isProntoParaDisparo(){
        return this.prontoParaDisparo;
    }

    public void setCargaArma(double cargaArma){
        this.cargaArma = cargaArma;
    }

    public void setProntoParaDisparo(boolean prontoParaDisparo){
        this.prontoParaDisparo = prontoParaDisparo;
    }


    public String toString(){
        return idCanhao + " " + cargaArma + " " + prontoParaDisparo;
    }

    public boolean equals(CanhaoLaser canhaoLaser){
        if (this.idCanhao == canhaoLaser.idCanhao) {
            return true;
        }
        else{
            return false;
        }
    }   


    public void carregarArma(TanqueCombustivel tanque){
        if(tanque.verificarVazamento() == false){
            tanque.consumir(10);
            this.cargaArma += 20;
            if(this.cargaArma > 50){
                this.prontoParaDisparo = true;
            }
        }
        else{
            System.out.println("Combustível instável, carregamento abortado!");
        }

    }

    public void atirar(MotorPropulsao motor){
        atirar(motor, 1);
    }

    public void atirar(MotorPropulsao motor, int quantidade){
        if(motor.isSuperaquecido()){
            System.out.println("Erro: instabilidade do motor. Disparo abortado!");
        } else {

            for (int i = 0; i < quantidade; i++){
                if (prontoParaDisparo == true){
                    cargaArma -= 50;
                    System.out.println("PEW! Asteroide destruído!");
                    if(this.cargaArma < 50){
                        desarmar();
                        break;
                    }
                }
            }

        }
    }

    public void desarmar(){
        this.cargaArma = 0;
        this.prontoParaDisparo = false;
    }
}